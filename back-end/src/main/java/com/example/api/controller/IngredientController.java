package com.example.api.controller;

import com.example.api.controller.impl.GenericControllerImpl;
import com.example.api.dtos.IngredientDTO;
import com.example.api.entity.Ingredient;
import com.example.api.service.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/ingredients")
public class IngredientController extends GenericControllerImpl<Ingredient, IngredientDTO> {

    @Autowired
    private IngredientService service;

    private static final String ERROR_MESSAGE = "{\"error\":\"Error. Por favor intente nuevamente.\"}";

    /**
     * Guarda un ingrediente.
     * URL: http://localhost:4000/api/ingredients/save
     *
     * @param dto Objeto IngredientDTO que representa el ingrediente a guardar.
     * @return ResponseEntity con el ingrediente guardado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PostMapping(value = "/save")
    public ResponseEntity<?> save(@RequestBody IngredientDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.saveIngredient(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza un ingrediente.
     * URL: http://localhost:4000/api/ingredients/update/{id}
     *
     * @param id  ID del ingrediente a actualizar.
     * @param dto Objeto IngredientDTO que contiene los datos actualizados del ingrediente.
     * @return ResponseEntity con el ingrediente actualizado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody IngredientDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.updateIngredient(id, dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Bloquea o desbloquea un ingrediente.
     * URL: http://localhost:4000/api/ingredients/block/{id}
     *
     * @param id      ID del ingrediente a bloquear.
     * @return ResponseEntity con el ingrediente bloqueado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PutMapping("/block/{id}")
    public ResponseEntity<?> blockIngredient(@PathVariable Long id) {
        try {
            Ingredient ingredient = service.blockIngredient(id);
            return ResponseEntity.status(HttpStatus.OK).body(ingredient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Bloquea o desbloquea un ingrediente.
     * URL: http://localhost:4000/api/ingredients/block/{id}
     *
     * @param id      ID del ingrediente a desbloquear.
     * @return ResponseEntity con el ingrediente desbloqueado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PutMapping("/unlock/{id}")
    public ResponseEntity<?> unlockIngredient(@PathVariable Long id) {
        try {
            Ingredient ingredient = service.unlockIngredient(id);
            return ResponseEntity.status(HttpStatus.OK).body(ingredient);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

}
