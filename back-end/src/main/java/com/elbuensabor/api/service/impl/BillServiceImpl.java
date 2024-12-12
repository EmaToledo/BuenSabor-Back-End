package com.elbuensabor.api.service.impl;
import com.elbuensabor.api.Enum.OrderStatus;
import com.elbuensabor.api.dto.BillDTO;
import com.elbuensabor.api.dto.OrderDTO;
import com.elbuensabor.api.entity.Bill;
import com.elbuensabor.api.entity.Order;
import com.elbuensabor.api.mapper.BillMapper;
import com.elbuensabor.api.mapper.GenericMapper;
import com.elbuensabor.api.repository.IBillRepository;
import com.elbuensabor.api.repository.IOrderRepository;
import com.elbuensabor.api.service.BillService;
import com.elbuensabor.api.service.EmailSenderService;
import com.elbuensabor.api.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BillServiceImpl extends GenericServiceImpl<Bill, BillDTO, Long> implements BillService {
    @Autowired
    private IBillRepository billRepository;
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private PdfService pdfService;

    @Autowired
    private BillMapper billMapper;
    @Autowired
    private EmailSenderService emailSenderService;

    public BillServiceImpl(com.elbuensabor.api.repository.IGenericRepository<Bill, Long> IGenericRepository, GenericMapper<Bill, BillDTO> genericMapper) {
        super(IGenericRepository, genericMapper);
    }



    @Override
    public OrderDTO updateOrderState(Long id, String newState) throws Exception {
        return null;
    }


    /**
     * Crea y Guarda una Factura en la base de datos.
     *
     * @param dto DTO de la Factura a guardar
     * @return La Factura guardada
     * @throws Exception Si ocurre algún error durante el proceso de guardado
     */
    @Override
    @Transactional
    public Bill saveBill(BillDTO dto) throws Exception {
        try {
            Order order = orderRepository.findById(dto.getOrderId())
                    .orElseThrow(() -> new Exception("La Orden no existe"));
            Bill bill = new Bill();
            bill.setGenerationDate(LocalDateTime.now());
            bill.setOrder(order);
            bill = billRepository.save(bill);
            String pdf = pdfService.createPDF(bill);
            bill.setPdf(pdf);
            //emailSenderService.sendEmail(order.getUser().getEmail(),pdf,bill.getId());
            return billRepository.save(bill);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Trae una Factura de BD mediante el Pedido
     *
     * @param orderId id del Pedido para buscar su Factura
     * @return La Factura encontrada
     * @throws Exception Si ocurre algún error durante el proceso de busqueda
     */
    @Override
    @Transactional(readOnly = true)
    public BillDTO getBillByOrderId(Long orderId) throws Exception {
        try{
        Bill bill = billRepository.findByBillByOrderId(orderId);
        return billMapper.toDTO(bill);
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void cancelBill(Long orderId) throws Exception {
        try{
            Bill bill = billRepository.findByBillByOrderId(orderId);
            bill.setLowDate(LocalDateTime.now());
            String pdf = pdfService.createPDF(bill);
            bill.setPdf(pdf);
            billRepository.save(bill);
        }catch(Exception e){
            throw new Exception(e.getMessage());
        }
    }


}
