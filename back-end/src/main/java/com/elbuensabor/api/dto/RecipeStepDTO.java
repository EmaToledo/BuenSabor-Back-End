package com.elbuensabor.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class RecipeStepDTO extends GenericDTO {

    private String description;
    private int stepNumber;
    private Long recipeId;

}
