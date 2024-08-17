package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.Ingredient;
import com.elbuensabor.api.entity.IngredientManufacturedLink;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IIngredientManufacturedLinkRepository extends IGenericRepository<IngredientManufacturedLink, Long> {

    // llama los ingredientes que tienen relacion con un id especifico de producto manufacturado
    @Query("SELECT i FROM IngredientManufacturedLink iml " +
            "JOIN iml.ingredient i " +
            "WHERE iml.manufacturedProduct.id = :idManufacturedProduct")
    List<Ingredient> findIngredientsByManufacturedProductId(@Param("idManufacturedProduct") Long idManufacturedProduct);

    // llama a la relacion mediante el id del ingrediente
    @Query("SELECT i FROM IngredientManufacturedLink iml " +
            "JOIN iml.ingredient i " +
            "WHERE iml.ingredient.id = :idIngredient")
    IngredientManufacturedLink findIngredientsByIngredientId(@Param("idIngredient") Long idIngredient);

    // modifica el query de delete para que elimine una relacion por el id del ingrediente
    @Modifying
    @Query("DELETE FROM IngredientManufacturedLink iml WHERE iml.ingredient.id = :idIngredient")
    void deleteByIngredientId(@Param("idIngredient") Long idIngredient);

}
