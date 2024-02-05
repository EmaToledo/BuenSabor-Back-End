package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.RoleDTO;
import com.elbuensabor.api.dto.UserDTO;
import com.elbuensabor.api.entity.Role;
import com.elbuensabor.api.entity.User;

public interface RoleService extends GenericService<Role, RoleDTO, Long> {
    RoleDTO[] getUserRolesAuth0(String userId) throws Exception;



}
