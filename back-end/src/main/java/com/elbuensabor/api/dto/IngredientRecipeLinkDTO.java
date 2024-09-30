package com.elbuensabor.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class IngredientRecipeLinkDTO extends GenericDTO {

    private Long recipeID;
    private Long ingredientID;
    private Long quantity;

}
