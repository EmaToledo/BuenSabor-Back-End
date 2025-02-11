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

    @Query("SELECT c.id FROM Category c WHERE c.fatherCategory.id = :parentId")
    List<Long> findIdsByParentCategoryId(Long parentId);

    @Query("SELECT c FROM Category c WHERE c.type = :typeCategory AND c.availability = true AND c.fatherCategory.id = :idCategory")
    List<Category> findRelationCategoriesByIdAndType(String type, Long idCategory);

    // Busca las categorías de productos
    @Query("SELECT c FROM Category c WHERE c.type = 'P' AND c.availability = true AND c.fatherCategory IS NOT NULL")
    List<Category> findProductCategories();

    // Busca las categorías de ingredientes
    @Query("SELECT c FROM Category c WHERE c.type = 'I' AND c.availability = true AND c.fatherCategory IS NOT NULL")
    List<Category> findIngredientCategories();

    // Busca las categorías de productos manufacturados
    @Query("SELECT c FROM Category c WHERE c.type = 'M' AND c.availability = true AND c.fatherCategory IS NOT NULL")
    List<Category> findManufacturedProductCategories();

    // Busca las categorías generales
    @Query(value = "SELECT * FROM category WHERE type = 'G' AND availability = true", nativeQuery = true)
    List<Category> findGeneralCategories();

    // Busca las categorías de productos y manufacturados con disponibilidad y sin categoría padre
    @Query("SELECT c FROM Category c WHERE (c.type = 'P' OR c.type = 'M') AND c.availability = true AND c.fatherCategory IS NULL")
    List<Category> findAvailableProductAndManufacturedCategoriesWithoutFather();

    @Query("SELECT c.id FROM Category c WHERE (c.type = 'P') AND c.availability = true AND c.fatherCategory IS NULL")
    List<Long> findAvailableProductCategoriesIdsWithoutFather();

    @Query("SELECT c.id FROM Category c WHERE (c.type = 'M') AND c.availability = true AND c.fatherCategory IS NULL")
    List<Long> findAvailableManufacturedCategoriesIdsWithoutFather();




}
