package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.ItemPaymentMarketDTO;
import com.elbuensabor.api.dto.OrderDTO;
import com.elbuensabor.api.dto.PreferenceMPDTO;
import com.elbuensabor.api.entity.ItemPaymentMarket;

import java.math.BigDecimal;

public interface PaymentMarketService {
    ItemPaymentMarketDTO savePreferenceID(OrderDTO dto, String preferenceId) throws Exception;
    void cancelPayment(Long orderId) throws  Exception;
    Double refundPaymentWithAmount(Long orderId,Double amount) throws  Exception;

    Double fullRefundPayment(Long orderId) throws  Exception;

    ItemPaymentMarket getByPreferenceId(String preferenceId) throws  Exception;

    String getPreferenceByOrderId(Long orderId) throws  Exception;

    ItemPaymentMarketDTO paidOrder(ItemPaymentMarketDTO dto) throws Exception;
}
