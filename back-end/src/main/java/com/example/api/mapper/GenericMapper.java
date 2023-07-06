package com.example.api.mapper;

import com.example.api.dtos.GenericDTO;
import com.example.api.entity.GenericEntity;

import java.util.List;

public interface GenericMapper<Entity extends GenericEntity, Dto extends GenericDTO> {
    // Convierte una entidad en un DTO
    Dto toDTO(Entity source);

    // Convierte un DTO en una entidad
    Entity toEntity(Dto source);

    // Convierte una lista de entidades en una lista de DTOs
    List<Dto> toDTOsList(List<Entity> source);

    // Convierte una lista de DTOs en una lista de entidades
    List<Entity> toEntitiesList(List<Dto> source);
}
