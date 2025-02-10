package com.elbuensabor.api.controller;

import com.elbuensabor.api.service.Auth0TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api")
public class Auth0TestController {

    @Autowired
    private Auth0TokenService service;

    @GetMapping(value = "/get-token")
    public ResponseEntity<?> getToken() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getAuth0Token());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

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
    @PreAuthorize("hasAuthority('read:admin')")
    public ResponseEntity<?> adminOnlyEndPoint() {
        return ResponseEntity.status(HttpStatus.OK).body("ADMIN Endpoint Working fine !");
    }

    @GetMapping(value = "/cajero-only")
    @PreAuthorize("hasAuthority('read:cajero')")
    public ResponseEntity<?> cajeroOnlyEndPoint() {
        return ResponseEntity.status(HttpStatus.OK).body("CAJERO Endpoint Working fine !");
    }

    @GetMapping(value = "/concinero-only")
    @PreAuthorize("hasAuthority('read:cocinero')")
    public ResponseEntity<?> concineroOnlyEndPoint() {
        return ResponseEntity.status(HttpStatus.OK).body("COCINERO Endpoint Working fine !");
    }

    @GetMapping(value = "/delivery-only")
    @PreAuthorize("hasAuthority('read:delivery')")
    public ResponseEntity<?> deliveryOnlyEndPoint() {
        return ResponseEntity.status(HttpStatus.OK).body("DELIVERY Endpoint Working fine !");
    }

    @GetMapping(value = "/user-only")
    @PreAuthorize("hasAuthority('read:user')")
    public ResponseEntity<?> userOnlyEndPoint() {
        return ResponseEntity.status(HttpStatus.OK).body("USER Endpoint Working fine !");
    }
}
