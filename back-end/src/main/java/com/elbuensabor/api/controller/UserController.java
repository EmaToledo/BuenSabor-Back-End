package com.elbuensabor.api.controller;

import com.elbuensabor.api.controller.impl.GenericControllerImpl;
import com.elbuensabor.api.dto.UserDTO;
import com.elbuensabor.api.entity.Product;
import com.elbuensabor.api.entity.User;
import com.elbuensabor.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/users")
public class UserController extends GenericControllerImpl<User, UserDTO> {
    @Autowired
    private UserService service;
    private static final String ERROR_MESSAGE = "{\"error\":\"Error. Por favor intente nuevamente.\"}";

    /**
     * Guarda un usuario.
     * URL: http://localhost:4000/api/users/save
     *
     * @param dto Objeto UserDTO que representa el usuario a guardar.
     * @return ResponseEntity con el usuario guardado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PostMapping(value = "/save")
    public ResponseEntity<?> save(@RequestBody UserDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.saveUser(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Bloquea un usuario.
     * URL: http://localhost:4000/api/users/block/{id}
     *
     * @param id      ID del usuario a bloquear.
     * @return ResponseEntity con el usuario bloqueado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PatchMapping("/block/{id}")
    public ResponseEntity<?> blockUser(@PathVariable String id) {
        try {
            User user = service.blockedStatus(id,true);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }
    /**
     * Desbloquea un usuario.
     * URL: http://localhost:4000/api/users/unlock/{id}
     *
     * @param id      ID del usuario a desbloquear.
     * @return ResponseEntity con el usuario desbloqueado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */

    @PatchMapping("/unlock/{id}")
    public ResponseEntity<?> unlockUser(@PathVariable String id) {
        try {
            User user =  service.blockedStatus(id,false);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Obtiene un registro de la entidad User por su ID.
     *URL: http://localhost:4000/api/users/logins-count/{id}
     * @param id ID del usuario a buscar.
     * @return ResponseEntity con el usuario encontrado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o NOT_FOUND si hay un error.
     */
    @GetMapping("/logins-count/{id}")
    public ResponseEntity<?> getLoginsCount(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getLoginsCount(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERROR_MESSAGE);
        }
    }

    /**
     * Cambia la contraseña de un usuario.
     * URL: http://localhost:4000/api/users/change-password/{id}
     *
     * @param id      ID del usuario cuya contraseña se va a cambiar.
     * @param password Nueva contraseña del usuario.
     * @return ResponseEntity con el usuario actualizado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PatchMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(@PathVariable String id, @RequestBody String password) {
        try {
            User user = service.changeUserPassword(id, password);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }
    /**
     * Asigna un usuario a un rol específico.
     * URL: http://localhost:4000/api/users/roles/{id}
     *
     * @param id ID del usuario al que se asignará el rol.
     * @param requestBody contiene un Array de IDs de roles  a asignar al usuario.
     * @return ResponseEntity con el resultado de la asignación en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PostMapping(value = "/roles/{id}")
    public ResponseEntity<?> assignUserToRole(@PathVariable String id, @RequestBody Map<String, String[]> requestBody) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.assignUserToRole(id,requestBody.get("roles")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Elimina roles específicos de un usuario.
     * URL: http://localhost:4000/api/users/roles/{id}
     *
     * @param id ID del usuario del que se eliminarán los roles.
     * @param requestBody contiene un Array de IDs de roles a eliminar del usuario.
     * @return ResponseEntity con el resultado de la eliminación en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @DeleteMapping(value = "/roles/{id}")
    public ResponseEntity<?> deleteRolesFromUser(@PathVariable String id, @RequestBody Map<String, String[]> requestBody) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.deleteRolesFromUser(id, requestBody.get("roles")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Obtiene un registro de la entidad User por su ID.
     * URL: http://localhost:4000/api/users/auth0/{id}
     *
     * @param id ID del usuario a buscar.
     * @return ResponseEntity con el usuario encontrado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o NOT_FOUND si hay un error.
     */
    @GetMapping("/auth0/{id}")
    public ResponseEntity<?> getUserApiAuth0(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getUserApiAuth0(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERROR_MESSAGE);
        }
    }


}
