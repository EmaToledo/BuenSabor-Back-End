package com.elbuensabor.api.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auth0UserDTO {
    private String email;
    private String password;
    private boolean blocked;
    private List<RoleDTO> roles;
}
