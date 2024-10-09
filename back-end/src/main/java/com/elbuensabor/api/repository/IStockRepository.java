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
}
