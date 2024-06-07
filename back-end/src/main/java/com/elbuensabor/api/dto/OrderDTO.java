package com.elbuensabor.api.dto;

import com.elbuensabor.api.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderDTO extends GenericDTO {
    private String address;
    private String apartment;
    private Double discount;
    private Time estimatedTime;
    private boolean paid;
    private boolean isCanceled;
    private String phone;
    private Double total;
    private Long userId;
    private String userName;
    private String userLastName;
    private String deliveryMethod;
    private boolean state;
    private String paymentType;
    private List<OrderDetailDTO> orderDetails;
}
