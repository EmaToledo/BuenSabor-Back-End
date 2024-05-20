package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository  extends IGenericRepository<Product, Long> {
    @Query("SELECT MAX(id) FROM Product")
    Long findLastProductId();
}
