package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.Auth0RoleDTO;
import com.elbuensabor.api.dto.RoleDTO;
import com.elbuensabor.api.entity.Role;

public interface RoleService extends GenericService<Role, RoleDTO, Long> {
    Auth0RoleDTO[] getUserRolesAuth0(String userId) throws Exception;


}
