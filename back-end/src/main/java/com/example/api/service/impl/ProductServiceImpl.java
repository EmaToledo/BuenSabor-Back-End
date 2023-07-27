package com.example.api.service.impl;

import com.example.api.dtos.CategoryDTO;
import com.example.api.dtos.ProductDTO;
import com.example.api.entity.Category;
import com.example.api.entity.Product;
import com.example.api.mapper.GenericMapper;
import com.example.api.mapper.ProductMapper;
import com.example.api.repository.ICategoryRepository;
import com.example.api.repository.IProductRepository;
import com.example.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductServiceImpl extends GenericServiceImpl<Product, ProductDTO, Long> implements ProductService {

    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private ICategoryRepository categoryRepository;

    private final ProductMapper productMapper = ProductMapper.getInstance();

    /**
     * Constructor de la clase.
     *
     * @param IGenericRepository Repositorio genérico utilizado para acceder a los datos de la entidad Product.
     * @param genericMapper      Mapeador genérico utilizado para convertir entre la entidad Product y el DTO ProductDTO.
     */
    public ProductServiceImpl(com.example.api.repository.IGenericRepository<Product, Long> IGenericRepository, GenericMapper<Product, ProductDTO> genericMapper) {
        super(IGenericRepository, genericMapper);
    }

    /**
     * Guarda un producto.
     *
     * @param dto DTO que representa el producto a guardar.
     * @return Producto guardado.
     * @throws Exception si ocurre algún error durante la operación o si la categoría del producto no existe.
     */
    @Override
    @Transactional
    public Product saveProduct(ProductDTO dto) throws Exception {
        try {
            Product product = productMapper.toEntity(dto);

            setProductCategoryIfExists(dto.getProductCategoryID(), product);

            return productRepository.save(product);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Actualiza un producto.
     *
     * @param id  ID del producto a actualizar.
     * @param dto DTO que contiene los datos actualizados del producto.
     * @return Producto actualizado.
     * @throws Exception si el producto a actualizar no existe, si la categoría del producto no existe
     *                   o si ocurre algún error durante la operación.
     */
    @Override
    @Transactional
    public Product updateProduct(Long id, ProductDTO dto) throws Exception {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new Exception("El producto a actualizar no existe."));

            setProductCategoryIfExists(dto.getProductCategoryID(), product);

            product.setDenomination(dto.getDenomination());
            product.setAvailability(dto.getAvailability());

            return productRepository.save(product);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Asigna la categoría del producto si existe en la base de datos, o la establece como null si no se proporciona una categoría.
     *
     * @param categoryId ID de la categoría del producto.
     * @param product    Producto al cual se le asignará la categoría.
     * @throws Exception si la categoría del producto no existe en la base de datos.
     */
    private void setProductCategoryIfExists(Long categoryId, Product product) throws Exception {
        if (categoryId != null) {
            Category productCategory = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new Exception("La categoria del producto no existe"));
            product.setProductCategory(productCategory);
        } else {
            product.setProductCategory(null);
        }
    }

    /**
     * Bloquea o desbloquea un producto cambiando su disponibilidad.
     *
     * @param id           ID del producto a bloquear.
     * @return Producto actualizado.
     * @throws Exception si el producto no existe o si ocurre algún error durante la operación.
     */
    @Override
    @Transactional
    public Product blockProduct(Long id) throws Exception {
        try {
            Product product = productRepository.findById(id).orElseThrow(() -> new Exception("Product not found"));
            product.setAvailability(false);

            return productRepository.save(product);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Bloquea o desbloquea un producto cambiando su disponibilidad.
     *
     * @param id           ID del producto a bloquear.
     * @return Producto actualizado.
     * @throws Exception si el producto no existe o si ocurre algún error durante la operación.
     */
    @Override
    @Transactional
    public Product unlockProduct(Long id) throws Exception {
        try {
            Product product = productRepository.findById(id).orElseThrow(() -> new Exception("Product not found"));
            product.setAvailability(true);

            return productRepository.save(product);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
