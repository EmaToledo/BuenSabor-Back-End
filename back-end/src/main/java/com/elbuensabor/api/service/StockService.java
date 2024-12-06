package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.OrderDetailDTO;
import com.elbuensabor.api.dto.StockDTO;
import com.elbuensabor.api.entity.Stock;

import java.util.List;
import java.util.Map;

public interface StockService extends GenericService<Stock, StockDTO, Long>{

    // Guarda un stock
    Stock saveStock(StockDTO dto, Character type, Long relationId) throws Exception;
    // Actualiza un stock
    Stock update(Long id, StockDTO dto) throws Exception;
    // Verifica la cantidad de stock utilizado en una orden ya sea producto o manufacturado y si es posible los reduce
    boolean verifAndDiscountStock(List<OrderDetailDTO> orderDetaisDtos) throws Exception;
    // Devuelve el stock para algunos casos de modificaciones o eliminaciones
    boolean reStock(List<OrderDetailDTO> deletedOrderDetails) throws Exception;
}