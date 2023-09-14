package com.example.api.mapper;

import com.example.api.dtos.RecipeStepDTO;
import com.example.api.entity.RecipeStep;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeStepMapper extends GenericMapper<RecipeStep, RecipeStepDTO> {

    // Obtener una instancia del RecipeStepMapper
    static RecipeStepMapper getInstance() {
        return Mappers.getMapper(RecipeStepMapper.class);
    }

    // Mapea una entidad RecipeStep a un DTO RecipeStepDTO
    @Mapping(source = "recipe.id", target = "recipeId")
    RecipeStepDTO toDTO(RecipeStep source);

    // Mapea un DTO RecipeStepDTO a una entidad RecipeStep
    @Mapping(target = "recipe", ignore = true)
    RecipeStep toEntity(RecipeStepDTO source);

    List<RecipeStepDTO> toDTOList(List<RecipeStep> source);

    List<RecipeStep> toEntityList(List<RecipeStepDTO> source);

}
