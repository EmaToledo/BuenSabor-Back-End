package com.elbuensabor.api.repository;

import com.elbuensabor.api.dto.DenominationXImageDto;
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

    @Query("SELECT p.id, p.denomination, i.name, p.productCategory.id " +
            "FROM Product p " +
            "LEFT JOIN Image i ON i.idProduct = p " +
            "WHERE p.availability = true")
    List<Object[]> findAvailableProductsXImage();


    @Query("SELECT p FROM Product p WHERE p.productCategory.id = :idCategory")
    List<Product> findProductByCategory(Long idCategory);
}
