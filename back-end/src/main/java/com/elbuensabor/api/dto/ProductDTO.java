package com.elbuensabor.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProductDTO extends GenericDTO {

    private String denomination;
    private String description;
    private Boolean availability;
    private Long productCategoryID;
    private PriceDTO price;
    private String categoryDenomination;

}
