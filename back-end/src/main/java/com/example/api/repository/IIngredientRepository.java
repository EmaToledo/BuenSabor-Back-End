package com.example.api.repository;

import com.example.api.entity.Ingredient;
import org.springframework.stereotype.Repository;

@Repository
public interface IIngredientRepository extends IGenericRepository<Ingredient, Long> {

}
