package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.Recipe;
import com.elbuensabor.api.entity.RecipeStep;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRecipeRepository extends IGenericRepository<Recipe, Long> {
    @Query("SELECT rs FROM RecipeStep rs WHERE rs.recipe.id = :recipeId ORDER BY rs.stepNumber")
    List<RecipeStep> findStepsByRecipeIdByOrder(Long recipeId);
}
