package com.elbuensabor.api.dto;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class Auth0RoleDTO {
    private String id;
    private String name;
    private String description;
}
