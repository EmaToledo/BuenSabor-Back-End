package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.StockDTO;
import com.elbuensabor.api.entity.Stock;

import java.util.List;

public interface StockService extends GenericService<Stock, StockDTO, Long>{

    // Obtiene un stock individual
    public StockDTO getStock(Long id) throws Exception;

    // Obtiene la lista completa de stocks
    public List<StockDTO> getAllStock() throws Exception;

    // Guarda un stock
    Stock saveStock(StockDTO dto, Character type, Long relationId) throws Exception;

    public Stock update(Long id, Long minStock) throws Exception;

    // Actualiza un stock
    Stock addStock(Long id, StockDTO dto) throws Exception;

}