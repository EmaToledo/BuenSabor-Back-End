package com.elbuensabor.api.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)

public class OrderDetailDTO extends GenericDTO {
    private Integer quantity;
    private Double subtotal;
    private ProductDTO itemProduct;
    private ManufacturedProductDTO itemManufacturedProduct;
}
