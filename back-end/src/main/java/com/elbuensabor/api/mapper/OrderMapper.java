package com.elbuensabor.api.mapper;

import com.elbuensabor.api.dto.OrderDTO;
import com.elbuensabor.api.dto.RoleDTO;
import com.elbuensabor.api.entity.Order;
import com.elbuensabor.api.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper extends GenericMapper<Order, OrderDTO>{
    static OrderMapper getInstance() {
        return Mappers.getMapper(OrderMapper.class);
    }

    @Mapping(source = "user.address", target = "address")
    @Mapping(source = "user.apartment", target = "apartment")
    @Mapping(source = "user.phone", target = "phone")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "user.lastName", target = "userLastName")
    @Mapping(ignore = true, target = "orderDetails")
    OrderDTO toDTO(Order order);

    @Mapping(target = "orderDetails", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "dateTime", ignore = true)
    Order toEntity(OrderDTO orderDTO);

    List<OrderDTO> toDTOList(List<Order> source);

    List<Order> toEntityList(List<OrderDTO> source);
}
