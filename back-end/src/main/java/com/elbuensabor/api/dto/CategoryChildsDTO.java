package com.elbuensabor.api.dto;

import com.elbuensabor.api.entity.Category;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class CategoryChildsDTO extends GenericDTO {
    private String denomination;
    private Character type;
    private Boolean availability;
    private Long categoryFatherId;
    private String categoryFatherDenomination;
    private List<Category> childs;
}
