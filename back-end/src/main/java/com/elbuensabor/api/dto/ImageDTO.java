package com.elbuensabor.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

@Data
@EqualsAndHashCode(callSuper=false)
public class ImageDTO extends GenericDTO {
    private String name;
    private Character relationType;
    private Long relationId;
}

