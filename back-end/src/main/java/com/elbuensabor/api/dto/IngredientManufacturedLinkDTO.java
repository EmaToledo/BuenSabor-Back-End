package com.elbuensabor.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class IngredientManufacturedLinkDTO extends GenericDTO {

    private Long manufacturedProductID;
    private Long ingredientID;

}
