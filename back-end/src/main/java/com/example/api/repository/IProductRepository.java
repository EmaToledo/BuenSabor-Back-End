package com.example.api.repository;

import com.example.api.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository  extends IGenericRepository<Product, Long> {

}
