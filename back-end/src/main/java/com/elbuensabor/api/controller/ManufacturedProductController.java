package com.elbuensabor.api.controller;

import com.elbuensabor.api.controller.impl.GenericControllerImpl;
import com.elbuensabor.api.dtos.ManufacturedProductDTO;
import com.elbuensabor.api.entity.ManufacturedProduct;
import com.elbuensabor.api.service.ManufacturedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/manufactured-products")
public class ManufacturedProductController extends GenericControllerImpl<ManufacturedProduct, ManufacturedProductDTO> {

    @Autowired
    private ManufacturedProductService service;

    private static final String ERROR_MESSAGE = "{\"error\":\"Error. Por favor intente nuevamente.\"}";

    /**
     * Guarda un producto manufacturado.
     * URL: http://localhost:4000/api/manufactured-products/save
     *
     * @param dto Objeto ManufacturedProductDTO que representa el producto manufacturado a guardar.
     * @return ResponseEntity con el producto manufacturado guardado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PostMapping(value = "/save")
    public ResponseEntity<?> save(@RequestBody ManufacturedProductDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.saveManufacturedProduct(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza un producto manufacturado.
     * URL: http://localhost:4000/api/manufactured-products/update/{id}
     *
     * @param id  ID del producto manufacturado a actualizar.
     * @param dto Objeto ManufacturedProductDTO que contiene los datos actualizados del producto manufacturado.
     * @return ResponseEntity con el producto manufacturado actualizado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ManufacturedProductDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.updateManufacturedProduct(id, dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Bloquea un producto manufacturado.
     * URL: http://localhost:4000/api/manufactured-products/block/{id}
     *
     * @param id      ID del producto manufacturado a bloquear.
     * @return ResponseEntity con el producto manufacturado bloqueado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PutMapping("/block/{id}")
    public ResponseEntity<?> blockManufacturedProduct(@PathVariable Long id) {
        try {
            ManufacturedProduct manufacturedProduct = service.blockManufacturedProduct(id);
            return ResponseEntity.status(HttpStatus.OK).body(manufacturedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Desbloquea un producto manufacturado.
     * URL: http://localhost:4000/api/manufactured-products/unlock/{id}
     *
     * @param id      ID del producto manufacturado a desbloquear.
     * @return ResponseEntity con el producto manufacturado desbloqueado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PutMapping("/unlock/{id}")
    public ResponseEntity<?> unlockManufacturedProduct(@PathVariable Long id) {
        try {
            ManufacturedProduct manufacturedProduct = service.unlockManufacturedProduct(id);
            return ResponseEntity.status(HttpStatus.OK).body(manufacturedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }
    
    @GetMapping("/lastID")
    public ResponseEntity<?> getLastManufacturedProductId() {
        try{
            Long lastID = service.getLastManufacturedProductId();
            return ResponseEntity.status(HttpStatus.OK).body(lastID);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

}
