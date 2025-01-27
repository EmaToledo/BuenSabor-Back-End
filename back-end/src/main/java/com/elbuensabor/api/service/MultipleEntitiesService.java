package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.ManufacturedProductDTO;
import com.elbuensabor.api.dto.ManufacturedRecipeDTO;
import com.elbuensabor.api.dto.RecipeDTO;
import com.elbuensabor.api.entity.ManufacturedProduct;
import org.springframework.web.multipart.MultipartFile;

public interface MultipleEntitiesService {
    ManufacturedProduct saveManufacturedWithRecipe(ManufacturedRecipeDTO dto) throws Exception;
}
