package com.example.api.service;

import com.example.api.dtos.CategoryDTO;
import com.example.api.entity.Category;

import java.util.List;

public interface CategoryService extends GenericService<Category, CategoryDTO, Long> {
    Category saveCategory(CategoryDTO dto) throws Exception;

    Category updateCategory(Long id, CategoryDTO dto) throws Exception;

    Category blockUnlockCategory(Long id, boolean blocked) throws Exception;

    List<CategoryDTO> findUnlockedCategories() throws Exception;

    List<CategoryDTO> findAllProductCategories() throws Exception;

    List<CategoryDTO> findProductCategories() throws Exception;

    List<CategoryDTO> findManufacturedProductCategories() throws Exception;

    List<CategoryDTO> findIngredientCategories() throws Exception;
}
