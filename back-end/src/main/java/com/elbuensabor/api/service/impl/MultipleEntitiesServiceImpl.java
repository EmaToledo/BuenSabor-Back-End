package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.dto.ManufacturedProductDTO;
import com.elbuensabor.api.dto.ManufacturedRecipeDTO;
import com.elbuensabor.api.dto.RecipeDTO;
import com.elbuensabor.api.entity.ManufacturedProduct;
import com.elbuensabor.api.service.ImageService;
import com.elbuensabor.api.service.ManufacturedProductService;
import com.elbuensabor.api.service.MultipleEntitiesService;
import com.elbuensabor.api.service.RecipeService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Getter
public class MultipleEntitiesServiceImpl implements MultipleEntitiesService {

    @Autowired
    private ManufacturedProductService manufacturedProductService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private ImageService imageService;

    @Transactional
    public ManufacturedProduct saveManufacturedWithRecipe(ManufacturedRecipeDTO dto) throws Exception {
        try {
            ManufacturedProduct manufacturedProduct = manufacturedProductService.saveManufacturedProduct(dto.getManufacturedProduct());
            dto.getRecipe().setManufacturedProductId(manufacturedProduct.getId());
            recipeService.saveRecipe( dto.getRecipe());
            return manufacturedProduct;
        } catch (Exception e) {
            throw new Exception("Error al guardar Producto Manufacturado Completo: " + e.getMessage(), e);
        }
    }
}
