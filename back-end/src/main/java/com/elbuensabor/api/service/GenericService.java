package com.elbuensabor.api.service;

import com.elbuensabor.api.dtos.GenericDTO;
import com.elbuensabor.api.entity.GenericEntity;

import java.io.Serializable;
import java.util.List;

public interface GenericService<Entity extends GenericEntity, Dto extends GenericDTO, ID extends Serializable> {
    /**
     * Obtiene todos los elementos de tipo Dto.
     *
     * @return Una lista de Dto.
     * @throws Exception si ocurre un error al obtener los elementos.
     */
    public List<Dto> findAll() throws Exception;

    /**
     * Obtiene un elemento Dto por su ID.
     *
     * @param id el ID del elemento a buscar.
     * @return El Dto correspondiente al ID.
     * @throws Exception si no se encuentra el elemento o si ocurre un error.
     */
    public Dto findById(ID id) throws Exception;

    /**
     * Guarda un elemento Entity a partir de un Dto.
     *
     * @param dto el Dto que contiene los datos del elemento a guardar.
     * @return El Entity guardado.
     * @throws Exception si ocurre un error al guardar el elemento.
     */
    public Entity save(Dto dto) throws Exception;

    /**
     * Actualiza un elemento Entity a partir de un Dto.
     *
     * @param id  el ID del elemento a actualizar.
     * @param dto el Dto que contiene los datos actualizados del elemento.
     * @return El Entity actualizado.
     * @throws Exception si no se encuentra el elemento o si ocurre un error al actualizarlo.
     */
    public Entity update(ID id, Dto dto) throws Exception;

    /**
     * Elimina un elemento por su ID.
     *
     * @param id el ID del elemento a eliminar.
     * @throws Exception si no se encuentra el elemento o si ocurre un error al eliminarlo.
     */
    public void delete(ID id) throws Exception;
}
