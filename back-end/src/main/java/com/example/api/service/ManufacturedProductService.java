package com.example.api.service;

import com.example.api.dtos.ManufacturedProductDTO;
import com.example.api.entity.ManufacturedProduct;

public interface ManufacturedProductService extends GenericService<ManufacturedProduct, ManufacturedProductDTO, Long>{

    // Guarda un producto manufacturado
    ManufacturedProduct saveManufacturedProduct(ManufacturedProductDTO dto) throws Exception;

    // Actualiza un producto manufacturado
    ManufacturedProduct updateManufacturedProduct(Long id, ManufacturedProductDTO dto) throws Exception;

    // Bloquea un producto manufacturado
    ManufacturedProduct blockManufacturedProduct(Long id) throws Exception;

    // Desbloquea un producto manufacturado
    ManufacturedProduct unlockManufacturedProduct(Long id) throws Exception;

}
