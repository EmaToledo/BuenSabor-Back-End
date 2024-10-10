package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.IngredientRecipeLink;
import com.elbuensabor.api.entity.ManufacturedProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IManufacturedProductRepository extends IGenericRepository<ManufacturedProduct, Long> {

    @Query("SELECT MAX(id) FROM ManufacturedProduct")
    Long findLastManufacturedProductId();

    @Query("SELECT m FROM ManufacturedProduct m WHERE m.availability = true")
    List<ManufacturedProduct> findAvailableManufacturedProducts();

    @Query("SELECT il FROM Recipe r JOIN IngredientRecipeLink il ON r.id = il.recipe.id WHERE r.manufacturedProduct = :manufacturedProductId")
    List<IngredientRecipeLink> findManufacturedProductIngredients(Long manufacturedProductId);

}