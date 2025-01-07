package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.Ingredient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IIngredientRepository extends IGenericRepository<Ingredient, Long> {
    @Query("SELECT MAX(id) FROM Ingredient")
    Long findLastIngredientId();
}
