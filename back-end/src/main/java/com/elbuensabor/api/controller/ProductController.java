package com.elbuensabor.api.controller;

import com.elbuensabor.api.controller.impl.GenericControllerImpl;
import com.elbuensabor.api.dtos.ProductDTO;
import com.elbuensabor.api.entity.Product;
import com.elbuensabor.api.service.ProductService;
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

    /**
     * Guarda un producto.
     * URL: http://localhost:4000/api/products/save
     *
     * @param dto Objeto ProductDTO que representa el producto a guardar.
     * @return ResponseEntity con el producto guardado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PostMapping(value = "/save")
    public ResponseEntity<?> save(@RequestBody ProductDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.saveProduct(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza un producto.
     * URL: http://localhost:4000/api/products/update/{id}
     *
     * @param id  ID del producto a actualizar.
     * @param dto Objeto ProductDTO que contiene los datos actualizados del producto.
     * @return ResponseEntity con el producto actualizado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.updateProduct(id, dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Bloquea un producto.
     * URL: http://localhost:4000/api/products/block/{id}
     *
     * @param id      ID del producto a bloquear.
     * @return ResponseEntity con el producto bloqueado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PutMapping("/block/{id}")
    public ResponseEntity<?> blockProduct(@PathVariable Long id) {
        try {
            Product product = service.blockProduct(id);
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Bloquea un producto.
     * URL: http://localhost:4000/api/products/unlock/{id}
     *
     * @param id      ID del producto a desbloquear.
     * @return ResponseEntity con el producto desbloqueado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PutMapping("/unlock/{id}")
    public ResponseEntity<?> unlockProduct(@PathVariable Long id) {
        try {
            Product product = service.unlockProduct(id);
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

}
