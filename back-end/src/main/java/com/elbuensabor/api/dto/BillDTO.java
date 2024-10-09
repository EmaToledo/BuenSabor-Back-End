package com.elbuensabor.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BillDTO extends GenericDTO{
    private Long orderId;
    private String base64;
}
