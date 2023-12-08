package com.elbuensabor.api.service;

import com.elbuensabor.api.dtos.RecipeDTO;
import com.elbuensabor.api.dtos.RecipeStepDTO;
import com.elbuensabor.api.entity.RecipeStep;

public interface RecipeStepService extends GenericService<RecipeStep, RecipeStepDTO, Long> {

    // Guarda un paso en una receta
    RecipeStep saveRecipeStep(RecipeStepDTO dto) throws Exception;

    // Actualiza un paso en una receta
    RecipeStep updateRecipeStep(Long id, RecipeStepDTO dto) throws Exception;

}
