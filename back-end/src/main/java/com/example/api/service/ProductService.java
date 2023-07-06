package com.example.api.service;

import com.example.api.dtos.ProductDTO;
import com.example.api.entity.Product;

import java.util.List;

public interface ProductService extends GenericService<Product, ProductDTO, Long> {
    Product saveProduct(ProductDTO dto) throws Exception;

    Product updateProduct(Long id, ProductDTO dto) throws Exception;

    Product blockUnlockProduct(Long id, boolean blocked) throws Exception;

    List<ProductDTO> findManufacturedProducts() throws Exception;

    List<ProductDTO> findPrePackagedProducts() throws Exception;
}
