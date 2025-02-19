package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.CategoryChildsDTO;
import com.elbuensabor.api.dto.CategoryDTO;
import com.elbuensabor.api.entity.Category;

import java.util.List;

public interface CategoryService extends GenericService<Category, CategoryDTO, Long> {

    // Guarda una categoría
    Category saveCategory(CategoryDTO dto) throws Exception;

    // Actualiza una categoría
    Category updateCategory(Long id, CategoryDTO dto) throws Exception;

    // Bloquea una categoría
    Category blockCategory(Long id) throws Exception;

    // Desbloquea una categoría
    Category unlockCategory(Long id) throws Exception;

    // Obtiene todas las categorías de un tipo específico
    List<CategoryDTO> findCategoriesByType(char categoryType) throws Exception;

    // Obtiene todas las categorías desbloqueadas de un tipo específico
    List<CategoryDTO> findUnlockedCategoriesByType(String categoryType) throws Exception;

    // Obtiene todas las categorías desbloqueadas excepto una categoría específica
    List<CategoryDTO> findUnlockedCategoriesByTypeExceptId(Long id) throws Exception;

    // Obtiene todas las categorías de ingredientes
    List<CategoryDTO> findIngredientCategories() throws Exception;

    // Obtiene todas las categorías de productos
    List<CategoryDTO> findProductCategories() throws Exception;

    // Obtiene todas las categorías de productos manufacturados
    List<CategoryDTO> findManufacturedProductCategories() throws Exception;

    // Obtiene todas las categorías generales
    List<CategoryDTO> findGeneralCategories() throws Exception;

    // Obtiene todas las categorías generales
    List<CategoryChildsDTO> findProductAndManufacturedProductCategories() throws Exception;

}
