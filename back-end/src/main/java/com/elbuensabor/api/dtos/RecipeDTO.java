package com.elbuensabor.api.dtos;

import com.elbuensabor.api.entity.RecipeStep;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class RecipeDTO extends GenericDTO {

    private String denomination;
    private String description;
    private Long manufacturedProductId;
    private List<RecipeStepDTO> steps;

}
