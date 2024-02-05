package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.ProductDTO;
import com.elbuensabor.api.entity.Product;

public interface ProductService extends GenericService<Product, ProductDTO, Long> {

    // Guarda un producto
    Product saveProduct(ProductDTO dto) throws Exception;

    // Actualiza un producto
    Product updateProduct(Long id, ProductDTO dto) throws Exception;

    // Bloquea un producto
    Product blockProduct(Long id) throws Exception;

    // Desbloquea un producto
    Product unlockProduct(Long id) throws Exception;

}
