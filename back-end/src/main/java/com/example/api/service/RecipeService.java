package com.example.api.service;

import com.example.api.dtos.RecipeDTO;
import com.example.api.entity.Recipe;

public interface RecipeService extends GenericService<Recipe, RecipeDTO, Long> {

    // Otiene una receta con todos sus pasos y sus respectivos detalles
    RecipeDTO getCompleteRecipe(Long recipeId) throws Exception;

    // Guarda una receta
    RecipeDTO saveRecipe(RecipeDTO dto) throws Exception;

    // Actualiza una receta
    RecipeDTO updateRecipe(Long id, RecipeDTO dto) throws Exception;

}
