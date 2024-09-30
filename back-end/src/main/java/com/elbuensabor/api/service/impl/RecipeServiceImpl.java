package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.dto.*;
import com.elbuensabor.api.entity.*;
import com.elbuensabor.api.mapper.*;
import com.elbuensabor.api.repository.*;
import com.elbuensabor.api.service.RecipeService;
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

    @Autowired
    private IIngredientRepository ingredientRepository;

    @Autowired
    private IIngredientRecipeLinkRepository ingredientRecipeLinkRepository;

    private final RecipeMapper recipeMapper = RecipeMapper.getInstance();
    private final RecipeStepMapper recipeStepMapper = RecipeStepMapper.getInstance();

    private final IngredientMapper ingredientMapper = IngredientMapper.getInstance();

    private final IngredientRecipeLinkMapper ingredientRecipeLinkMapper = IngredientRecipeLinkMapper.getInstance();

    // Constructor
    public RecipeServiceImpl(com.elbuensabor.api.repository.IGenericRepository<Recipe, Long> IGenericRepository, GenericMapper<Recipe, RecipeDTO> genericMapper) {
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

            List<RecipeStep> steps = recipeRepository.findStepsByRecipeIdByOrder(recipe.getId());

            List<IngredientRecipeLink> ingredientRecipeLinks = ingredientRecipeLinkRepository.findIngredientsByRecipeId(recipeId);

            List<RecipeStepDTO> stepDTOs = new ArrayList<>();

            List<IngredientQuantityDTO> ingredientQuantityDTOs = new ArrayList<>();

            for (RecipeStep step : steps) {
                RecipeStepDTO stepDTO = recipeStepMapper.toDTO(step);
                stepDTOs.add(stepDTO);
            }

            for (IngredientRecipeLink ingredientRecipeLink : ingredientRecipeLinks) {
                ingredientQuantityDTOs.add(ingredientRecipeLinkMapper.toQuantityIngredientDTO(ingredientRecipeLink));
            }

            RecipeDTO recipeDTO = recipeMapper.toDTO(recipe);
            recipeDTO.setSteps(stepDTOs);
            recipeDTO.setIngredientsQuantity(ingredientQuantityDTOs);

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

            recipe.setDenomination(dto.getDenomination());
            recipe.setDescription(dto.getDescription());

            ManufacturedProduct manufacturedProduct = setManufacturedProductIfExists(dto.getManufacturedProductId());

            recipe.setManufacturedProduct(manufacturedProduct);

            recipeRepository.save(recipe);

            RecipeDTO savedRecipe = recipeMapper.toDTO(recipe);

            List<RecipeStepDTO> steps = dto.getSteps();

            List<IngredientQuantityDTO> ingredientQuantities = dto.getIngredientsQuantity();

            List<RecipeStepDTO> stepDTOs = new ArrayList<>();

            List<IngredientQuantityDTO> ingredientQuantitiesDTOs = new ArrayList<>();

            for (RecipeStepDTO stepDTO : steps) {
                RecipeStep step = recipeStepMapper.toEntity(stepDTO);
                step.setRecipe(recipe);

                recipeStepRepository.save(step);

                stepDTO = recipeStepMapper.toDTO(step);
                stepDTO.setRecipeId(savedRecipe.getId());
                stepDTOs.add(stepDTO);
            }

            for (IngredientQuantityDTO ingredientQuantityDTO : ingredientQuantities) {
                Ingredient ingredient = verifIngredientExist(ingredientQuantityDTO.getIngredient().getId());

                IngredientRecipeLink ingredientRecipeLink = new IngredientRecipeLink();

                ingredientRecipeLink.setRecipe(recipe);
                ingredientRecipeLink.setIngredient(ingredient);
                ingredientRecipeLink.setQuantity(ingredientQuantityDTO.getQuantity());

                ingredientRecipeLinkRepository.save(ingredientRecipeLink);

                // para mostrar la receta completa al guardarla
                IngredientQuantityDTO savedIngredientQuantityDTO = new IngredientQuantityDTO();
                savedIngredientQuantityDTO.setIngredient(ingredientMapper.toDTO(ingredient));
                savedIngredientQuantityDTO.setQuantity(ingredientQuantityDTO.getQuantity());

                ingredientQuantitiesDTOs.add(savedIngredientQuantityDTO);
            }

            savedRecipe.setSteps(stepDTOs);
            savedRecipe.setIngredientsQuantity(ingredientQuantitiesDTOs);

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
    public RecipeDTO updateRecipe(Long id, RecipeDTO dto) throws Exception  {
        try {
            Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new Exception("La receta a actualizar no existe"));

            ManufacturedProduct manufacturedProduct = setManufacturedProductIfExists(dto.getManufacturedProductId());

            recipe.setManufacturedProduct(manufacturedProduct);
            recipe.setDescription(dto.getDescription());
            recipe.setDenomination(dto.getDenomination());
            recipeRepository.save(recipe);

            RecipeDTO modifiedRecipe = recipeMapper.toDTO(recipe);

            List<RecipeStep> updatedSteps = new ArrayList<>();

            List<RecipeStep> existingSteps = recipeRepository.findStepsByRecipeIdByOrder(id);

            List<IngredientQuantityDTO> updatedRelations = new ArrayList<>();

            List<IngredientRecipeLink> existingIngredientRecipeLinks = ingredientRecipeLinkRepository.findIngredientsByRecipeId(recipe.getId());


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
                    verifStep.setStepNumber(stepDTO.getStepNumber());

                    recipeStepRepository.save(verifStep);
                }
            }

            // se verifican los ingredientes existentes segun la receta y se eliminan de la tabla de relacion
            for (IngredientRecipeLink existingIngredientRecipeLink : existingIngredientRecipeLinks) {

                IngredientRecipeLinkDTO ingredientRecipeLinkDTO = ingredientRecipeLinkMapper.toDTO(existingIngredientRecipeLink);
                // se recorre la lista de los linkDTO
                if (!verifIngredientRecipeLink(ingredientRecipeLinkDTO, dto.getIngredientsQuantity())) {
                    // si no se encuentra en la lista se elimina de la tabla de relacion
                    ingredientRecipeLinkRepository.deleteById(ingredientRecipeLinkDTO.getId());
                } else {
                    // si se encuentra en la lista se agrega a otra lista de ingredientes actualizada
                    updatedRelations.add(ingredientRecipeLinkMapper.toQuantityIngredientDTO(existingIngredientRecipeLink));
                }
            }

            // se recorre la lista de relaciones actualizada
            for (IngredientQuantityDTO ingredientQuantityDTO : dto.getIngredientsQuantity()) {

                IngredientRecipeLinkDTO quantityIngredientRecipeLinkDTO = ingredientRecipeLinkMapper.toIngredientQuantityDTO(ingredientQuantityDTO);

                IngredientRecipeLink newIngredientRecipeLink = new IngredientRecipeLink();

                if(quantityIngredientRecipeLinkDTO.getId() == null){

                    newIngredientRecipeLink.setRecipe(recipe);

                    newIngredientRecipeLink.setIngredient(verifIngredientExist(quantityIngredientRecipeLinkDTO.getIngredientID()));

                    newIngredientRecipeLink.setQuantity(quantityIngredientRecipeLinkDTO.getQuantity());

                    ingredientRecipeLinkRepository.save(newIngredientRecipeLink);
                } else {
                    newIngredientRecipeLink = ingredientRecipeLinkRepository.findById(quantityIngredientRecipeLinkDTO.getId()).orElseThrow(() -> new Exception("No se encontro el ingredientQuantityLink"));

                    newIngredientRecipeLink.setIngredient(verifIngredientExist(quantityIngredientRecipeLinkDTO.getIngredientID()));
                    newIngredientRecipeLink.setQuantity(quantityIngredientRecipeLinkDTO.getQuantity());

                    ingredientRecipeLinkRepository.save(newIngredientRecipeLink);
                }

                updatedRelations.add(ingredientRecipeLinkMapper.toQuantityIngredientDTO(newIngredientRecipeLink));

            }

            modifiedRecipe.setSteps(recipeStepMapper.toDTOList(updatedSteps));
            modifiedRecipe.setIngredientsQuantity(updatedRelations);

            return modifiedRecipe;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private ManufacturedProduct setManufacturedProductIfExists(Long manufacturedId) throws Exception {
        if (manufacturedId != null) {
            ManufacturedProduct manufacturedRecipe = manufacturedProductRepository.findById(manufacturedId).orElseThrow(() -> new Exception("El producto manufacturado de la receta no existe"));

            return manufacturedRecipe;
        } else {
            return null;
        }
    }

    private Ingredient verifIngredientExist(Long ingredientId) throws Exception {
        if (ingredientId != null) {
            Ingredient ingredientRecipe = ingredientRepository.findById(ingredientId).orElseThrow(() -> new Exception("El ingrediente que se desea relacionar no existe"));

            return ingredientRecipe;
        } else {
            return null;
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

    private boolean verifIngredientRecipeLink(IngredientRecipeLinkDTO existingRelation, List<IngredientQuantityDTO> modifiedRelations) {
        for (IngredientQuantityDTO frontendRelation : modifiedRelations) {
            if (existingRelation.getId().equals(frontendRelation.getId())) {
                return true;
            }
        }
        return false;
    }
}