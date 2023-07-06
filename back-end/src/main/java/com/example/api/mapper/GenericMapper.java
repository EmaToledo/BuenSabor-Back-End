package com.example.api.mapper;

import com.example.api.dtos.GenericDTO;
import com.example.api.entity.GenericEntity;

import java.util.List;

public interface GenericMapper<Entity extends GenericEntity, Dto extends GenericDTO> {
    Dto toDTO(Entity source);

    Entity toEntity(Dto source);

    List<Dto> toDTOsList(List<Entity> source);

    List<Entity> toEntitiesList(List<Dto> source);
}
