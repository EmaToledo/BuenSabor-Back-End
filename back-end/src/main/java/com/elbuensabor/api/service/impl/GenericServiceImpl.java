package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.dto.GenericDTO;
import com.elbuensabor.api.dto.OrderDTO;
import com.elbuensabor.api.entity.GenericEntity;
import com.elbuensabor.api.mapper.GenericMapper;
import com.elbuensabor.api.repository.IGenericRepository;
import com.elbuensabor.api.service.GenericService;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class GenericServiceImpl<Entity extends GenericEntity, Dto extends GenericDTO, ID extends Serializable> implements GenericService<Entity, Dto, ID> {

    protected IGenericRepository<Entity, ID> IGenericRepository;
    protected GenericMapper<Entity, Dto> genericMapper;

    /**
     * Constructor de la clase.
     *
     * @param IGenericRepository Repositorio genérico utilizado para acceder a los datos de la entidad.
     * @param genericMapper      Mapeador genérico utilizado para convertir entre la entidad y el DTO.
     */
    public GenericServiceImpl(IGenericRepository<Entity, ID> IGenericRepository, GenericMapper<Entity, Dto> genericMapper) {
        this.IGenericRepository = IGenericRepository;
        this.genericMapper = genericMapper;
    }

    /**
     * Recupera todas las entidades.
     *
     * @return Lista de DTOs que representan las entidades.
     * @throws Exception si ocurre algún error durante la operación.
     */
    @Override
    public List<Dto> findAll() throws Exception {
        try {
            List<Entity> entities = IGenericRepository.findAll();
            return genericMapper.toDTOsList(entities);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Busca una entidad por su ID.
     *
     * @param id ID de la entidad a buscar.
     * @return DTO que representa la entidad encontrada.
     * @throws Exception si no se encuentra la entidad o si ocurre algún error durante la operación.
     */
    @Override
    public Dto findById(ID id) throws Exception {
        try {
            Entity entity = IGenericRepository.findById(id).get();
            return genericMapper.toDTO(entity);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Guarda una entidad.
     *
     * @param dto DTO que representa la entidad a guardar.
     * @return Entidad guardada.
     * @throws Exception si ocurre algún error durante la operación.
     */
    @Override
    @Transactional
    public Entity save(Dto dto) throws Exception {
        try {
            return IGenericRepository.save(genericMapper.toEntity(dto));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Actualiza una entidad.
     *
     * @param id  ID de la entidad a actualizar.
     * @param dto DTO que contiene los datos actualizados de la entidad.
     * @return Entidad actualizada.
     * @throws Exception si no se encuentra la entidad a actualizar o si ocurre algún error durante la operación.
     */
    @Override
    @Transactional
    public Entity update(ID id, Dto dto) throws Exception {
        try {
            Optional<Entity> entityOptional = IGenericRepository.findById(id);

            if (entityOptional.isEmpty()) {
                throw new Exception("No se encontro la entidad a actualizar");
            }
            return IGenericRepository.save(genericMapper.toEntity(dto));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Elimina una entidad por su ID.
     *
     * @param id ID de la entidad a eliminar.
     * @throws Exception si no se encuentra la entidad a eliminar o si ocurre algún error durante la operación.
     */
    @Override
    @Transactional
    public void delete(ID id) throws Exception {
        try {
            if (!IGenericRepository.existsById(id)) {
                throw new Exception("No se encontro la entidad a eliminar");
            }
            IGenericRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
