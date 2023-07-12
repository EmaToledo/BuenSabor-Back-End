package com.example.api.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CategoryDTO extends GenericDTO {

    private String denomination;
    private Character type;
    private Boolean availability;
    private Long categoryFatherId;
    private String categoryFatherDenomination;

}
