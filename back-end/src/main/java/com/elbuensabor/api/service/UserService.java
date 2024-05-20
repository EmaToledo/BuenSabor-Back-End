package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.Auth0UserDTO;
import com.elbuensabor.api.dto.UserDTO;
import com.elbuensabor.api.entity.Role;
import com.elbuensabor.api.entity.User;
import org.springframework.http.ResponseEntity;


public interface UserService extends GenericService<User, UserDTO, Long> {
    ResponseEntity<User> saveUser(Auth0UserDTO newUser) throws Exception;
    User blockedStatus(String userId,boolean blocked) throws Exception;
    Long getLoginsCount(String userId) throws Exception;
    User changeUserPassword(String userId,String password) throws Exception;
    User  assignUserToRole(String userId, Role role) throws Exception;
    User deleteRolesFromUser(String userId, String[] rolesId) throws Exception;
    String getUserApiAuth0(String userId ) throws Exception;
    boolean checkEmailExists(String email);
    void logIn(String userId) throws Exception;
    void logOut(String userId) throws Exception;

    Role getUserRolByAuth0Id(String userId) throws Exception;
    UserDTO getUserbyAuth0Id(String userId) throws Exception;
    String updateUserPicture(String userId,String picture) throws Exception;

}
