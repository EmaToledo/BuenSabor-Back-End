package com.example.api.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class RecipeStepDTO extends GenericDTO {

    private String description;
    private Long recipeId;

}
