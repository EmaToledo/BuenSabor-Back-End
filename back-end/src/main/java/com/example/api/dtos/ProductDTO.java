package com.example.api.dtos;

import lombok.Data;

import java.sql.Time;

@Data
public class ProductDTO extends GenericDTO {

    private String denomination;

    private String description;

    private Time cooking_time;

    private Boolean availability;

    private Long productCategoryID;

}
