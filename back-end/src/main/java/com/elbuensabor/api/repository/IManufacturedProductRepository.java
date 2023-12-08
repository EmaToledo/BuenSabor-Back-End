package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.ManufacturedProduct;
import org.springframework.stereotype.Repository;

@Repository
public interface IManufacturedProductRepository extends IGenericRepository<ManufacturedProduct, Long> {

}
