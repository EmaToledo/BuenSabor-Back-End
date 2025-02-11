package com.elbuensabor.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class DenominationXImageDto extends GenericDTO{
    private Long itemID;
    private String itemDenomination;
    private String imageDenomination;
    private Long categoryId;
    private String type;
}
