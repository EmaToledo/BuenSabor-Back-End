package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.RecipeStep;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRecipeStepRepository extends IGenericRepository<RecipeStep, Long> {
    @Query("SELECT rs FROM RecipeStep rs WHERE rs.recipe.id = :recipeId")
    List<RecipeStep> findStepsByRecipeId(Long recipeId);

}

