package com.elbuensabor.api.mapper;

import com.elbuensabor.api.dto.IngredientDTO;
import com.elbuensabor.api.entity.Ingredient;
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
    @Mapping(source = "ingredientCategory.denomination", target = "categoryDenomination")
    IngredientDTO toDTO(Ingredient ingredient);

    // Convierte un objeto IngredientDTO en un objeto Ingredient
    @Mapping(target = "ingredientCategory", ignore = true)
    Ingredient toEntity(IngredientDTO ingredientDTO);

}
