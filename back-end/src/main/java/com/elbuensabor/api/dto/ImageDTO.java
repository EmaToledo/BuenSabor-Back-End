package com.elbuensabor.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ImageDTO extends GenericDTO {
    private String name;
    private String base64;
    private Long productId;
    private Long manufacturedProductId;
    private Long userId;

}
