package com.example.api.service;

import com.example.api.dtos.ProductDTO;
import com.example.api.entity.Product;

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
