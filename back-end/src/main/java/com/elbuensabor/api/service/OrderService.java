package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.CartItemDTO;
import com.elbuensabor.api.dto.ItemListDTO;
import com.elbuensabor.api.dto.ItemPaymentMarketDTO;
import com.elbuensabor.api.dto.OrderDTO;
import com.elbuensabor.api.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService extends GenericService<Order, OrderDTO, Long>{

    List<OrderDTO> getAllOrders() throws Exception;
    List<OrderDTO> getAllOrdersByUserId(Long id) throws Exception;
    OrderDTO getOrder(Long id) throws Exception;
    OrderDTO saveOrder(OrderDTO dto) throws Exception;
    OrderDTO updateOrder(Long id, OrderDTO dto) throws Exception;
    OrderDTO cancelOrder(Long id) throws Exception;

    List<CartItemDTO> getCartItemsList() throws Exception;

    ItemListDTO getItemsList() throws Exception;
}
