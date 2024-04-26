package com.elbuensabor.api.dto;

import com.elbuensabor.api.entity.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Auth0UserDTO {
    private String email;
    private String password;
    private boolean blocked;
    private Role role ;
}
