package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.ManufacturedProductDTO;
import com.elbuensabor.api.entity.ManufacturedProduct;

public interface ManufacturedProductService extends GenericService<ManufacturedProduct, ManufacturedProductDTO, Long>{

    // Guarda un producto manufacturado
    ManufacturedProduct saveManufacturedProduct(ManufacturedProductDTO dto) throws Exception;

    // Actualiza un producto manufacturado
    ManufacturedProduct updateManufacturedProduct(Long id, ManufacturedProductDTO dto) throws Exception;

    // Bloquea un producto manufacturado
    ManufacturedProduct blockManufacturedProduct(Long id) throws Exception;

    // Desbloquea un producto manufacturado
    ManufacturedProduct unlockManufacturedProduct(Long id) throws Exception;

    // Obtiene desde la base de datos el ultimo id para
    Long getLastManufacturedProductId() throws Exception;

    // trae un producto manufacturado con su precio
    ManufacturedProductDTO getManufacturedProductComplete(Long id) throws Exception;

    ManufacturedProductDTO getManufacturedProductOnlySellPrice(Long id) throws Exception;
}
