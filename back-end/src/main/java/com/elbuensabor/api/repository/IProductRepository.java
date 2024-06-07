package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository  extends IGenericRepository<Product, Long> {
    @Query("SELECT MAX(id) FROM Product")
    Long findLastProductId();

    @Query("SELECT p FROM Product p WHERE p.availability = true")
    List<Product> findAvailableProducts();
}
