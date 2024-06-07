package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends IGenericRepository<Category, Long> {

    // Busca los hjos de una categoría
    @Query("SELECT c FROM Category c WHERE c.fatherCategory.id = :parentId")
    List<Category> findByParentCategoryId(Long parentId);

    // Busca las categorías de productos
    @Query(value = "SELECT * FROM category WHERE type = 'P' AND availability = true", nativeQuery = true)
    List<Category> findProductCategories();

    // Busca las categorías de ingredientes
    @Query(value = "SELECT * FROM category WHERE type = 'I' AND availability = true", nativeQuery = true)
    List<Category> findIngredientCategories();

    // Busca las categorías de productos manufacturados
    @Query(value = "SELECT * FROM category WHERE type = 'M' AND availability = true", nativeQuery = true)
    List<Category> findManufacturedProductCategories();

    // Busca las categorías generales
    @Query(value = "SELECT * FROM category WHERE type = 'G' AND availability = true", nativeQuery = true)
    List<Category> findGeneralCategories();

    // Busca las categorías de productos y manufacturados con disponibilidad y sin categoría padre
    @Query("SELECT c FROM Category c WHERE (c.type = 'P' OR c.type = 'M') AND c.availability = true AND c.fatherCategory IS NULL")
    List<Category> findAvailableProductAndManufacturedCategoriesWithoutFather();




}
