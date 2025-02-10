package com.elbuensabor.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ProductXStockDTO {
    private ProductDTO product;
    private StockDTO stock;
}
