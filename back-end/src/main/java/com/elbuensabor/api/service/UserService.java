package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.UserDTO;
import com.elbuensabor.api.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService extends GenericService<User, UserDTO, Long> {
    ResponseEntity<User> saveUser(UserDTO newUser) throws Exception;
    User blockedStatus(String userId,boolean blocked) throws Exception;
    Long getLoginsCount(String userId) throws Exception;
    User changeUserPassword(String userId,String password) throws Exception;
    User  assignUserToRole(String userId, String[] rolesId) throws Exception;
    User deleteRolesFromUser(String userId, String[] rolesId) throws Exception;
    String getUserApiAuth0(String userId ) throws Exception;
}
