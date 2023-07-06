package com.example.api.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Time;

@Data
@EqualsAndHashCode(callSuper = false)
public class ManufacturedProductDTO extends GenericDTO {

    private String denomination;
    private String description;
    private Time cookingTime;
    private Boolean availability;
    private String urlImage;
    private Long manufacturedProductCategoryID;

}
