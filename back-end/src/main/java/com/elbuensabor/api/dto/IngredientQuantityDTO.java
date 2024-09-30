package com.elbuensabor.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class IngredientQuantityDTO extends GenericDTO {
    private IngredientDTO ingredient;
    private Long quantity;
}
