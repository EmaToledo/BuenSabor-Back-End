package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.IngredientDTO;
import com.elbuensabor.api.entity.Ingredient;

public interface IngredientService extends GenericService<Ingredient, IngredientDTO, Long> {

    // Guarda un ingrediente
    Ingredient saveIngredient(IngredientDTO dto) throws Exception;

    // Actualiza un ingrediente
    Ingredient updateIngredient(Long id, IngredientDTO dto) throws Exception;

    // Bloquea un ingrediente
    Ingredient blockIngredient(Long id) throws Exception;

    // Desbloquea un ingrediente
    Ingredient unlockIngredient(Long id) throws Exception;

}
