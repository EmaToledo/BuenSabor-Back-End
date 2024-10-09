package com.elbuensabor.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class IngredientDTO extends GenericDTO {

    private String denomination;
    private String unit;
    private Boolean availability;
    private Long ingredientCategoryID;

}
