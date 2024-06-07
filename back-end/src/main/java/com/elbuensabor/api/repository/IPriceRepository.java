package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.Price;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IPriceRepository extends IGenericRepository<Price, Long>{

    @Query("SELECT p FROM Price p WHERE p.manufacturedProduct.id = :idManufacturedProduct AND p.costPriceDate = (SELECT MAX(p2.costPriceDate) FROM Price p2 WHERE p2.manufacturedProduct.id = :idManufacturedProduct)")
    Price findPriceIdByIdManufacturedProduct(Long idManufacturedProduct);

    @Query("SELECT p FROM Price p WHERE p.product.id = :idProduct AND p.costPriceDate = (SELECT MAX(p2.costPriceDate) FROM Price p2 WHERE p2.product.id = :idProduct)")
    Price findPriceIdByIdProduct(Long idProduct);





}


