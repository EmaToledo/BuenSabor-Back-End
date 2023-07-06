package com.example.api.dtos;

import lombok.Data;

@Data
public class IngredientDTO extends GenericDTO {

    private String denomination;
    private Long minStock;
    private Long actualStock;
    private String unit;
    private Boolean availability;
    private Long ingredientCategoryID;

}
