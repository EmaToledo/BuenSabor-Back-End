package com.elbuensabor.api.controller;

import com.elbuensabor.api.controller.impl.GenericControllerImpl;
import com.elbuensabor.api.dto.RoleDTO;
import com.elbuensabor.api.entity.Role;
import com.elbuensabor.api.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/roles")
public class RoleController extends GenericControllerImpl<Role, RoleDTO> {

    @Autowired
    public RoleService service;
    private static final String ERROR_MESSAGE = "{\"error\":\"Error. Por favor intente nuevamente.\"}";

    /**
     * Obtiene un registro de Roles de la  entidad User por su ID.
     * URL: http://localhost:4000/api/roles/auth0/{id}
     *
     * @param id ID del usuario a buscar.
     * @return ResponseEntity con lista roles del usuario encontrado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o NOT_FOUND si hay un error.
     */
    @GetMapping("/auth0/{id}")
    public ResponseEntity<?> getUserApiAuth0(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getUserRolesAuth0(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERROR_MESSAGE);
        }
    }


}
