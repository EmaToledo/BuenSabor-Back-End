package com.elbuensabor.api.controller;

import com.elbuensabor.api.controller.impl.GenericControllerImpl;
import com.elbuensabor.api.dto.Auth0UserDTO;
import com.elbuensabor.api.dto.RoleDTO;
import com.elbuensabor.api.dto.UserDTO;
import com.elbuensabor.api.entity.Product;
import com.elbuensabor.api.entity.Role;
import com.elbuensabor.api.entity.User;
import com.elbuensabor.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/users")
public class UserController extends GenericControllerImpl<User, UserDTO> {
    @Autowired
    private UserService service;
    private static final String ERROR_MESSAGE = "{\"error\":\"Error. Por favor intente nuevamente.\"}";

    /**
     * Guarda un usuario en Auth0 y en BD.
     * URL: http://localhost:4000/api/users/saveAuth0User
     *
     * @param dto Objeto Auth0UserDTO que representa el usuario a guardar.
     * @return ResponseEntity con el usuario guardado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PostMapping(value = "/saveAuth0User")
    public ResponseEntity<?> save(@RequestBody Auth0UserDTO dto) {
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
     * @param requestBody Nueva contraseña del usuario.
     * @return ResponseEntity con el usuario actualizado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PatchMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(@PathVariable String id, @RequestBody Map<String, String> requestBody) {
        try {
            User user = service.changeUserPassword(id, requestBody.get("password"));
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
    public ResponseEntity<?> assignUserToRole(@PathVariable String id, @RequestBody Role requestBody) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.assignUserToRole(id,requestBody));
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
    /**
     * Obtiene un registro de la entidad User por su ID.
     * URL: http://localhost:4000/api/users/check-email/{email}
     *
     * @param email email del usuario a buscar.
     * @return ResponseEntity con booleano true si el usuario es encontrado, caso contrario false.
     *         HttpStatus OK si la operación se realiza correctamente, o NOT_FOUND si hay un error.
     */
    @GetMapping("/check-email/{email}")
    public ResponseEntity<?> checkEmailExists(@PathVariable String email) {
        return ResponseEntity.ok(service.checkEmailExists(email));
    }
    /**
     * Cambia el esta logged del usuario a true.
     * URL: http://localhost:4000/api/users/log-in/{id}
     *
     * @param id ID del usuario a buscar.
     * @return ResponseEntity con el usuario encontrado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o NOT_FOUND si hay un error.
     */
    @PutMapping("/log-in/{id}")
    public ResponseEntity<Void> userLogIn(@PathVariable String id){
        try {
            service.logIn(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Cambia el esta logged del usuario a false.
     * URL: http://localhost:4000/api/users/log-out/{id}
     *
     * @param id ID del usuario a buscar.
     * @return ResponseEntity con el usuario encontrado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o NOT_FOUND si hay un error.
     */
    @PutMapping("/log-out/{id}")
    public ResponseEntity<Void> userLogOut(@PathVariable String id){
        try {
            service.logOut(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene el role de la entidad User por su ID.
     * URL: http://localhost:4000/api/users/role/{id}
     *
     * @param id ID del usuario para buscar su rol.
     * @return ResponseEntity con el role encontrado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o NOT_FOUND si hay un error.
     */
    @GetMapping("/role/{id}")
    public ResponseEntity<?> getRolUser(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getUserRolByAuth0Id(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERROR_MESSAGE);
        }
    }
    /**
     * Obtiene la entidad User por  ID Auth0.
     * URL: http://localhost:4000/api/users/bd/{id}
     *
     * @param id ID (Auth0) del usuario para buscar.
     * @return ResponseEntity con el usuario encontrado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o NOT_FOUND si hay un error.
     */
    @GetMapping("/bd/{id}")
    public ResponseEntity<?> getUserbyAuth0IdFromBD(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getUserbyAuth0Id(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERROR_MESSAGE);
        }
    }

    /**
     * Edita la Imgaten el  User por su ID.
     * URL: http://localhost:4000/api/users/picture/{id}
     *
     * @param id ID del usuario a buscar.
     * @return ResponseEntity con el usuario encontrado y editado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o NOT_FOUND si hay un error.
     */
    @PatchMapping("/picture/{id}")
    public ResponseEntity<?> updateUserPicture(@PathVariable String id,@RequestBody Map<String, String> requestBody) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.updateUserPicture(id,requestBody.get("picture")));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ERROR_MESSAGE);
        }
    }


}
