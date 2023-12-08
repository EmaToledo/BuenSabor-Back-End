package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.Ingredient;
import org.springframework.stereotype.Repository;

@Repository
public interface IIngredientRepository extends IGenericRepository<Ingredient, Long> {

}
