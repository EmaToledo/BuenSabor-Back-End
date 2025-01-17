package com.elbuensabor.api.mapper;

import com.elbuensabor.api.dto.OrderDTO;
import com.elbuensabor.api.dto.OrderDetailDTO;
import com.elbuensabor.api.entity.Order;
import com.elbuensabor.api.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper extends GenericMapper<OrderDetail, OrderDetailDTO>{

    static OrderDetailMapper getInstance() {
        return Mappers.getMapper(OrderDetailMapper.class);
    }
    @Mapping(ignore = true, target = "itemProduct")
    @Mapping(ignore = true, target = "itemManufacturedProduct")
    OrderDetailDTO toDTO(OrderDetail source);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "manufacturedProduct", ignore = true)
    OrderDetail toEntity(OrderDetailDTO source);

}
