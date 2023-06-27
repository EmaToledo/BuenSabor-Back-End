package com.example.api.service.impl;

import com.example.api.dtos.CategoryDTO;
import com.example.api.entity.Category;
import com.example.api.mapper.CategoryMapper;
import com.example.api.mapper.GenericMapper;
import com.example.api.repository.ICategoryRepository;
import com.example.api.repository.IGenericRepository;
import com.example.api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl extends GenericServiceImpl<Category, CategoryDTO, Long> implements CategoryService {

    @Autowired
    private ICategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper = CategoryMapper.getInstance();

    public CategoryServiceImpl(IGenericRepository<Category, Long> IGenericRepository, GenericMapper<Category, CategoryDTO> genericMapper) {
        super(IGenericRepository, genericMapper);
    }

    @Override
    @Transactional
    public Category saveCategory(CategoryDTO dto) throws Exception {
        try {
            Category category = categoryMapper.toEntity(dto);

            if (dto.getCategoryFatherId() != null) {
                if (categoryRepository.existsById(dto.getCategoryFatherId())) {
                    Category fatherCategory = categoryRepository.findById(dto.getCategoryFatherId()).get();
                    category.setFatherCategory(fatherCategory);
                } else {
                    throw new Exception("La categoría padre no existe");
                }
            }

            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }


    }

    @Override
    @Transactional
    public Category updateCategory(Long id, CategoryDTO dto) throws Exception {
        try {
            Optional<Category> optionalCategory = categoryRepository.findById(id);

            if (optionalCategory.isEmpty()) {
                throw new Exception("La categoria a actualizar no existe.");
            }

            Category category = optionalCategory.get();

            if (dto.getCategoryFatherId() != null) {
                if (categoryRepository.existsById(dto.getCategoryFatherId())) {
                    Category categoryFather = categoryRepository.findById(dto.getCategoryFatherId()).get();
                    category.setFatherCategory(categoryFather);
                } else {
                    throw new Exception("La categoria padre no existe");
                }
            } else {
                category.setFatherCategory(null);
            }
            category.setDenomination(dto.getDenomination());
            category.setAvailability(dto.getAvailability());
            category.setType(dto.getType());

            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Category blockUnlockCategory(Long id, boolean availability) throws Exception {
        try {
            Category category = categoryRepository.findById(id).orElseThrow(() -> new Exception("Category not found"));
            category.setAvailability(availability);
            return categoryRepository.save(category);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<CategoryDTO> findUnlockedCategories() throws Exception {
        try {
            List<Category> categories = categoryRepository.findUnlockedCategories();
            return genericMapper.toDTOsList(categories);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<CategoryDTO> findAllProductCategories() throws Exception {
        try {
            List<Category> categories = categoryRepository.findAllProductCategories();
            return genericMapper.toDTOsList(categories);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<CategoryDTO> findProductCategories() throws Exception {
        try {
            List<Category> categories = categoryRepository.findProductCategories();
            for (Category c :
                    categories) {
                System.out.println("2 = "+c.getDenomination());
            }
            return genericMapper.toDTOsList(categories);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<CategoryDTO> findManufacturedProductCategories() throws Exception {
        try {
            List<Category> categories = categoryRepository.findManufacturedProductCategories();
            return genericMapper.toDTOsList(categories);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<CategoryDTO> findIngredientCategories() throws Exception {
        try {
            List<Category> categories = categoryRepository.findIngredientCategories();
            return genericMapper.toDTOsList(categories);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}