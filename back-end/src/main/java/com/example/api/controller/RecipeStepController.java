package com.example.api.controller;

import com.example.api.controller.impl.GenericControllerImpl;
import com.example.api.dtos.RecipeStepDTO;
import com.example.api.entity.RecipeStep;
import com.example.api.service.RecipeStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/recipes/steps")
public class RecipeStepController extends GenericControllerImpl<RecipeStep, RecipeStepDTO> {

    @Autowired
    private RecipeStepService recipeStepService;

    private static final String ERROR_MESSAGE = "{\"error\":\"Error. Por favor intente nuevamente.\"}";

    /**
     * Guarda un paso de receta.
     * URL: http://localhost:4000/api/recipes/steps/save
     *
     * @param dto Objeto RecipeStepDTO que representa el paso de receta a guardar.
     * @return ResponseEntity con el paso de receta guardado en el cuerpo de la respuesta.
     * HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PostMapping(value = "/save")
    public ResponseEntity<?> save(@RequestBody RecipeStepDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(recipeStepService.saveRecipeStep(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
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
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RecipeStepDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(recipeStepService.updateRecipeStep(id, dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }
}

