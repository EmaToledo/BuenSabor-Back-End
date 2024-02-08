package com.elbuensabor.api.mapper;

import com.elbuensabor.api.dto.ProductDTO;
import com.elbuensabor.api.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper extends GenericMapper<Product, ProductDTO> {

    // Devuelve una instancia del mapeador de productos
    static ProductMapper getInstance() {
        return Mappers.getMapper(ProductMapper.class);
    }

    // Convierte un objeto Product en un objeto ProductDTO
    @Mapping(source = "productCategory.id", target = "productCategoryID")
    ProductDTO toDTO(Product product);

    // Convierte un objeto ProductDTO en un objeto Product
    @Mapping(target = "productCategory", ignore = true)
    Product toEntity(ProductDTO productDTO);

}
