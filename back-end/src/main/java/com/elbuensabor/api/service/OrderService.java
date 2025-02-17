package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.*;
import com.elbuensabor.api.entity.Order;
import com.elbuensabor.api.repository.IOrderRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService extends GenericService<Order, OrderDTO, Long>{

    List<OrderDTO> getAllOrders() throws Exception;
    List<OrderDTO> getAllOrdersByUserId(Long id) throws Exception;
    OrderDTO getOrder(Long id) throws Exception;
    OrderDTO saveOrder(OrderDTO dto) throws Exception;
    OrderDTO updateOrder(Long id, OrderDTO dto) throws Exception;
    OrderDTO cancelOrder(Long id) throws Exception;
    List<DenominationXImageDto> getItemsList() throws Exception;
    OrderDTO updateOrderState(Long id, String newState) throws Exception;
    OrderDTO updateOrderReady(Long id) throws Exception;
}
