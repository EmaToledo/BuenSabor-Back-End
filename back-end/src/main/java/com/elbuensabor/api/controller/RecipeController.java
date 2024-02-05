package com.elbuensabor.api.controller;

import com.elbuensabor.api.controller.impl.GenericControllerImpl;
import com.elbuensabor.api.dto.RecipeDTO;
import com.elbuensabor.api.entity.Recipe;
import com.elbuensabor.api.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/recipes")
public class RecipeController extends GenericControllerImpl<Recipe, RecipeDTO> {

    @Autowired
    private RecipeService recipeService;

    private static final String ERROR_MESSAGE = "{\"error\":\"Error. Por favor intente nuevamente.\"}";

    /**
     * Obtiene una receta completa por su ID.
     * URL: http://localhost:4000/api/recipes/complete/{id}
     *
     * @param id ID de la receta a obtener.
     * @return ResponseEntity con la receta completa en el cuerpo de la respuesta.
     * HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @GetMapping(value = "/complete/{id}")
    public ResponseEntity<?> getCompleteRecipe(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(recipeService.getCompleteRecipe(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
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
    @PostMapping(value = "/save")
    public ResponseEntity<?> save(@RequestBody RecipeDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(recipeService.saveRecipe(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
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
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody RecipeDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(recipeService.updateRecipe(id, dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }
}
