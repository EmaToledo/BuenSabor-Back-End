package com.elbuensabor.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class ItemListDTO {

    private List<DenominationXImageDto> productDTOList;
    private List<DenominationXImageDto> manufacturedProductDTOList;

}
