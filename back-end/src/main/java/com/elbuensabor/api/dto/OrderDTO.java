package com.elbuensabor.api.dto;

import com.elbuensabor.api.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderDTO extends GenericDTO {
    private User user;
    private String deliveryMethod;
    private Date dateTime;
    private boolean state;
    private boolean isCanceled;
}
