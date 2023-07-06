package com.example.api.controller;

import com.example.api.controller.impl.GenericControllerImpl;
import com.example.api.dtos.CategoryDTO;
import com.example.api.entity.Category;
import com.example.api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/categories")
public class CategoryController extends GenericControllerImpl<Category, CategoryDTO> {

    @Autowired
    private CategoryService service;
    private static final String ERROR_MESSAGE = "{\"error\":\"Error. Por favor intente nuevamente.\"}";

    @Override
    @PostMapping(value = "/save")
    public ResponseEntity<?> save(@RequestBody CategoryDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.saveCategory(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    @Override
    @PutMapping("/{id}/update")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.updateCategory(id, dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    @PutMapping("/block/{id}")
    public ResponseEntity<?> blockUnlockCategory(@PathVariable Long id, @RequestParam boolean blocked) {
        try {
            Category category = service.blockUnlockCategory(id, blocked);
            return ResponseEntity.status(HttpStatus.OK).body(category);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    @GetMapping("/filter/unlocked")
    public ResponseEntity<?> findUnlockedCategories() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findUnlockedCategories());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    @GetMapping("/filter/all-product")
    public ResponseEntity<?> findAllProductCategories() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findAllProductCategories());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    @GetMapping("/filter/product")
    public ResponseEntity<?> findProductCategories() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findProductCategories());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    @GetMapping("/filter/manufactured-product")
    public ResponseEntity<?> findManufacturedProductCategories() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findManufacturedProductCategories());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }
    //http://localhost:4000/api/categories/filter/ingredient
    @GetMapping("/filter/ingredient")
    public ResponseEntity<?> findIngredientCategories() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findIngredientCategories());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

}
