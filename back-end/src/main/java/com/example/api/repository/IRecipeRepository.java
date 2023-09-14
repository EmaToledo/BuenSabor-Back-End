package com.example.api.repository;

import com.example.api.entity.Recipe;
import org.springframework.stereotype.Repository;

@Repository
public interface IRecipeRepository extends IGenericRepository<Recipe, Long> {

}
