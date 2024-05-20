package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.dto.ManufacturedProductDTO;
import com.elbuensabor.api.entity.Category;
import com.elbuensabor.api.entity.ManufacturedProduct;
import com.elbuensabor.api.mapper.GenericMapper;
import com.elbuensabor.api.mapper.ManufacturedProductMapper;
import com.elbuensabor.api.repository.ICategoryRepository;
import com.elbuensabor.api.repository.IImageRepository;
import com.elbuensabor.api.repository.IManufacturedProductRepository;
import com.elbuensabor.api.service.ManufacturedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManufacturedProductServiceImpl extends GenericServiceImpl<ManufacturedProduct, ManufacturedProductDTO, Long> implements ManufacturedProductService {

    @Autowired
    private IManufacturedProductRepository manufacturedProductRepository;
    @Autowired
    private ICategoryRepository categoryRepository;

    private final ManufacturedProductMapper manufacturedProductMapper = ManufacturedProductMapper.getInstance();

    /**
     * Constructor de la clase.
     *
     * @param IGenericRepository Repositorio genérico utilizado para acceder a los datos de la entidad ManufacturedProduct.
     * @param genericMapper      Mapeador genérico utilizado para convertir entre la entidad ManufacturedProduct y el DTO ManufacturedProductDTO.
     */
    public ManufacturedProductServiceImpl(com.elbuensabor.api.repository.IGenericRepository<ManufacturedProduct, Long> IGenericRepository, GenericMapper<ManufacturedProduct, ManufacturedProductDTO> genericMapper) {
        super(IGenericRepository, genericMapper);
    }

    /**
     * Guarda un producto manufacturado.
     *
     * @param dto DTO que representa el producto manufacturado a guardar.
     * @return Producto manufacturado guardado.
     * @throws Exception si ocurre algún error durante la operación o si la categoría del producto manufacturado no existe.
     */
    @Override
    @Transactional
    public ManufacturedProduct saveManufacturedProduct(ManufacturedProductDTO dto) throws Exception {
        try {
            ManufacturedProduct manufacturedProduct = manufacturedProductMapper.toEntity(dto);

            setManufacturedProductCategoryIfExists(dto.getManufacturedProductCategoryID(), manufacturedProduct);

            return manufacturedProductRepository.save(manufacturedProduct);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Actualiza un producto manufacturado.
     *
     * @param id  ID del producto manufacturado a actualizar.
     * @param dto DTO que contiene los datos actualizados del producto manufacturado.
     * @return Producto manufacturado actualizado.
     * @throws Exception si el producto manufacturado a actualizar no existe, si la categoría del producto manufacturado no existe
     *                   o si ocurre algún error durante la operación.
     */
    @Override
    @Transactional
    public ManufacturedProduct updateManufacturedProduct(Long id, ManufacturedProductDTO dto) throws Exception {
        try {
            ManufacturedProduct manufacturedProduct = manufacturedProductRepository.findById(id)
                    .orElseThrow(() -> new Exception("El producto manufacturado a actualizar no existe."));

            setManufacturedProductCategoryIfExists(dto.getManufacturedProductCategoryID(), manufacturedProduct);

            manufacturedProduct.setDenomination(dto.getDenomination());
            manufacturedProduct.setDescription(dto.getDescription());
            manufacturedProduct.setCookingTime(dto.getCookingTime());
            manufacturedProduct.setAvailability(dto.getAvailability());

            return manufacturedProductRepository.save(manufacturedProduct);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Asigna la categoría del producto manufacturado si existe en la base de datos, o la establece como null si no se proporciona una categoría.
     *
     * @param categoryId ID de la categoría del producto manufacturado.
     * @param manufactured producto manufacturado al cual se le asignará la categoría.
     * @throws Exception si la categoría del producto manufacturado no existe en la base de datos.
     */
    private void setManufacturedProductCategoryIfExists(Long categoryId, ManufacturedProduct manufactured) throws Exception {
        if (categoryId != null) {
            Category manufacturedProductCategory = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new Exception("La categoria del ingrediente no existe"));
            manufactured.setManufacturedProductCategory(manufacturedProductCategory);
        } else {
            manufactured.setManufacturedProductCategory(null);
        }
    }

    /**
     * Bloquea un producto manufacturado cambiando su disponibilidad.
     *
     * @param id           ID del producto manufacturado a bloquear.
     * @return Producto manufacturado actualizado.
     * @throws Exception si el producto manufacturado no existe o si ocurre algún error durante la operación.
     */
    @Override
    @Transactional
    public ManufacturedProduct blockManufacturedProduct(Long id) throws Exception {
        try {
            ManufacturedProduct manufacturedProduct = manufacturedProductRepository.findById(id).orElseThrow(() -> new Exception("ManufacturedProduct not found"));
            manufacturedProduct.setAvailability(false);

            return manufacturedProductRepository.save(manufacturedProduct);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Desbloquea un producto manufacturado cambiando su disponibilidad.
     *
     * @param id           ID del producto manufacturado a desbloquear.
     * @return Producto manufacturado actualizado.
     * @throws Exception si el producto manufacturado no existe o si ocurre algún error durante la operación.
     */
    @Override
    @Transactional
    public ManufacturedProduct unlockManufacturedProduct(Long id) throws Exception {
        try {
            ManufacturedProduct manufacturedProduct = manufacturedProductRepository.findById(id).orElseThrow(() -> new Exception("ManufacturedProduct not found"));
            manufacturedProduct.setAvailability(true);

            return manufacturedProductRepository.save(manufacturedProduct);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Long getLastManufacturedProductId() throws Exception {
        try {
            Long lastID = manufacturedProductRepository.findLastManufacturedProductId();

            return lastID;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }


}
