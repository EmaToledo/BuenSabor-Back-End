package com.elbuensabor.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api")
public class Auth0TestController {

    /**
     * Endpoint público sin restricciones de acceso.
     * URL: http://localhost:4000/api/public
     *
     * @return ResponseEntity con un mensaje indicando que el endpoint funciona correctamente.
     */
    @GetMapping(value = "/public")
    public ResponseEntity<?> publicEndpoint() {
        return ResponseEntity.status(HttpStatus.OK).body("Public Endpoint Working fine !");
    }

    /**
     * Endpoint privado que requiere autenticación para acceder.
     * URL: http://localhost:4000/api/private
     *
     * @return ResponseEntity con un mensaje indicando que el endpoint funciona correctamente.
     */
    @GetMapping(value = "/private")
    public ResponseEntity<?> privateEndpoint() {
        return ResponseEntity.status(HttpStatus.OK).body("Private Endpoint Working fine !");
    }

    /**
     * Endpoint exclusivo para administradores que requiere autorización.
     * URL: http://localhost:4000/api/admin-only
     *
     * @return ResponseEntity con un mensaje indicando que el endpoint funciona correctamente.
     */
    @GetMapping(value = "/admin-only")
    //@PreAuthorize("hasAuthority('read:admin-test')")
    public ResponseEntity<?> adminOnlyEndPoint() {
        return ResponseEntity.status(HttpStatus.OK).body("ADMIN Endpoint Working fine !");
    }
}
