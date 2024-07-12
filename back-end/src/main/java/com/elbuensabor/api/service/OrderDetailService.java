package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.OrderDTO;
import com.elbuensabor.api.dto.OrderDetailDTO;
import com.elbuensabor.api.entity.Order;
import com.elbuensabor.api.entity.OrderDetail;
import com.elbuensabor.api.mapper.OrderDetailMapper;

import java.util.List;

public interface OrderDetailService  extends GenericService<OrderDetail, OrderDetailDTO, Long>{


    List<OrderDetail> saveOrderDetails(List<OrderDetailDTO> dto , Order order) throws Exception;
    List<OrderDetail> updateOrderDetails(List<OrderDetailDTO> dto , Order order) throws Exception;
    List<OrderDetailDTO> mapperToDtoListComplete(List<OrderDetail> orderDetailList) throws Exception ;
}
