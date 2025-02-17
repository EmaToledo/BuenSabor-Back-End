package com.elbuensabor.api.dto;

import com.elbuensabor.api.entity.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserDTO extends GenericDTO {
    private String auth0UserId;
    private String email;
    private String address;
    private String phone;
    private boolean blocked;
    private boolean logged;
    private Role role;
    private String name;
    private String lastName;
    private String apartment;
    private Long descuento;
}
