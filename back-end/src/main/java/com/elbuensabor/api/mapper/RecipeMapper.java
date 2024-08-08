package com.elbuensabor.api.mapper;

import com.elbuensabor.api.dto.RecipeDTO;
import com.elbuensabor.api.dto.RecipeStepDTO;
import com.elbuensabor.api.entity.Recipe;
import com.elbuensabor.api.entity.RecipeStep;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",uses = {RecipeStepMapper.class})
public interface RecipeMapper extends GenericMapper<Recipe, RecipeDTO> {

    // Obtener una instancia del RecipeMapper
    static RecipeMapper getInstance() {
        return Mappers.getMapper(RecipeMapper.class);
    }

    // Mapea una entidad Recipe a un DTO RecipeDTO
    @Mapping(source = "manufacturedProduct.id", target = "manufacturedProductId")
    @Mapping(ignore = true, target = "steps")
    RecipeDTO toDTO(Recipe source);

    // Mapea un DTO RecipeDTO a una entidad Recipe
    @Mapping(target = "manufacturedProduct", ignore = true)
    Recipe toEntity(RecipeDTO source);

    @Mapping(source = "recipe.id", target = "recipeId")
    List<RecipeStepDTO> toDTOList(List<RecipeStep> source);

    @Mapping(target = "recipe.id", source = "recipeId")
    List<RecipeStep> toEntityList(List<RecipeStepDTO> source);

}
