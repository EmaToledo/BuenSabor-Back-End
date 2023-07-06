package com.example.api.controller;

import com.example.api.dtos.GenericDTO;
import com.example.api.entity.GenericEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;

public interface GenericController<Entity extends GenericEntity, Dto extends GenericDTO, ID extends Serializable> {
    /**
     * Obtiene todos los elementos.
     *
     * @return ResponseEntity con la lista de elementos en el cuerpo de la respuesta.
     */
    public ResponseEntity<?> getAll();

    /**
     * Obtiene un elemento por su ID.
     *
     * @param id ID del elemento a obtener.
     * @return ResponseEntity con el elemento en el cuerpo de la respuesta.
     */
    public ResponseEntity<?> getOneById(@PathVariable ID id);

    /**
     * Guarda un elemento.
     *
     * @param dto Objeto DTO que representa el elemento a guardar.
     * @return ResponseEntity con el elemento guardado en el cuerpo de la respuesta.
     */
    public ResponseEntity<?> save(@RequestBody Dto dto);

    /**
     * Actualiza un elemento.
     *
     * @param id  ID del elemento a actualizar.
     * @param dto Objeto DTO que contiene los datos actualizados del elemento.
     * @return ResponseEntity con el elemento actualizado en el cuerpo de la respuesta.
     */
    public ResponseEntity<?> update(@PathVariable ID id, @RequestBody Dto dto);

    /**
     * Elimina un elemento por su ID.
     *
     * @param id ID del elemento a eliminar.
     * @return ResponseEntity con un mensaje de éxito en el cuerpo de la respuesta.
     */
    public ResponseEntity<?> delete(@PathVariable ID id);
}
