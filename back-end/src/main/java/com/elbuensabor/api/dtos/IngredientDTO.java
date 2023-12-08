package com.elbuensabor.api.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class IngredientDTO extends GenericDTO {

    private String denomination;
    private Long minStock;
    private Long actualStock;
    private String unit;
    private Boolean availability;
    private Long ingredientCategoryID;

}
