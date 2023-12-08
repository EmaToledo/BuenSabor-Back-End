package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository  extends IGenericRepository<Product, Long> {

}
