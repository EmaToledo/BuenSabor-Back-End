package com.example.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api")
public class Auth0TestController {

    //localhost:8080/api/public
    //localhost:4000/api/public
	@GetMapping(value = "/public")
    public ResponseEntity<?> publicEndpoint() {
        return ResponseEntity.status(HttpStatus.OK).body("Public Endpoint Working fine !");
    }

    //localhost:8080/api/private
    //http://localhost:4000/api/private
    @GetMapping(value = "/private")
    public ResponseEntity<?> privateEndpoint() {
        return ResponseEntity.status(HttpStatus.OK).body("Private Endpoint Working fine !");
    }
    //localhost:8080/api/admin-only
    //http://localhost:4000/api/admin-only
    @GetMapping(value = "/admin-only")
   // @PreAuthorize("hasAuthority('read:admin-test')")
    public ResponseEntity<?> adminOnlyEndPoint() {
        return ResponseEntity.status(HttpStatus.OK).body("ADMIN Endpoint Working fine !");
    }
}