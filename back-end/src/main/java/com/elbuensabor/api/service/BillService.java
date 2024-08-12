package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.BillDTO;
import com.elbuensabor.api.entity.Bill;

public interface BillService extends GenericService<Bill, BillDTO, Long> {
    // Crea una Factura
    Bill saveBill(BillDTO dto) throws Exception;

    BillDTO getBillByOrderId(Long orderId) throws  Exception;

    void cancelBill(Long orderId) throws  Exception;
}
