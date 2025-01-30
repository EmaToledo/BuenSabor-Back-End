package com.elbuensabor.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ManufacturedRecipeDTO {
    ManufacturedProductDTO manufacturedProduct;
    RecipeDTO recipe;
}
