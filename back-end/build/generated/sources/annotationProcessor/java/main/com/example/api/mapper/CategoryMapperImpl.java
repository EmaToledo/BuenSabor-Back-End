package com.example.api.mapper;

import com.example.api.dtos.CategoryDTO;
import com.example.api.entity.Category;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-07-05T21:22:33-0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.1.jar, environment: Java 17.0.6 (Amazon.com Inc.)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public List<CategoryDTO> toDTOsList(List<Category> source) {
        if ( source == null ) {
            return null;
        }

        List<CategoryDTO> list = new ArrayList<CategoryDTO>( source.size() );
        for ( Category category : source ) {
            list.add( toDTO( category ) );
        }

        return list;
    }

    @Override
    public List<Category> toEntitiesList(List<CategoryDTO> source) {
        if ( source == null ) {
            return null;
        }

        List<Category> list = new ArrayList<Category>( source.size() );
        for ( CategoryDTO categoryDTO : source ) {
            list.add( toEntity( categoryDTO ) );
        }

        return list;
    }

    @Override
    public CategoryDTO toDTO(Category source) {
        if ( source == null ) {
            return null;
        }

        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setCategoryFatherId( sourceFatherCategoryId( source ) );
        categoryDTO.setCategoryFatherDenomination( sourceFatherCategoryDenomination( source ) );
        categoryDTO.setId( source.getId() );
        categoryDTO.setDenomination( source.getDenomination() );
        categoryDTO.setType( source.getType() );
        categoryDTO.setAvailability( source.getAvailability() );

        return categoryDTO;
    }

    @Override
    public Category toEntity(CategoryDTO source) {
        if ( source == null ) {
            return null;
        }

        Category category = new Category();

        category.setId( source.getId() );
        category.setDenomination( source.getDenomination() );
        category.setType( source.getType() );
        category.setAvailability( source.getAvailability() );

        return category;
    }

    private Long sourceFatherCategoryId(Category category) {
        if ( category == null ) {
            return null;
        }
        Category fatherCategory = category.getFatherCategory();
        if ( fatherCategory == null ) {
            return null;
        }
        Long id = fatherCategory.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String sourceFatherCategoryDenomination(Category category) {
        if ( category == null ) {
            return null;
        }
        Category fatherCategory = category.getFatherCategory();
        if ( fatherCategory == null ) {
            return null;
        }
        String denomination = fatherCategory.getDenomination();
        if ( denomination == null ) {
            return null;
        }
        return denomination;
    }
}
