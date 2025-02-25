package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.Ingredient;
import com.elbuensabor.api.entity.Stock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IStockRepository extends IGenericRepository<Stock, Long> {
    @Query("SELECT s FROM Stock s WHERE s.ingredientStock.id = :ingredientID")
    Stock findIngredientStock(Long ingredientID);

    @Query("SELECT s FROM Stock s WHERE s.productStock.id = :productID")
    Stock findProductStock(Long productID);

    @Query("SELECT s FROM Stock s INNER JOIN Ingredient i ON s.ingredientStock.id = i.id WHERE i.ingredientCategory.id = :categoryID")
    List<Stock> findStockByIngredientCategory(Long categoryID);

    @Query("SELECT s FROM Stock s INNER JOIN Product p ON s.productStock.id = p.id WHERE p.productCategory.id = :categoryID")
    List<Stock> findStockByProductCategory(Long categoryID);
}
