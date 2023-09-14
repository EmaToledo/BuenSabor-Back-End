package com.example.api.mapper;

import com.example.api.dtos.RecipeDTO;
import com.example.api.dtos.RecipeStepDTO;
import com.example.api.entity.Recipe;
import com.example.api.entity.RecipeStep;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RecipeMapper extends GenericMapper<Recipe, RecipeDTO> {

    // Obtener una instancia del RecipeMapper
    static RecipeMapper getInstance() {
        return Mappers.getMapper(RecipeMapper.class);
    }

    // Mapea una entidad Recipe a un DTO RecipeDTO
    @Mapping(source = "manufacturedProduct.id", target = "manufacturedProductId")
    RecipeDTO toDTO(Recipe source);

    // Mapea un DTO RecipeDTO a una entidad Recipe
    @Mapping(target = "manufacturedProduct", ignore = true)
    Recipe toEntity(RecipeDTO source);

    List<RecipeStepDTO> toDTOList(List<RecipeStep> source);

    List<RecipeStep> toEntityList(List<RecipeStepDTO> source);

}
