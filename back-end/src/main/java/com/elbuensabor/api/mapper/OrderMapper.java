package com.elbuensabor.api.mapper;

import com.elbuensabor.api.dto.OrderDTO;
import com.elbuensabor.api.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper extends GenericMapper<Order, OrderDTO>{
    static OrderMapper getInstance() {
        return Mappers.getMapper(OrderMapper.class);
    }

}
