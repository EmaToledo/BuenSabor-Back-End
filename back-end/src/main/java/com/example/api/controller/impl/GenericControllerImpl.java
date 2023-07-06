package com.example.api.controller.impl;

import com.example.api.controller.GenericController;
import com.example.api.dtos.GenericDTO;
import com.example.api.entity.GenericEntity;
import com.example.api.service.GenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public abstract class GenericControllerImpl<Entity extends GenericEntity, Dto extends GenericDTO> implements GenericController<Entity, Dto, Long> {

    @Autowired
    protected GenericService<Entity, Dto, Long> service;
    private static final String ERROR_MESSAGE = "{\"error\":\"Error. Por favor intente nuevamente.\"}";

    /**
     * Obtiene todos los registros de la entidad.
     *
     * @return ResponseEntity con la lista de entidades en el cuerpo de la respuesta.
     */
    @Override
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERROR_MESSAGE);
        }
    }

    /**
     * Obtiene un registro de la entidad por su ID.
     *
     * @param id ID del registro a buscar.
     * @return ResponseEntity con la entidad encontrada en el cuerpo de la respuesta.
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> getOneById(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERROR_MESSAGE);
        }
    }

    /**
     * Guarda un nuevo registro de la entidad.
     *
     * @param dto DTO con los datos del nuevo registro.
     * @return ResponseEntity con el registro guardado en el cuerpo de la respuesta.
     */
    @Override
    @PostMapping("/save")
    public ResponseEntity<?> save(Dto dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.save(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza un registro de la entidad por su ID.
     *
     * @param id  ID del registro a actualizar.
     * @param dto DTO con los nuevos datos del registro.
     * @return ResponseEntity con el registro actualizado en el cuerpo de la respuesta.
     */
    @Override
    @PutMapping("/{id}/update")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Dto dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.update(id, dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Elimina un registro de la entidad por su ID.
     *
     * @param id ID del registro a eliminar.
     * @return ResponseEntity sin contenido en el cuerpo de la respuesta.
     */
    @DeleteMapping("/{id}/delete")
    @Override
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

}
