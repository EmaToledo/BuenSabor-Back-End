package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.dto.OrderDTO;
import com.elbuensabor.api.dto.PriceDTO;
import com.elbuensabor.api.entity.*;
import com.elbuensabor.api.mapper.GenericMapper;
import com.elbuensabor.api.mapper.PriceMapper;
import com.elbuensabor.api.repository.IManufacturedProductRepository;
import com.elbuensabor.api.repository.IPriceRepository;
import com.elbuensabor.api.repository.IProductRepository;
import com.elbuensabor.api.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;


@Service
public class PriceServiceImpl extends GenericServiceImpl<Price, PriceDTO, Long> implements PriceService {
    @Autowired
    private IPriceRepository priceRepository;
    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IManufacturedProductRepository manufacturedProductRepository;
    private final PriceMapper priceMapper = PriceMapper.getInstance();
    public PriceServiceImpl(com.elbuensabor.api.repository.IGenericRepository<Price, Long> genericRepository, GenericMapper<Price, PriceDTO> genericMapper) {
        super(genericRepository, genericMapper);
    }

    @Override
    public OrderDTO updateOrderState(Long id, String newState) throws Exception {
        return null;
    }



    @Override
    @Transactional()
    public Price savePrice(PriceDTO dto,Long idItem,Integer filter) throws Exception {
        try {
            Price price = priceMapper.toEntity(dto);
            setIdRelationsIfExists(idItem,filter,price);
            price.setCostPriceDate(LocalDateTime.now());
            return priceRepository.save(price);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional()
    public Price update(PriceDTO dto,Long idItem,Integer filter) throws Exception {
        try {
            Price price = priceMapper.toEntity(getPricebyIdFilter(idItem,filter));
            setIdRelationsIfExists(idItem,filter,price);
            price.setSellPrice(dto.getSellPrice());
            price.setCostPrice(dto.getCostPrice());
            price.setCostPriceDate(LocalDateTime.now());
            return priceRepository.save(price);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PriceDTO getPricebyIdFilter(Long id , Integer filter) throws Exception {
        try {
            if (filter == 1) {
                return priceMapper.toDTO(priceRepository.findPriceIdByIdProduct(id));
            } else if (filter == 2) {
                return priceMapper.toDTO(priceRepository.findPriceIdByIdManufacturedProduct(id));
            } else {
                throw new Exception("Error al Buscar el precio con un filtro");
            }
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PriceDTO getOnlySellPrice(Long id , Integer filter) throws Exception {
        try {
            PriceDTO dto = getPricebyIdFilter(id,filter);
            dto.setCostPrice(null);
            return dto;
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    private void setIdRelationsIfExists(Long idItem,Integer filter,Price price) throws Exception {
        try {
            if (filter == 1) {
                price.setProduct(productRepository.findById(idItem).orElseThrow(() -> new Exception("El producto con id " + idItem + " no existe")));
            } else if (filter == 2) {
                price.setManufacturedProduct(manufacturedProductRepository.findById(idItem).orElseThrow(() -> new Exception("El producto manufacturado con id " + idItem + " no existe")));
            }else{
                throw new Exception("Error al relacionar el precio con un producto, manufacturado");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }


}
