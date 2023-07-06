package com.example.api.mapper;

import com.example.api.dtos.ProductDTO;
import com.example.api.entity.Category;
import com.example.api.entity.Product;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-26T20:37:12-0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.6.1.jar, environment: Java 17 (Oracle Corporation)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public List<ProductDTO> toDTOsList(List<Product> source) {
        if ( source == null ) {
            return null;
        }

        List<ProductDTO> list = new ArrayList<ProductDTO>( source.size() );
        for ( Product product : source ) {
            list.add( toDTO( product ) );
        }

        return list;
    }

    @Override
    public List<Product> toEntitiesList(List<ProductDTO> source) {
        if ( source == null ) {
            return null;
        }

        List<Product> list = new ArrayList<Product>( source.size() );
        for ( ProductDTO productDTO : source ) {
            list.add( toEntity( productDTO ) );
        }

        return list;
    }

    @Override
    public ProductDTO toDTO(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();

        productDTO.setProductCategoryID( productProductCategoryId( product ) );
        productDTO.setId( product.getId() );
        productDTO.setDenomination( product.getDenomination() );
        productDTO.setDescription( product.getDescription() );
        productDTO.setCooking_time( product.getCooking_time() );
        productDTO.setAvailability( product.getAvailability() );

        return productDTO;
    }

    @Override
    public Product toEntity(ProductDTO productDTO) {
        if ( productDTO == null ) {
            return null;
        }

        Product product = new Product();

        product.setId( productDTO.getId() );
        product.setDenomination( productDTO.getDenomination() );
        product.setDescription( productDTO.getDescription() );
        product.setCooking_time( productDTO.getCooking_time() );
        product.setAvailability( productDTO.getAvailability() );

        return product;
    }

    private Long productProductCategoryId(Product product) {
        if ( product == null ) {
            return null;
        }
        Category productCategory = product.getProductCategory();
        if ( productCategory == null ) {
            return null;
        }
        Long id = productCategory.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
