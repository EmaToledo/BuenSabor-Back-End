package com.elbuensabor.api.controller;

import com.elbuensabor.api.controller.impl.GenericControllerImpl;
import com.elbuensabor.api.dto.PriceDTO;
import com.elbuensabor.api.entity.Price;
import com.elbuensabor.api.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/price")
public class PriceController extends GenericControllerImpl<Price, PriceDTO> {
    @Autowired
    private PriceService service;
    private static final String ERROR_MESSAGE = "{\"error\":\"Error. Por favor intente nuevamente.\"}";

    /**
     * Trae un Precio.
     * URL: http://localhost:4000/api/price/{filter}/{id}
     *
     * @param id id del producto o manufacturado a buscar.
     * @return ResponseEntity con el Precio Buscado en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @GetMapping("/{filter}/{id}")
    public ResponseEntity<?> getPriceByIdFilter(@PathVariable Long id,@PathVariable Integer filter) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(service.getPricebyIdFilter(id,filter));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    @GetMapping("sell/{filter}/{id}")
    public ResponseEntity<?> getSellPriceByIdFilter(@PathVariable Long id,@PathVariable Integer filter) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(service.getOnlySellPrice(id,filter));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }
}
