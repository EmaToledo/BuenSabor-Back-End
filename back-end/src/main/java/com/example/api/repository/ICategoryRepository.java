package com.example.api.repository;

import com.example.api.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ICategoryRepository extends IGenericRepository<Category, Long> {
    @Query(value = "SELECT * FROM category WHERE availability = true", nativeQuery = true)
    List<Category> findUnlockedCategories();

    @Query(value = "SELECT * FROM category WHERE availability = true AND id != :id", nativeQuery = true)
    List<Category> findUnlockedCategoriesExceptId(@Param("id") Long id);

    @Query(value = "SELECT * FROM category WHERE type = true", nativeQuery = true)
    List<Category> findAllProductCategories();

    @Query(value = "SELECT DISTINCT c.* FROM category c INNER JOIN product p ON c.id_category = p.id_category WHERE c.type = true AND p.cooking_time = '00:00:00'", nativeQuery = true)
    List<Category> findProductCategories();

    @Query(value = "SELECT DISTINCT c.* FROM category c INNER JOIN product p ON c.id_category = p.id_category WHERE c.type = true AND p.cooking_time != '00:00:00'", nativeQuery = true)
    List<Category> findManufacturedProductCategories();

    @Query(value = "SELECT * FROM category WHERE type = false", nativeQuery = true)
    List<Category> findIngredientCategories();

}
