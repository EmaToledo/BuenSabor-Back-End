package com.example.api.mapper;

import com.example.api.dtos.CategoryDTO;
import com.example.api.entity.Category;
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
    @Mapping(source = "source.fatherCategory.id", target = "categoryFatherId")
    @Mapping(source = "source.fatherCategory.denomination", target = "categoryFatherDenomination")
    CategoryDTO toDTO(Category source);

    // Mapea un DTO CategoryDTO a una entidad Category
    @Mapping(target = "fatherCategory", ignore = true)
    @Mapping(target = "childCategories", ignore = true)
    Category toEntity(CategoryDTO source);

}
