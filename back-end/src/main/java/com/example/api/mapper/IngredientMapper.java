package com.example.api.mapper;

import com.example.api.dtos.IngredientDTO;
import com.example.api.entity.Ingredient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IngredientMapper extends GenericMapper<Ingredient, IngredientDTO> {

    // Devuelve una instancia del mapeador de ingredientes
    static IngredientMapper getInstance() {
        return Mappers.getMapper(IngredientMapper.class);
    }

    // Convierte un objeto Ingredient en un objeto IngredientDTO
    @Mapping(source = "ingredientCategory.id", target = "ingredientCategoryID")
    IngredientDTO toDTO(Ingredient ingredient);

    // Convierte un objeto IngredientDTO en un objeto Ingredient
    @Mapping(target = "ingredientCategory", ignore = true)
    Ingredient toEntity(IngredientDTO ingredientDTO);

}
