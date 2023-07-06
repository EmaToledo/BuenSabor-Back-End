package com.example.api.service.impl;

import com.example.api.dtos.ManufacturedProductDTO;
import com.example.api.entity.Category;
import com.example.api.entity.ManufacturedProduct;
import com.example.api.mapper.GenericMapper;
import com.example.api.mapper.ManufacturedProductMapper;
import com.example.api.repository.IManufacturedProductRepository;
import com.example.api.service.ManufacturedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ManufacturedProductServiceImpl extends GenericServiceImpl<ManufacturedProduct, ManufacturedProductDTO, Long> implements ManufacturedProductService {

    @Autowired
    private IManufacturedProductRepository manufacturedProductRepository;

    private final ManufacturedProductMapper manufacturedProductMapper = ManufacturedProductMapper.getInstance();

    /**
     * Constructor de la clase.
     *
     * @param IGenericRepository Repositorio genérico utilizado para acceder a los datos de la entidad ManufacturedProduct.
     * @param genericMapper      Mapeador genérico utilizado para convertir entre la entidad ManufacturedProduct y el DTO ManufacturedProductDTO.
     */
    public ManufacturedProductServiceImpl(com.example.api.repository.IGenericRepository<ManufacturedProduct, Long> IGenericRepository, GenericMapper<ManufacturedProduct, ManufacturedProductDTO> genericMapper) {
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

            if (dto.getManufacturedProductCategoryID() != null) {
                if (manufacturedProductRepository.existsById(dto.getManufacturedProductCategoryID())) {
                    Category manufacturedProductCategory = manufacturedProductRepository.findById(dto.getManufacturedProductCategoryID()).get().getManufacturedProductCategory();
                    manufacturedProduct.setManufacturedProductCategory(manufacturedProductCategory);
                } else {
                    throw new Exception("La categoría del manufactured product no existe");
                }
            }

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
            Optional<ManufacturedProduct> optionalManufacturedProduct = manufacturedProductRepository.findById(id);

            if (optionalManufacturedProduct.isEmpty()) {
                throw new Exception("El manufactured product a actualizar no existe.");
            }

            ManufacturedProduct manufacturedProduct = optionalManufacturedProduct.get();

            if (dto.getManufacturedProductCategoryID() != null) {
                if (manufacturedProductRepository.existsById(dto.getManufacturedProductCategoryID())) {
                    Category manufacturedProductCategory = manufacturedProductRepository.findById(dto.getManufacturedProductCategoryID()).get().getManufacturedProductCategory();
                    manufacturedProduct.setManufacturedProductCategory(manufacturedProductCategory);
                } else {
                    throw new Exception("La categoria manufactured product no existe");
                }
            } else {
                manufacturedProduct.setManufacturedProductCategory(null);
            }
            manufacturedProduct.setDenomination(dto.getDenomination());
            manufacturedProduct.setDescription(dto.getDescription());
            manufacturedProduct.setCookingTime(dto.getCookingTime());
            manufacturedProduct.setAvailability(dto.getAvailability());
            manufacturedProduct.setUrlImage(dto.getUrlImage());

            return manufacturedProductRepository.save(manufacturedProduct);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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

}
