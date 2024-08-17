package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.PriceDTO;
import com.elbuensabor.api.entity.Price;

public interface PriceService extends GenericService<Price, PriceDTO, Long>{
    // Guarda un Precio
    Price savePrice(PriceDTO dto,Long idItem,Integer filter) throws Exception;

    // Actualiza un Precio
    Price update(PriceDTO dto,Long idItem,Integer filter) throws Exception;
    PriceDTO getPricebyIdFilter(Long id , Integer filter) throws Exception;
    PriceDTO getOnlySellPrice(Long id , Integer filter) throws Exception;

}
