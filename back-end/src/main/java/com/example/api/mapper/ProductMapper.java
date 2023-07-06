package com.example.api.mapper;

import com.example.api.dtos.ProductDTO;
import com.example.api.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper extends GenericMapper<Product, ProductDTO> {

    static ProductMapper getInstance() {
        return Mappers.getMapper(ProductMapper.class);
    }

    @Mapping(source = "productCategory.id", target = "productCategoryID")
    ProductDTO toDTO(Product product);

    @Mapping(target = "productCategory", ignore = true)
    Product toEntity(ProductDTO productDTO);

}
