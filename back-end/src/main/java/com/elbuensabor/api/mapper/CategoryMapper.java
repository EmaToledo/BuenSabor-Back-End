package com.elbuensabor.api.mapper;

import com.elbuensabor.api.dto.CategoryDTO;
import com.elbuensabor.api.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends GenericMapper<Category, CategoryDTO> {

    // Obtener una instancia del CategoryMapper
    static CategoryMapper getInstance() {
        return Mappers.getMapper(CategoryMapper.class);
    }

    // Mapea una entidad Category a un DTO CategoryDTO
    @Mapping(source = "fatherCategory.id", target = "categoryFatherId")
    @Mapping(source = "fatherCategory.denomination", target = "categoryFatherDenomination")
    CategoryDTO toDTO(Category source);

    // Mapea un DTO CategoryDTO a una entidad Category
    @Mapping(target = "fatherCategory", ignore = true)
    @Mapping(target = "childCategories", ignore = true)
    Category toEntity(CategoryDTO source);

}
