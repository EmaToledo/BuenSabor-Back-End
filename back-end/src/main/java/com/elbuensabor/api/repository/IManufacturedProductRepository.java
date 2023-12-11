package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.ManufacturedProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IManufacturedProductRepository extends IGenericRepository<ManufacturedProduct, Long> {

    @Query("SELECT MAX(id) FROM ManufacturedProduct")
    Long findLastManufacturedProductId();
    
}