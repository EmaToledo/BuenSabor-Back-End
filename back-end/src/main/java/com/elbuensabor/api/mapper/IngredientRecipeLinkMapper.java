package com.elbuensabor.api.mapper;

import com.elbuensabor.api.dto.IngredientQuantityDTO;
import com.elbuensabor.api.dto.IngredientRecipeLinkDTO;
import com.elbuensabor.api.entity.IngredientRecipeLink;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IngredientRecipeLinkMapper extends GenericMapper<IngredientRecipeLink, IngredientRecipeLinkDTO> {

    // Devuelve una instancia del mapeador de la tabla relacion
    static IngredientRecipeLinkMapper getInstance() { return Mappers.getMapper(IngredientRecipeLinkMapper.class); }

    // Convierte un objeto de la tabla relacion en un objeto DTO de la tabla relacion
    @Mapping(source = "recipe.id", target = "recipeID")
    @Mapping(source = "ingredient.id", target = "ingredientID")
    IngredientRecipeLinkDTO toDTO(IngredientRecipeLink ingredientRecipeLink);

    // Convierte un objeto DTO de la tabla relacion en un objeto de la tabla relacion
    @Mapping(target = "recipe", ignore = true)
    @Mapping(target = "ingredient", ignore = true)
    @Mapping(target = "quantity", ignore = true)
    IngredientRecipeLink toEntity(IngredientRecipeLinkDTO ingredientRecipeLinkDTO);

    // Convierte un objeto IngredientLinkDTO a IngredientQuantityDTO
    @Mapping(target = "recipeID", ignore = true)
    @Mapping(source = "ingredient.id", target = "ingredientID")
    @Mapping(source = "quantity", target = "quantity")
    IngredientRecipeLinkDTO toIngredientQuantityDTO(IngredientQuantityDTO ingredientQuantityDTO);

    // Convierte un objeto IngredientLinkDTO a IngredientQuantityDTO
    @Mapping(source = "ingredient.id", target = "ingredient.id")
    @Mapping(source = "quantity", target = "quantity")
    IngredientQuantityDTO toQuantityIngredientDTO(IngredientRecipeLink ingredientRecipeLink);

}