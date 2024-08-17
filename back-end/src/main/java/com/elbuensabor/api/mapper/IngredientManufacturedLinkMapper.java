package com.elbuensabor.api.mapper;

import com.elbuensabor.api.dto.IngredientManufacturedLinkDTO;
import com.elbuensabor.api.entity.IngredientManufacturedLink;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface IngredientManufacturedLinkMapper extends GenericMapper<IngredientManufacturedLink, IngredientManufacturedLinkDTO> {

    // Devuelve una instancia del mapeador de la tabla relacion
    static IngredientManufacturedLinkMapper getInstance() { return Mappers.getMapper(IngredientManufacturedLinkMapper.class); }

    // Convierte un objeto de la tabla relacion en un objeto DTO de la tabla relacion
    @Mapping(source = "manufacturedProduct.id", target = "manufacturedProductID")
    @Mapping(source = "ingredient.id", target = "ingredientID")
    IngredientManufacturedLinkDTO toDTO(IngredientManufacturedLink ingredientManufacturedLink);

    // Convierte un objeto DTO de la tabla relacion en un objeto de la tabla relacion
    @Mapping(target = "manufacturedProduct", ignore = true)
    @Mapping(target = "ingredient", ignore = true)
    IngredientManufacturedLink toEntity(IngredientManufacturedLinkDTO ingredientManufacturedLinkDTO);

}