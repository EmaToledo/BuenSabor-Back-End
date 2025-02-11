package com.elbuensabor.api.repository;

import com.elbuensabor.api.dto.DenominationXImageDto;
import com.elbuensabor.api.entity.IngredientRecipeLink;
import com.elbuensabor.api.entity.ManufacturedProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IManufacturedProductRepository extends IGenericRepository<ManufacturedProduct, Long> {

    @Query("SELECT MAX(id) FROM ManufacturedProduct")
    Long findLastManufacturedProductId();

    @Query("SELECT m FROM ManufacturedProduct m WHERE m.availability = true")
    List<ManufacturedProduct> findAvailableManufacturedProducts();

    @Query("SELECT m.id, m.denomination, i.name, m.manufacturedProductCategory.id " +
            "FROM ManufacturedProduct m " +
            "LEFT JOIN Image i ON i.idManufacturedProduct = m " +
            "WHERE m.availability = true")
    List<Object[]> findAvailableManufacturedProductsXImage();
    @Query("SELECT il FROM Recipe r JOIN IngredientRecipeLink il ON r.id = il.recipe.id WHERE r.manufacturedProduct.id = :manufacturedProductId")
    List<IngredientRecipeLink> findManufacturedProductIngredients(Long manufacturedProductId);

    @Query("SELECT m FROM ManufacturedProduct m WHERE m.manufacturedProductCategory.id = :idCategory")
    List<ManufacturedProduct> findManufacturedProductByCategory(Long idCategory);
}