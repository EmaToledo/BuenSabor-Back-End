package com.elbuensabor.api.dto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PriceDTO extends GenericDTO {
    private Double sellPrice;
    private Double costPrice;
}