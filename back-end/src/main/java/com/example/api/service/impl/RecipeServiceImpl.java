package com.example.api.service.impl;

import com.example.api.dtos.RecipeDTO;
import com.example.api.dtos.RecipeStepDTO;
import com.example.api.entity.ManufacturedProduct;
import com.example.api.entity.Recipe;
import com.example.api.entity.RecipeStep;
import com.example.api.mapper.GenericMapper;
import com.example.api.mapper.RecipeMapper;
import com.example.api.mapper.RecipeStepMapper;
import com.example.api.repository.*;
import com.example.api.service.RecipeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeServiceImpl extends GenericServiceImpl<Recipe, RecipeDTO, Long> implements RecipeService {

    @Autowired
    private IRecipeRepository recipeRepository;

    @Autowired
    private IRecipeStepRepository recipeStepRepository;

    @Autowired
    private IManufacturedProductRepository manufacturedProductRepository;

    private final RecipeMapper recipeMapper = RecipeMapper.getInstance();
    private final RecipeStepMapper recipeStepMapper = RecipeStepMapper.getInstance();

    // Constructor
    public RecipeServiceImpl(com.example.api.repository.IGenericRepository<Recipe, Long> IGenericRepository, GenericMapper<Recipe, RecipeDTO> genericMapper) {
        super(IGenericRepository, genericMapper);
    }

    /**
     * Obtiene una receta completa por su ID.
     * URL: http://localhost:4000/api/recipes/complete/{recipeId}
     *
     * @param recipeId ID de la receta a recuperar.
     * @return ResponseEntity con la receta completa en el cuerpo de la respuesta.
     * HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @Override
    @Transactional
    public RecipeDTO getCompleteRecipe(Long recipeId) throws Exception {
        try {
            Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new Exception("La receta no existe"));

            List<RecipeStep> steps = recipeStepRepository.findStepsByRecipeId(recipe.getId());

            List<RecipeStepDTO> stepDTOs = new ArrayList<>();

            for (RecipeStep step : steps) {
                RecipeStepDTO stepDTO = RecipeStepMapper.getInstance().toDTO(step);
                stepDTOs.add(stepDTO);
            }

            RecipeDTO recipeDTO = RecipeMapper.getInstance().toDTO(recipe);
            recipeDTO.setSteps(stepDTOs);

            return recipeDTO;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Guarda una receta.
     * URL: http://localhost:4000/api/recipes/save
     *
     * @param dto Objeto RecipeDTO que representa la receta a guardar.
     * @return ResponseEntity con la receta guardada en el cuerpo de la respuesta.
     * HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @Override
    @Transactional
    public RecipeDTO saveRecipe(RecipeDTO dto) throws Exception {
        try {
            Recipe recipe = recipeMapper.toEntity(dto);

            recipe.setDescription(dto.getDescription());
            setManufacturedProductIfExists(dto.getManufacturedProductId(), recipe);

            recipeRepository.save(recipe);

            RecipeDTO savedRecipe = recipeMapper.toDTO(recipe);

            List<RecipeStepDTO> steps = dto.getSteps();

            List<RecipeStepDTO> stepDTOs = new ArrayList<>();

            for (RecipeStepDTO stepDTO : steps) {
                RecipeStep step = recipeStepMapper.toEntity(stepDTO);
                step.setRecipe(recipe);

                recipeStepRepository.save(step);

                stepDTO = recipeStepMapper.toDTO(step);
                stepDTO.setRecipeId(savedRecipe.getId());
                stepDTOs.add(stepDTO);
            }

            savedRecipe.setSteps(stepDTOs);

            return savedRecipe;

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Actualiza una receta por su ID.
     * URL: http://localhost:4000/api/recipes/update/{id}
     *
     * @param id  ID de la receta a actualizar.
     * @param dto Objeto RecipeDTO que contiene los datos actualizados de la receta.
     * @return ResponseEntity con la receta actualizada en el cuerpo de la respuesta.
     * HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @Override
    @Transactional
    public RecipeDTO updateRecipe(Long id, RecipeDTO dto) throws Exception {
        try {
            Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new Exception("La receta a actualizar no existe"));

            setManufacturedProductIfExists(dto.getManufacturedProductId(), recipe);

            recipe.setDescription(dto.getDescription());

            recipeRepository.save(recipe);

            RecipeDTO modifiedRecipe = recipeMapper.toDTO(recipe);

            List<RecipeStep> updatedSteps = new ArrayList<>();

            List<RecipeStep> existingSteps = recipeStepRepository.findStepsByRecipeId(id);

            for (RecipeStep existingStep : existingSteps) {
                if (!verifDeletedStep(existingStep, dto.getSteps())) {
                    recipeStepRepository.delete(existingStep);
                } else {
                    updatedSteps.add(existingStep);
                }
            }

            for (RecipeStepDTO stepDTO : dto.getSteps()) {

                if (stepDTO.getId() == null) {
                    RecipeStep newStep = recipeStepMapper.toEntity(stepDTO);

                    newStep.setRecipe(recipe);

                    recipeStepRepository.save(newStep);

                    updatedSteps.add(newStep);
                } else {

                    RecipeStep verifStep = recipeStepRepository.findById(stepDTO.getId())
                            .orElse(null);

                    verifStep.setDescription(stepDTO.getDescription());

                    recipeStepRepository.save(verifStep);
                }
            }

            modifiedRecipe.setSteps(recipeStepMapper.toDTOList(updatedSteps));

            return modifiedRecipe;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void setManufacturedProductIfExists(Long manufacturedId, Recipe recipe) throws Exception {
        if (manufacturedId != null) {
            ManufacturedProduct manufacturedRecipe = manufacturedProductRepository.findById(manufacturedId).orElseThrow(() -> new Exception("El producto manufacturado de la receta no existe"));

            recipe.setManufacturedProduct(manufacturedRecipe);
        } else {
            recipe.setManufacturedProduct(null);
        }
    }

    private boolean verifDeletedStep(RecipeStep existingStep, List<RecipeStepDTO> modifiedSteps) {
        for (RecipeStepDTO frontendStep : modifiedSteps) {
            if (existingStep.getId().equals(frontendStep.getId())) {
                return true;
            }
        }
        return false;
    }
}
