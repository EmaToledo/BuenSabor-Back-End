package com.example.api.repository;

import com.example.api.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends IGenericRepository<Product, Long> {
    @Query(value = "SELECT * FROM product WHERE cooking_time != '00:00:00'", nativeQuery = true)
    List<Product> findManufacturedProducts();

    @Query(value = "SELECT * FROM product WHERE cooking_time = '00:00:00'", nativeQuery = true)
    List<Product> findPrePackagedProducts();
}
