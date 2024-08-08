package com.elbuensabor.api.mapper;
import com.elbuensabor.api.dto.PriceDTO;
import com.elbuensabor.api.entity.Price;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PriceMapper extends GenericMapper<Price, PriceDTO>{
    static PriceMapper getInstance() {
        return Mappers.getMapper(PriceMapper.class);
    }

    // mapea una entidad Price a un DTO PriceDTO
    PriceDTO toDTO(Price source);

    // mapea un DTO PriceDTO a una entidad Price
    @Mapping(target = "sellPriceDate", ignore = true)
    @Mapping(target = "costPriceDate", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "manufacturedProduct", ignore = true)
    Price toEntity(PriceDTO source);

}

