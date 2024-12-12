package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.dto.OrderDTO;
import com.elbuensabor.api.dto.RecipeStepDTO;
import com.elbuensabor.api.entity.Recipe;
import com.elbuensabor.api.entity.RecipeStep;
import com.elbuensabor.api.mapper.GenericMapper;
import com.elbuensabor.api.mapper.RecipeStepMapper;
import com.elbuensabor.api.repository.IRecipeRepository;
import com.elbuensabor.api.repository.IRecipeStepRepository;
import com.elbuensabor.api.service.RecipeStepService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeStepServiceImpl extends GenericServiceImpl<RecipeStep, RecipeStepDTO, Long> implements RecipeStepService {

    @Autowired
    private IRecipeStepRepository recipeStepRepository;

    @Autowired
    private IRecipeRepository recipeRepository;

    private final RecipeStepMapper recipeStepMapper = RecipeStepMapper.getInstance();

    // Constructor
    public RecipeStepServiceImpl(com.elbuensabor.api.repository.IGenericRepository<RecipeStep, Long> IGenericRepository, GenericMapper<RecipeStep, RecipeStepDTO> genericMapper, IRecipeStepRepository recipeStepRepository, IRecipeRepository recipeRepository) {
        super(IGenericRepository, genericMapper);
    }

    /**
     * Guarda un paso de receta.
     * URL: http://localhost:4000/api/recipes/steps/save
     *
     * @param dto Objeto RecipeStepDTO que representa el paso de receta a guardar.
     * @return ResponseEntity con el paso de receta guardado en el cuerpo de la respuesta.
     * HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @Override
    @Transactional
    public RecipeStep saveRecipeStep(RecipeStepDTO dto) throws Exception {
        try {
            RecipeStep recipeStep = recipeStepMapper.toEntity(dto);

            setRecipeIfExist(dto.getRecipeId(), recipeStep);
            recipeStep.setDescription(dto.getDescription());

            return recipeStepRepository.save(recipeStep);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Actualiza un paso de receta por su ID.
     * URL: http://localhost:4000/api/recipes/steps/update/{id}
     *
     * @param id  ID del paso de receta a actualizar.
     * @param dto Objeto RecipeStepDTO que contiene los datos actualizados del paso de receta.
     * @return ResponseEntity con el paso de receta actualizado en el cuerpo de la respuesta.
     * HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @Override
    @Transactional
    public RecipeStep updateRecipeStep(Long id, RecipeStepDTO dto) throws Exception {
        try {
            RecipeStep recipeStep = recipeStepRepository.findById(id)
                    .orElseThrow(() -> new Exception("El paso de la receta a actualizar no existe"));

            setRecipeIfExist(dto.getRecipeId(), recipeStep);

            recipeStep.setDescription(dto.getDescription());

            return recipeStepRepository.save(recipeStep);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void setRecipeIfExist(Long recipeId, RecipeStep recipeStep) throws Exception {
        if (recipeId != null) {
            Recipe recipe = recipeRepository.findById(recipeId)
                    .orElseThrow(() -> new Exception("La receta padre del paso de la receta no existe"));

            recipeStep.setRecipe(recipe);
        } else {
            recipeStep.setRecipe(null);
        }
    }


    @Override
    public OrderDTO updateOrderState(Long id, String newState) throws Exception {
        return null;
    }
}
