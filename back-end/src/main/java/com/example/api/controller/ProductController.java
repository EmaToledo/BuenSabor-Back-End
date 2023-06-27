package com.example.api.controller;

import com.example.api.controller.impl.GenericControllerImpl;
import com.example.api.dtos.ProductDTO;
import com.example.api.entity.Product;
import com.example.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/products")
public class ProductController extends GenericControllerImpl<Product, ProductDTO> {

    @Autowired
    private ProductService service;
    private static final String ERROR_MESSAGE = "{\"error\":\"Error. Por favor intente nuevamente.\"}";

    @GetMapping("/filter/manufactured")
    public ResponseEntity<?> findManufacturedProducts() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findManufacturedProducts());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    @GetMapping("/filter/pre-packaged")
    public ResponseEntity<?> findPrePackagedProducts() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findPrePackagedProducts());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

}
