package com.elbuensabor.api.repository;

import com.elbuensabor.api.dto.IngredientRecipeLinkDTO;
import com.elbuensabor.api.entity.Ingredient;
import com.elbuensabor.api.entity.IngredientRecipeLink;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IIngredientRecipeLinkRepository extends IGenericRepository<IngredientRecipeLink, Long> {

    // llama los ingredientes que tienen relacion con un id especifico de receta
    @Query("SELECT iml FROM IngredientRecipeLink iml " +
            "WHERE iml.recipe.id = :idRecipe")
    List<IngredientRecipeLink> findIngredientsByRecipeId(@Param("idRecipe") Long idRecipe);

    // llama a la relacion mediante el id del ingrediente
//    @Query("SELECT i FROM IngredientRecipeLink iml " +
//            "JOIN iml.ingredient i " +
//            "WHERE iml.ingredient.id = :idIngredient")
    @Query("SELECT iml FROM IngredientRecipeLink iml " +
            "JOIN iml.ingredient i " +
            "WHERE iml.ingredient.id = :idIngredient")
    List<IngredientRecipeLink> findIngredientsByIngredientId(@Param("idIngredient") Long idIngredient);

    // modifica el query de delete para que elimine una relacion por el id del ingrediente
    @Modifying
    @Query("DELETE FROM IngredientRecipeLink iml WHERE iml.ingredient.id = :idIngredient")
    void deleteByIngredientId(@Param("idIngredient") Long idIngredient);

}
