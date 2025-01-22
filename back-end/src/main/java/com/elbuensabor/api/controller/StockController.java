package com.elbuensabor.api.controller;

import com.elbuensabor.api.controller.impl.GenericControllerImpl;
import com.elbuensabor.api.dto.OrderDetailDTO;
import com.elbuensabor.api.dto.StockDTO;
import com.elbuensabor.api.entity.Stock;
import com.elbuensabor.api.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/stock")
public class StockController extends GenericControllerImpl<Stock, StockDTO> {

    @Autowired
    public StockService service;

    private static final String ERROR_MESSAGE = "{\"error\":\"Error. Por favor intente nuevamente.\"}";

    @PostMapping("/save-stock")
    public ResponseEntity<?> saveStock(@RequestBody StockDTO dto, @RequestParam Character type, @RequestParam Long relationId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.saveStock(dto, type, relationId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateStock(@PathVariable Long id, @RequestBody StockDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.update(id, dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }
}
