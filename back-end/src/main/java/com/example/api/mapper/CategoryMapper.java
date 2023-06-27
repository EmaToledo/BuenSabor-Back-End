package com.example.api.mapper;

import com.example.api.dtos.CategoryDTO;
import com.example.api.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends GenericMapper<Category, CategoryDTO> {

    static CategoryMapper getInstance() {
        return Mappers.getMapper(CategoryMapper.class);
    }

    @Mapping(source = "source.fatherCategory.id", target = "categoryFatherId")
    @Mapping(source = "source.fatherCategory.denomination", target = "categoryFatherDenomination")
    CategoryDTO toDTO(Category source);

    @Mapping(target = "fatherCategory", ignore = true)
    @Mapping(target = "childCategories", ignore = true)
    Category toEntity(CategoryDTO source);

}
