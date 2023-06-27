package com.example.api.service;

import com.example.api.dtos.GenericDTO;
import com.example.api.entity.GenericEntity;

import java.io.Serializable;
import java.util.List;

public interface GenericService<Entity extends GenericEntity, Dto extends GenericDTO, ID extends Serializable> {
    public List<Dto> findAll() throws Exception;

    public Dto findById(ID id) throws Exception;

    public Entity save(Dto dto) throws Exception;

    public Entity update(ID id, Dto dto) throws Exception;

    public void delete(ID id) throws Exception;
}