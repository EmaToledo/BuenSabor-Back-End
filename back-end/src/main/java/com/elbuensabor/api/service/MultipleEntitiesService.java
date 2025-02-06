package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.*;
import com.elbuensabor.api.entity.Ingredient;
import com.elbuensabor.api.entity.ManufacturedProduct;
import com.elbuensabor.api.entity.Product;
import org.springframework.web.multipart.MultipartFile;

public interface MultipleEntitiesService {
    ManufacturedProduct saveManufacturedWithRecipe(ManufacturedRecipeDTO dto) throws Exception;
    Ingredient saveIngredientWithStock(IngredientDTO dto, StockDTO stockDTO) throws Exception;
    Product saveProductWithStock(ProductDTO dto, StockDTO stockDTO) throws Exception;


}
