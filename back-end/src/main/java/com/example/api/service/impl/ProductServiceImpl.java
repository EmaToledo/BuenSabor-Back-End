package com.example.api.service.impl;

import com.example.api.dtos.ProductDTO;
import com.example.api.entity.Category;
import com.example.api.entity.Product;
import com.example.api.mapper.GenericMapper;
import com.example.api.mapper.ProductMapper;
import com.example.api.repository.IGenericRepository;
import com.example.api.repository.IProductRepository;
import com.example.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl extends GenericServiceImpl<Product, ProductDTO, Long> implements ProductService {

    @Autowired
    private IProductRepository productRepository;

    private final ProductMapper productMapper = ProductMapper.getInstance();

    public ProductServiceImpl(IGenericRepository<Product, Long> IGenericRepository, GenericMapper<Product, ProductDTO> genericMapper) {
        super(IGenericRepository, genericMapper);
    }

    @Override
    @Transactional
    public Product saveProduct(ProductDTO dto) throws Exception {
        try {
            Product product = productMapper.toEntity(dto);

            if (dto.getProductCategoryID() != null) {
                if (productRepository.existsById(dto.getProductCategoryID())) {
                    Category productCategory = productRepository.findById(dto.getProductCategoryID()).get().getProductCategory();
                    product.setProductCategory(productCategory);
                } else {
                    throw new Exception("La categoría del producto no existe");
                }
            }

            return productRepository.save(product);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }


    }

    @Override
    @Transactional
    public Product updateProduct(Long id, ProductDTO dto) throws Exception {
        try {
            Optional<Product> optionalProduct = productRepository.findById(id);

            if (optionalProduct.isEmpty()) {
                throw new Exception("El producto a actualizar no existe.");
            }

            Product product = optionalProduct.get();

            if (dto.getProductCategoryID() != null) {
                if (productRepository.existsById(dto.getProductCategoryID())) {
                    Category productCategory = productRepository.findById(dto.getProductCategoryID()).get().getProductCategory();
                    product.setProductCategory(productCategory);
                } else {
                    throw new Exception("La categoria producto no existe");
                }
            } else {
                product.setProductCategory(null);
            }
            product.setDenomination(dto.getDenomination());
            product.setDescription(dto.getDescription());
            product.setCooking_time(dto.getCooking_time());
            product.setAvailability(dto.getAvailability());
            return productRepository.save(product);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Product blockUnlockProduct(Long id, boolean availability) throws Exception {
        try {
            Product product = productRepository.findById(id).orElseThrow(() -> new Exception("Product not found"));
            product.setAvailability(availability);
            return productRepository.save(product);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<ProductDTO> findManufacturedProducts() throws Exception {
        try {
            List<Product> products = productRepository.findManufacturedProducts();
            return genericMapper.toDTOsList(products);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public List<ProductDTO> findPrePackagedProducts() throws Exception {
        try {
            List<Product> products = productRepository.findPrePackagedProducts();
            return genericMapper.toDTOsList(products);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
