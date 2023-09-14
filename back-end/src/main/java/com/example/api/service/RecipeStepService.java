package com.example.api.service;

import com.example.api.dtos.RecipeDTO;
import com.example.api.dtos.RecipeStepDTO;
import com.example.api.entity.RecipeStep;

public interface RecipeStepService extends GenericService<RecipeStep, RecipeStepDTO, Long> {

    // Guarda un paso en una receta
    RecipeStep saveRecipeStep(RecipeStepDTO dto) throws Exception;

    // Actualiza un paso en una receta
    RecipeStep updateRecipeStep(Long id, RecipeStepDTO dto) throws Exception;

}
