package com.example.api.dtos;

import com.example.api.entity.RecipeStep;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class RecipeDTO extends GenericDTO {

    private String description;
    private Long manufacturedProductId;
    private List<RecipeStepDTO> steps;

}
