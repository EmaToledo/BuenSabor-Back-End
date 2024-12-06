package com.elbuensabor.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StockDTO extends GenericDTO{

    private Long actualStock;
    private Long minStock;
    private Long productStockID;
    private Long ingredientStockID;

}
