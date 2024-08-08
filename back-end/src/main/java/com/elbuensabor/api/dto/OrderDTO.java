package com.elbuensabor.api.dto;

import com.elbuensabor.api.Enum.OrderStatus;
import com.elbuensabor.api.Enum.PaymentStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Time;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderDTO extends GenericDTO {
    private String address;
    private String apartment;
    private Double discount;
    private Time estimatedTime;
    private PaymentStatus paid;
    private boolean isCanceled;
    private String phone;
    private Double total;
    private Long userId;
    private String userName;
    private String userLastName;
    private String deliveryMethod;
    private OrderStatus state;
    private String paymentType;
    private List<OrderDetailDTO> orderDetails;
}
