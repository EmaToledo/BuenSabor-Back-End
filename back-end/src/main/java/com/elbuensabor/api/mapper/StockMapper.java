package com.elbuensabor.api.mapper;

import com.elbuensabor.api.dto.StockDTO;
import com.elbuensabor.api.entity.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StockMapper extends GenericMapper<Stock, StockDTO>{
    static StockMapper getInstance() {return Mappers.getMapper(StockMapper.class);}

    // mapea una entidad Stock a un DTO StockDTO
    @Mapping(source = "productStock.id", target = "productStockID")
    @Mapping(source = "ingredientStock.id", target = "ingredientStockID")
    StockDTO toDTO(Stock source);

    // mapea un DTO StockDTO a una entidad Stock
    @Mapping(source = "productStockID", target = "productStock.id")
    @Mapping(source = "ingredientStockID", target = "ingredientStock.id")
    Stock toEntity(StockDTO source);

}
