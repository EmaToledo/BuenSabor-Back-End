package com.elbuensabor.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Time;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO extends GenericDTO{
    private String denomination;
    private Time cookingTime;
//    private Integer quantity;
//    private Double discount;
    private Long idFatherCategory;
    private Double price;
    private Long idCategory;
}
