package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.dto.IngredientDTO;
import com.elbuensabor.api.entity.Category;
import com.elbuensabor.api.entity.Ingredient;
import com.elbuensabor.api.mapper.GenericMapper;
import com.elbuensabor.api.mapper.IngredientMapper;
import com.elbuensabor.api.repository.ICategoryRepository;
import com.elbuensabor.api.repository.IIngredientRepository;
import com.elbuensabor.api.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IngredientServiceImpl extends GenericServiceImpl<Ingredient, IngredientDTO, Long> implements IngredientService {

    @Autowired
    private IIngredientRepository ingredientRepository;
    @Autowired
    private ICategoryRepository categoryRepository;

    private final IngredientMapper ingredientMapper = IngredientMapper.getInstance();

    /**
     * Constructor de la clase.
     *
     * @param IGenericRepository Repositorio genérico utilizado para acceder a los datos de la entidad Ingredient.
     * @param genericMapper      Mapeador genérico utilizado para convertir entre la entidad Ingredient y el DTO IngredientDTO.
     */
    public IngredientServiceImpl(com.elbuensabor.api.repository.IGenericRepository<Ingredient, Long> IGenericRepository, GenericMapper<Ingredient, IngredientDTO> genericMapper) {
        super(IGenericRepository, genericMapper);
    }

    /**
     * Guarda un ingrediente.
     *
     * @param dto DTO que representa el ingrediente a guardar.
     * @return Ingrediente guardado.
     * @throws Exception si ocurre algún error durante la operación o si la categoría del ingrediente no existe.
     */
    @Override
    @Transactional
    public Ingredient saveIngredient(IngredientDTO dto) throws Exception {
        try {
            Ingredient ingredient = ingredientMapper.toEntity(dto);

            setIngredientCategoryIfExists(dto.getIngredientCategoryID(), ingredient);

            return ingredientRepository.save(ingredient);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Actualiza un ingrediente.
     *
     * @param id  ID del ingrediente a actualizar.
     * @param dto DTO que contiene los datos actualizados del ingrediente.
     * @return Ingrediente actualizado.
     * @throws Exception si el ingrediente a actualizar no existe, si la categoría del ingrediente no existe
     *                   o si ocurre algún error durante la operación.
     */
    @Override
    @Transactional
    public Ingredient updateIngredient(Long id, IngredientDTO dto) throws Exception {
        try {
            Ingredient ingredient = ingredientRepository.findById(id)
                    .orElseThrow(() -> new Exception("El ingrediente a actualizar no existe."));

            setIngredientCategoryIfExists(dto.getIngredientCategoryID(), ingredient);

            ingredient.setDenomination(dto.getDenomination());
            ingredient.setUnit(dto.getUnit());
            ingredient.setAvailability(dto.getAvailability());

            return ingredientRepository.save(ingredient);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Asigna la categoría del ingrediente si existe en la base de datos, o la establece como null si no se proporciona una categoría.
     *
     * @param categoryId ID de la categoría del ingrediente.
     * @param ingredient Ingrediente al cual se le asignará la categoría.
     * @throws Exception si la categoría del ingrediente no existe en la base de datos.
     */
    private void setIngredientCategoryIfExists(Long categoryId, Ingredient ingredient) throws Exception {
        if (categoryId != null) {
            Category ingredientCategory = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new Exception("La categoria del ingrediente no existe"));

            ingredient.setIngredientCategory(ingredientCategory);
        } else {
            ingredient.setIngredientCategory(null);
        }
    }

    @Override
    @Transactional
    public Long getLastIngredientId() throws Exception {
        try {
            return ingredientRepository.findLastIngredientId();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Bloquea un ingrediente cambiando su disponibilidad.
     *
     * @param id           ID del ingrediente a bloquear o desbloquear.
     * @return Ingrediente actualizado.
     * @throws Exception si el ingrediente no existe o si ocurre algún error durante la operación.
     */
    @Override
    @Transactional
    public Ingredient blockIngredient(Long id) throws Exception {
        try {
            Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(() -> new Exception("Ingredient not found"));
            ingredient.setAvailability(false);

            return ingredientRepository.save(ingredient);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Desbloquea un ingrediente cambiando su disponibilidad.
     *
     * @param id           ID del ingrediente a bloquear o desbloquear.
     * @return Ingrediente actualizado.
     * @throws Exception si el ingrediente no existe o si ocurre algún error durante la operación.
     */
    @Override
    @Transactional
    public Ingredient unlockIngredient(Long id) throws Exception {
        try {
            Ingredient ingredient = ingredientRepository.findById(id).orElseThrow(() -> new Exception("Ingredient not found"));
            ingredient.setAvailability(true);

            return ingredientRepository.save(ingredient);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
