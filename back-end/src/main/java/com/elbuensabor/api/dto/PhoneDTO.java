package com.elbuensabor.api.dto;

import com.elbuensabor.api.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PhoneDTO extends GenericDTO  {
    private Long phone;
}
