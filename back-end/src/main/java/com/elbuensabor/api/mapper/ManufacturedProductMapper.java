package com.elbuensabor.api.mapper;

import com.elbuensabor.api.dtos.ManufacturedProductDTO;
import com.elbuensabor.api.entity.ManufacturedProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ManufacturedProductMapper extends GenericMapper<ManufacturedProduct, ManufacturedProductDTO> {

    // Devuelve una instancia del mapeador de productos fabricados
    static ManufacturedProductMapper getInstance() {
        return Mappers.getMapper(ManufacturedProductMapper.class);
    }

    // Convierte un objeto ManufacturedProduct en un objeto ManufacturedProductDTO
    @Mapping(source = "manufacturedProductCategory.id", target = "manufacturedProductCategoryID")
    ManufacturedProductDTO toDTO(ManufacturedProduct manufacturedProduct);

    // Convierte un objeto ManufacturedProductDTO en un objeto ManufacturedProduct
    @Mapping(target = "manufacturedProductCategory", ignore = true)
    ManufacturedProduct toEntity(ManufacturedProductDTO manufacturedProductDTO);

}
