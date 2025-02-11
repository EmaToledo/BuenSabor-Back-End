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
    boolean verifAndDiscountOrAddStock(List<OrderDetailDTO> orderDetaisDtos, char reduceOrAddType) throws Exception;

    // testing
    boolean verifAndDisableByStock(Long id, char type) throws Exception;

    boolean verifActualStockAndQuantity(List<OrderDetailDTO> orderDetailDTOList) throws Exception;
}