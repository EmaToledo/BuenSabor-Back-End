package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.dto.CategoryDTO;
import com.elbuensabor.api.entity.Category;
import com.elbuensabor.api.mapper.CategoryMapper;
import com.elbuensabor.api.mapper.GenericMapper;
import com.elbuensabor.api.repository.ICategoryRepository;
import com.elbuensabor.api.repository.IGenericRepository;
import com.elbuensabor.api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl extends GenericServiceImpl<Category, CategoryDTO, Long> implements CategoryService {

    @Autowired
    private ICategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper = CategoryMapper.getInstance();

    private static final char CATEGORY_TYPE_INGREDIENT = 'I';
    private static final char CATEGORY_TYPE_PRODUCT = 'P';
    private static final char CATEGORY_TYPE_MANUFACTURED_PRODUCT = 'M';
    private static final char CATEGORY_TYPE_GENERAL = 'G';

    public CategoryServiceImpl(IGenericRepository<Category, Long> IGenericRepository, GenericMapper<Category, CategoryDTO> genericMapper) {
        super(IGenericRepository, genericMapper);
    }

    /**
     * Guarda una categoría en la base de datos.
     *
     * @param dto DTO de la categoría a guardar
     * @return La categoría guardada
     * @throws Exception Si ocurre algún error durante el proceso de guardado
     */
    @Override
    @Transactional
    public Category saveCategory(CategoryDTO dto) throws Exception {
        Category category = categoryMapper.toEntity(dto);

        setCategoryFatherIfExists(dto.getCategoryFatherId(), category);
        verifCategoryType(dto, category);

        return categoryRepository.save(category);
    }

    /**
     * Actualiza una categoría existente en la base de datos.
     *
     * @param id  ID de la categoría a actualizar
     * @param dto DTO con los nuevos datos de la categoría
     * @return La categoría actualizada
     * @throws Exception Si la categoría no existe o si ocurre algún error durante la actualización
     */
    @Override
    @Transactional
    public Category updateCategory(Long id, CategoryDTO dto) throws Exception {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new Exception("La categoría a actualizar no existe."));

            setCategoryFatherIfExists(dto.getCategoryFatherId(), category);
            verifCategoryType(dto, category);

            category.setDenomination(dto.getDenomination());
            category.setAvailability(dto.getAvailability());

            return categoryRepository.save(category);
    }

    /**
     * Asigna la categoría padre si existe en la base de datos, o la establece como null si no se proporciona una categoría.
     *
     * @param categoryId ID de la categoría del producto.
     * @param category   Categoria a la cual se le asignará la categoría padre.
     * @throws Exception si la categoría padre no existe en la base de datos.
     */
    private void setCategoryFatherIfExists(Long categoryId, Category category) throws Exception {
        if (categoryId != null) {
            Category categoryFather = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new Exception("La categoria padre no existe"));
            category.setFatherCategory(categoryFather);
        } else {
            category.setFatherCategory(null);
        }
    }

    /**
     * Verifica y asigna el tipo de categoría si es uno de los tipos válidos.
     *
     * @param dto      DTO con los datos de la categoría.
     * @param category Categoría a la cual se le asignará el tipo.
     * @throws Exception si el tipo de categoría proporcionado no es uno de los tipos válidos.
     */
    private void verifCategoryType(CategoryDTO dto, Category category) throws Exception {
        if (dto.getType() == CATEGORY_TYPE_INGREDIENT || dto.getType() == CATEGORY_TYPE_PRODUCT || dto.getType() == CATEGORY_TYPE_MANUFACTURED_PRODUCT || dto.getType() == CATEGORY_TYPE_GENERAL) {
            category.setType(dto.getType());
        } else {
            throw new Exception(dto.getType() + " - No es un tipo válido de categoría");
        }
    }

    /**
     * Bloquea una categoría y todas sus subcategorías.
     *
     * @param id ID de la categoría a bloquear
     * @return La categoría bloqueada
     * @throws Exception Si la categoría no existe o si ocurre algún error durante el bloqueo
     */
    @Override
    @Transactional
    public Category blockCategory(Long id) throws Exception {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new Exception("Category not found"));

            category.setAvailability(false);
            category = categoryRepository.save(category);

            List<Category> childCategories = categoryRepository.findByParentCategoryId(id);
            blockChildCategories(childCategories);

            return category;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void blockChildCategories(List<Category> categories) {
        for (Category childCategory : categories) {
            childCategory.setAvailability(false);
            categoryRepository.save(childCategory);

            List<Category> grandchildren = categoryRepository.findByParentCategoryId(childCategory.getId());
            blockChildCategories(grandchildren);
        }
    }

    /**
     * Desbloquea una categoría.
     *
     * @param id ID de la categoría a desbloquear
     * @return La categoría desbloqueada
     * @throws Exception Si la categoría no existe o si ocurre algún error durante el desbloqueo
     */
    @Override
    @Transactional
    public Category unlockCategory(Long id) throws Exception {
        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new Exception("Category not found"));
            category.setAvailability(true);
            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Obtiene las categorías filtradas por tipo.
     *
     * @param categoryType Tipo de categoría (I, P, M, G)
     * @return Lista de categorías filtradas por tipo
     */
    public List<CategoryDTO> findCategoriesByType(char categoryType) {
        List<Category> categories;

        switch (categoryType) {
            case CATEGORY_TYPE_INGREDIENT:
                categories = categoryRepository.findIngredientCategories();
                break;
            case CATEGORY_TYPE_PRODUCT:
                categories = categoryRepository.findProductCategories();
                break;
            case CATEGORY_TYPE_MANUFACTURED_PRODUCT:
                categories = categoryRepository.findManufacturedProductCategories();
                break;
            case CATEGORY_TYPE_GENERAL:
                categories = categoryRepository.findGeneralCategories();
                break;
            default:
                throw new IllegalArgumentException("Tipo de categoría inválido: " + categoryType);
        }

        return genericMapper.toDTOsList(categories);
    }

    /**
     * Obtiene las categorías desbloqueadas filtradas por tipo.
     *
     * @param categoryType Tipo de categoría (I, P, M, G)
     * @return Lista de categorías desbloqueadas filtradas por tipo
     * @throws Exception Si ocurre algún error al obtener las categorías
     */
    public List<CategoryDTO> findUnlockedCategoriesByType(String categoryType) throws Exception {
        try {
            char type = categoryType.charAt(0);
            List<CategoryDTO> categories = findCategoriesByType(type);

            return categories.stream()
                    .filter(CategoryDTO::getAvailability)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception("Error al obtener las categorías desbloqueadas por tipo: " + e.getMessage());
        }
    }

    /**
     * Obtiene las categorías desbloqueadas filtradas por tipo, excluyendo una categoría específica.
     *
     * @param id ID de la categoría a excluir
     * @return Lista de categorías desbloqueadas filtradas por tipo, excluyendo la categoría con el ID dado
     * @throws Exception Si la categoría no existe o si ocurre algún error al obtener las categorías
     */
    public List<CategoryDTO> findUnlockedCategoriesByTypeExceptId(Long id) throws Exception {
        Optional<Category> categoryFilter = categoryRepository.findById(id);

        if (!categoryFilter.isPresent()) {
            throw new Exception("Category not found");
        }

        char categoryType = categoryFilter.get().getType();
        List<CategoryDTO> categoriesByTypeExceptId = findUnlockedCategoriesByType(String.valueOf(categoryType));

        return categoriesByTypeExceptId.stream()
                .filter(category -> !category.getId().equals(id))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene las categorías de ingredientes.
     *
     * @return Lista de categorías de ingredientes
     * @throws Exception Si ocurre algún error al obtener las categorías
     */
    public List<CategoryDTO> findIngredientCategories() throws Exception {
        try {
            return findCategoriesByType(CATEGORY_TYPE_INGREDIENT);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Obtiene las categorías de productos.
     *
     * @return Lista de categorías de productos
     * @throws Exception Si ocurre algún error al obtener las categorías
     */
    public List<CategoryDTO> findProductCategories() throws Exception {
        try {
            return findCategoriesByType(CATEGORY_TYPE_PRODUCT);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Obtiene las categorías de productos manufacturados.
     *
     * @return Lista de categorías de productos manufacturados
     * @throws Exception Si ocurre algún error al obtener las categorías
     */
    public List<CategoryDTO> findManufacturedProductCategories() throws Exception {
        try {
            return findCategoriesByType(CATEGORY_TYPE_MANUFACTURED_PRODUCT);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Obtiene las categorías generales.
     *
     * @return Lista de categorías generales
     * @throws Exception Si ocurre algún error al obtener las categorías
     */
    public List<CategoryDTO> findGeneralCategories() throws Exception {
        try {
            return findCategoriesByType(CATEGORY_TYPE_GENERAL);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
