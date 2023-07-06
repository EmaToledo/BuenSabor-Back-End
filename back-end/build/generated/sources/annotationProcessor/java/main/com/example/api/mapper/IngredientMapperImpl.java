package com.example.api.mapper;

import com.example.api.dtos.IngredientDTO;
import com.example.api.entity.Category;
import com.example.api.entity.Ingredient;
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
public class IngredientMapperImpl implements IngredientMapper {

    @Override
    public List<IngredientDTO> toDTOsList(List<Ingredient> source) {
        if ( source == null ) {
            return null;
        }

        List<IngredientDTO> list = new ArrayList<IngredientDTO>( source.size() );
        for ( Ingredient ingredient : source ) {
            list.add( toDTO( ingredient ) );
        }

        return list;
    }

    @Override
    public List<Ingredient> toEntitiesList(List<IngredientDTO> source) {
        if ( source == null ) {
            return null;
        }

        List<Ingredient> list = new ArrayList<Ingredient>( source.size() );
        for ( IngredientDTO ingredientDTO : source ) {
            list.add( toEntity( ingredientDTO ) );
        }

        return list;
    }

    @Override
    public IngredientDTO toDTO(Ingredient ingredient) {
        if ( ingredient == null ) {
            return null;
        }

        IngredientDTO ingredientDTO = new IngredientDTO();

        ingredientDTO.setIngredientCategoryID( ingredientIngredientCategoryId( ingredient ) );
        ingredientDTO.setId( ingredient.getId() );
        ingredientDTO.setDenomination( ingredient.getDenomination() );
        ingredientDTO.setMinStock( ingredient.getMinStock() );
        ingredientDTO.setActualStock( ingredient.getActualStock() );
        ingredientDTO.setUnit( ingredient.getUnit() );
        ingredientDTO.setAvailability( ingredient.getAvailability() );

        return ingredientDTO;
    }

    @Override
    public Ingredient toEntity(IngredientDTO ingredientDTO) {
        if ( ingredientDTO == null ) {
            return null;
        }

        Ingredient ingredient = new Ingredient();

        ingredient.setId( ingredientDTO.getId() );
        ingredient.setDenomination( ingredientDTO.getDenomination() );
        ingredient.setMinStock( ingredientDTO.getMinStock() );
        ingredient.setActualStock( ingredientDTO.getActualStock() );
        ingredient.setUnit( ingredientDTO.getUnit() );
        ingredient.setAvailability( ingredientDTO.getAvailability() );

        return ingredient;
    }

    private Long ingredientIngredientCategoryId(Ingredient ingredient) {
        if ( ingredient == null ) {
            return null;
        }
        Category ingredientCategory = ingredient.getIngredientCategory();
        if ( ingredientCategory == null ) {
            return null;
        }
        Long id = ingredientCategory.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
