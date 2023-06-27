package com.example.api.service;

import com.example.api.dtos.IngredientDTO;
import com.example.api.entity.Ingredient;

public interface IngredientService extends GenericService<Ingredient, IngredientDTO, Long> {

    Ingredient saveIngredient(IngredientDTO dto) throws Exception;

    Ingredient updateIngredient(Long id, IngredientDTO dto) throws Exception;

    Ingredient blockUnlockIngredient(Long id, boolean blocked) throws Exception;

}
