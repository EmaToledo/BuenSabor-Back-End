package com.elbuensabor.api.dto;

import com.elbuensabor.api.Enum.PaymentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ItemPaymentMarketDTO extends  GenericDTO {
    private  String preferenceId;
    private Long paymentID;
    private PaymentStatus status;
   // private Long orderId;
}
