package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.Enum.PaymentStatus;
import com.elbuensabor.api.dto.*;
import com.elbuensabor.api.entity.ItemPaymentMarket;
import com.elbuensabor.api.entity.User;
import com.elbuensabor.api.mapper.ItemPaymentMarketMapper;
import com.elbuensabor.api.repository.IItemPaymentMarketRepository;
import com.elbuensabor.api.repository.IOrderRepository;
import com.elbuensabor.api.service.PaymentMarketService;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentRefundClient;
import com.mercadopago.client.preference.*;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.payment.PaymentRefund;
import com.mercadopago.resources.preference.Preference;
import com.nimbusds.jose.shaded.gson.JsonObject;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
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

    // @Value("${ACCESS_TOKEN}")
    private String accessToken = "TEST-7851658657870878-061113-737f43506bf13887aa8929e82a046f37-294926653";
//    private static final String BASE_URL = "https://api.mercadopago.com/v1/payments/";

    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private IItemPaymentMarketRepository itemPaymentMarketRepository;
    @Autowired
    private ItemPaymentMarketMapper itemPaymentMarketMapper;


    @Transactional
    @Override
    public ItemPaymentMarketDTO savePreferenceID(OrderDTO dto ,String preferenceId)  throws Exception {
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
            Preference preference;
            ItemPaymentMarket itemPaymentMarket;
            if ( preferenceId != null && !preferenceId.trim().isEmpty()){
                preference = client.update(preferenceId,preferenceRequest);
                itemPaymentMarket =  itemPaymentMarketRepository.findItemPaymentMarketByPreferenceId(preferenceId);
            }else {
             preference = client.create(preferenceRequest);
             itemPaymentMarket = new ItemPaymentMarket();
            }
            itemPaymentMarket.setPreferenceId(preference.getId());
            itemPaymentMarket.setOrder(orderRepository.findById(dto.getId())
                    .orElseThrow(() -> new Exception("Orden no encontrado con el ID: " + dto.getId())));

            return itemPaymentMarketMapper.toDTO(itemPaymentMarketRepository.save(itemPaymentMarket));
        } catch (Exception e) {
            e.printStackTrace();
            throw  new Exception(e.getMessage());
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

 /*           String url = BASE_URL + paymentId;
            OkHttpClient client = new OkHttpClient();
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("status", "cancelled");
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestBody.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("Content-Type", "application/json")
                    .put(body)
                    .build();
            client.newCall(request).execute();*/
            MercadoPagoConfig.setAccessToken(this.accessToken);
            PaymentClient client = new PaymentClient();
//            Payment paymentCancelled = client.cancel(paymentId);
            client.cancel(itemPaymentMarket.getPaymentID());

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
      /*  OkHttpClient client = new OkHttpClient();
        try {
            String url = BASE_URL + paymentId + "/refunds";
            String idempotencyKey = UUID.randomUUID().toString();

            JsonObject requestBody = new JsonObject();
            if (amount != null) {
                requestBody.addProperty("amount", amount);
            }

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestBody.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-Idempotency-Key", idempotencyKey)
                    .post(body)
                    .build();

            client.newCall(request).execute();*/

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
