package com.elbuensabor.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class RecipeDTO extends GenericDTO {

    private String denomination;
    private String description;
    private Long manufacturedProductId;
    private List<RecipeStepDTO> steps;
    private List<IngredientDTO> ingredients;

}