package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.Enum.PaymentStatus;
import com.elbuensabor.api.dto.*;
import com.elbuensabor.api.entity.ItemPaymentMarket;
import com.elbuensabor.api.entity.User;
import com.elbuensabor.api.mapper.ItemPaymentMarketMapper;
import com.elbuensabor.api.repository.IItemPaymentMarketRepository;
import com.elbuensabor.api.repository.IOrderRepository;
import com.elbuensabor.api.service.BillService;
import com.elbuensabor.api.service.PaymentMarketService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentRefundClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.payment.PaymentRefund;
import com.mercadopago.resources.preference.Preference;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentMarketServiceImpl implements PaymentMarketService {

    @Value("${MP_ACCESS_TOKEN}")
    private String accessToken;
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private IItemPaymentMarketRepository itemPaymentMarketRepository;
    @Autowired
    private ItemPaymentMarketMapper itemPaymentMarketMapper;
    @Autowired
    private BillService billService;

    @Transactional
    @Override
    public ItemPaymentMarketDTO savePreferenceID(OrderDTO dto)  throws Exception {
        try {
            MercadoPagoConfig.setAccessToken(this.accessToken);
            List<PreferenceItemRequest> items = new ArrayList<>();
            for (OrderDetailDTO orderDetailDTO : dto.getOrderDetails()) {
                PreferenceItemRequest itemRequest = createPreferenceItemRequest(orderDetailDTO.getItemProduct(), orderDetailDTO.getItemManufacturedProduct(), orderDetailDTO.getQuantity(), orderDetailDTO.getId());
                items.add(itemRequest);
            }
            if (dto.getDiscount() != null && dto.getDiscount() > 0) {
                // Añadir ítem de descuento negativo
                PreferenceItemRequest discountItem = PreferenceItemRequest.builder()
                        .id("discount")
                        .title("Discount")
                        .description("Descuento aplicado")
                        .quantity(1)
                        .currencyId("ARG")
                        .unitPrice(new BigDecimal(dto.getDiscount()).negate())// Precio negativo para representar el descuento
                        .build();
                items.add(discountItem);
            }

            PreferenceBackUrlsRequest backURL = PreferenceBackUrlsRequest.builder()
                    .success("http://localhost:5173/private/payment/")
                    .pending("http://localhost:5173/private/payment/")
                    .failure("http://localhost:5173/private/payment/")
                    .build();

            List<PreferencePaymentTypeRequest> excludedPaymentTypes = new ArrayList<>();
            excludedPaymentTypes.add(PreferencePaymentTypeRequest.builder().id("ticket").build());

            PreferencePaymentMethodsRequest paymentMethods =
                    PreferencePaymentMethodsRequest.builder()
                            .excludedPaymentTypes(excludedPaymentTypes)
                            .installments(2)
                            .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backURL)
                    .paymentMethods(paymentMethods)
                    .build();
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);
            ItemPaymentMarket itemPaymentMarket;
            itemPaymentMarket = new ItemPaymentMarket();
            itemPaymentMarket.setPreferenceId(preference.getId());
            itemPaymentMarket.setOrder(orderRepository.findById(dto.getId())
                    .orElseThrow(() -> new Exception("Orden no encontrado con el ID: " + dto.getId())));

            return itemPaymentMarketMapper.toDTO(itemPaymentMarketRepository.save(itemPaymentMarket));
        } catch (MPApiException e) {
            System.err.println("Código de error: " + e.getApiResponse().getStatusCode());
            System.err.println("Cuerpo de la respuesta: " + e.getApiResponse().getContent());
            throw new Exception("Error de MercadoPago: " + e.getApiResponse().getContent());
        }

    }


    private PreferenceItemRequest createPreferenceItemRequest(ProductDTO product, ManufacturedProductDTO manufacturedProductDTO, int quantity, long id) {
        if (product != null) {
            return PreferenceItemRequest.builder()
                    .id(String.valueOf(id))
                    .title(product.getDenomination())
                    .description(product.getDescription())
                    .quantity(quantity)
                    .currencyId("ARG")
                    .unitPrice(new BigDecimal(product.getPrice().getSellPrice()))
                    .build();
        }
        return PreferenceItemRequest.builder()
                .id(String.valueOf(id))
                .title(manufacturedProductDTO.getDenomination())
                .description(manufacturedProductDTO.getDescription())
                .quantity(quantity)
                .currencyId("ARG")
                .unitPrice(new BigDecimal(manufacturedProductDTO.getPrice().getSellPrice()))
                .build();
    }


    /*
     Cancelaciones solo se pueden realizar si el estado de pago es Pending o In process.
     Estos estados se muestran en la respuesta a la llamada de la API de cancelación en los
     campos de, Status y Status detail respectivamente.
    */
    @Override
    public void cancelPayment(Long orderId) throws Exception{
        try {
            ItemPaymentMarket itemPaymentMarket = itemPaymentMarketRepository.findItemPaymentMarketByOrderId(orderId);
            if (itemPaymentMarket.getPaymentID() != null){
                MercadoPagoConfig.setAccessToken(this.accessToken);
                PaymentClient client = new PaymentClient();
                client.cancel(itemPaymentMarket.getPaymentID());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error cancelling payment: " + e.getMessage());
        }
    }

    /*
    Crear reembolsos parciales/totales para un pago específico.
    Si el campo de suma ha sido completado, creará un reembolso parcial,
    en caso contrario, creará un reembolso total.
    @param amount Monto de reembolso. Si esta propiedad (monto) es removida del body,
    creará un reembolso total.
     */
    @Transactional
    @Override
    public Double refundPaymentWithAmount(Long orderId, Double amount) throws Exception{
        ItemPaymentMarket itemPaymentMarket = itemPaymentMarketRepository.findItemPaymentMarketByOrderId(orderId);
        MercadoPagoConfig.setAccessToken(this.accessToken);
        try{
          PaymentRefundClient client = new PaymentRefundClient();
            PaymentRefund refund =  client.refund( itemPaymentMarket.getPaymentID(), new BigDecimal(amount));
            itemPaymentMarket.setMountRefund(refund.getAmount().doubleValue());
            itemPaymentMarketRepository.save(itemPaymentMarket);
            return refund.getAmount().doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error processing refund with amount: " + e.getMessage());
        }
    }
    @Transactional
    @Override
    public Double fullRefundPayment(Long orderId) throws Exception{
        ItemPaymentMarket itemPaymentMarket = itemPaymentMarketRepository.findItemPaymentMarketByOrderId(orderId);
        MercadoPagoConfig.setAccessToken(this.accessToken);
        try{
            PaymentRefundClient client = new PaymentRefundClient();
            PaymentRefund refund = client.refund(itemPaymentMarket.getPaymentID());
            itemPaymentMarket.setMountRefund(refund.getAmount().doubleValue());
            itemPaymentMarketRepository.save(itemPaymentMarket);
            return refund.getAmount().doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error processing full refund: " + e.getMessage());
        }
    }
    @Transactional(readOnly = true)
    @Override
    public ItemPaymentMarket getByPreferenceId(String preferenceId) throws Exception{
        try{
            return itemPaymentMarketRepository.findItemPaymentMarketByPreferenceId(preferenceId);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error Get  ItemPaymentMarket by Preference : " + e.getMessage());
        }
    }
    @Transactional(readOnly = true)
    @Override
    public String getPreferenceByOrderId(Long orderId) throws Exception{
        try{
            return itemPaymentMarketRepository.getPreferenceByOrderId(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Error Get  Preference by order Id : " + e.getMessage());
        }
    }
    @Transactional()
    @Override
    public ItemPaymentMarketDTO paidOrder(ItemPaymentMarketDTO dto) throws Exception{
        try{
            ItemPaymentMarket itemPaymentMarket = itemPaymentMarketRepository.findItemPaymentMarketByPreferenceId(dto.getPreferenceId());
            if (dto.getStatus() != null){
            itemPaymentMarket.getOrder().setPaid(dto.getStatus());
            if (dto.getStatus() == PaymentStatus.approved){
                 //billService.sendBillByMail(itemPaymentMarket.getOrder().getId());
            }
            }else{
                itemPaymentMarket.getOrder().setPaid(PaymentStatus.rejected);
            }
            itemPaymentMarket.setPaymentID(dto.getPaymentID());
            return itemPaymentMarketMapper.toDTO(itemPaymentMarketRepository.save(itemPaymentMarket));
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception("Paid Order from Payment Market " + e.getMessage());
        }
    }
}
