package com.example.api.service.impl;

import com.example.api.dtos.IngredientDTO;
import com.example.api.entity.Category;
import com.example.api.entity.Ingredient;
import com.example.api.mapper.GenericMapper;
import com.example.api.mapper.IngredientMapper;
import com.example.api.repository.IIngredientRepository;
import com.example.api.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class IngredientServiceImpl  extends GenericServiceImpl<Ingredient, IngredientDTO, Long> implements IngredientService {

    @Autowired
    private IIngredientRepository ingredientRepository;

    private final IngredientMapper ingredientMapper = IngredientMapper.getInstance();

    public IngredientServiceImpl(com.example.api.repository.IGenericRepository<Ingredient, Long> IGenericRepository, GenericMapper<Ingredient, IngredientDTO> genericMapper) {
        super(IGenericRepository, genericMapper);
    }

    @Override
    @Transactional
    public Ingredient saveIngredient(IngredientDTO dto) throws Exception {
        try {
            Ingredient ingredient = ingredientMapper.toEntity(dto);

            if (dto.getIngredientCategoryID() != null) {
                if (ingredientRepository.existsById(dto.getIngredientCategoryID())) {
                    Category ingredientCategory = ingredientRepository.findById(dto.getIngredientCategoryID()).get().getIngredientCategory();
                    ingredient.setIngredientCategory(ingredientCategory);
                } else {
                    throw new Exception("La categoría del ingrediente no existe");
                }
            }

            return ingredientRepository.save(ingredient);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }


    }

    @Override
    @Transactional
    public Ingredient updateIngredient(Long id, IngredientDTO dto) throws Exception {
        try {
            Optional<Ingredient> optionalIngredient = ingredientRepository.findById(id);

            if (optionalIngredient.isEmpty()) {
                throw new Exception("El ingrediente a actualizar no existe.");
            }

            Ingredient ingredient = optionalIngredient.get();

            if (dto.getIngredientCategoryID() != null) {
                if (ingredientRepository.existsById(dto.getIngredientCategoryID())) {
                    Category ingredientCategory = ingredientRepository.findById(dto.getIngredientCategoryID()).get().getIngredientCategory();
                    ingredient.setIngredientCategory(ingredientCategory);
                } else {
                    throw new Exception("La categoria ingrediente no existe");
                }
            } else {
                ingredient.setIngredientCategory(null);
            }
            ingredient.setDenomination(dto.getDenomination());
            ingredient.setMinStock(dto.getMinStock());
            ingredient.setActualStock(dto.getActualStock());
            ingredient.setAvailability(dto.getAvailability());
            ingredient.setUnit(dto.getUnit());

            return ingredientRepository.save(ingredient);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Ingredient blockUnlockIngredient(Long id, boolean availability) throws Exception {
        try {
            Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(() -> new Exception("Ingredient not found"));
            ingredient.setAvailability(availability);
            return ingredientRepository.save(ingredient);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
