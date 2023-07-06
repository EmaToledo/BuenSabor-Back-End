package com.example.api.service.impl;

import com.example.api.dtos.GenericDTO;
import com.example.api.entity.GenericEntity;
import com.example.api.mapper.GenericMapper;
import com.example.api.repository.IGenericRepository;
import com.example.api.service.GenericService;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class GenericServiceImpl<Entity extends GenericEntity, Dto extends GenericDTO, ID extends Serializable> implements GenericService<Entity, Dto, ID> {

    protected IGenericRepository<Entity, ID> IGenericRepository;
    protected GenericMapper<Entity, Dto> genericMapper;

    public GenericServiceImpl(IGenericRepository<Entity, ID> IGenericRepository, GenericMapper<Entity, Dto> genericMapper) {
        this.IGenericRepository = IGenericRepository;
        this.genericMapper = genericMapper;
    }

    @Override
    public List<Dto> findAll() throws Exception {
        try {
            List<Entity> entities = IGenericRepository.findAll();
            return genericMapper.toDTOsList(entities);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Dto findById(ID id) throws Exception {
        try {
            Entity entity = IGenericRepository.findById(id).get();
            return genericMapper.toDTO(entity);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Entity save(Dto dto) throws Exception {
        try {
            return IGenericRepository.save(genericMapper.toEntity(dto));
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

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
