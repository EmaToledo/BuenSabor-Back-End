package com.example.api.repository;

import com.example.api.entity.ManufacturedProduct;
import org.springframework.stereotype.Repository;

@Repository
public interface IManufacturedProductRepository extends IGenericRepository<ManufacturedProduct, Long> {

}
