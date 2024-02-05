package com.elbuensabor.api.controller;

import com.elbuensabor.api.controller.impl.GenericControllerImpl;
import com.elbuensabor.api.dto.CategoryDTO;
import com.elbuensabor.api.entity.Category;
import com.elbuensabor.api.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/categories")
public class CategoryController extends GenericControllerImpl<Category, CategoryDTO> {

    @Autowired
    private CategoryService service;
    private static final String ERROR_MESSAGE = "{\"error\":\"Error. Por favor intente nuevamente.\"}";

    /**
     * Guarda una categoría.
     * URL: http://localhost:4000/api/categories/save
     *
     * @param dto Objeto CategoryDTO que representa la categoría a guardar.
     * @return ResponseEntity con la categoría guardada en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PostMapping(value = "/save")
    public ResponseEntity<?> save(@RequestBody CategoryDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.saveCategory(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza una categoría.
     * URL: http://localhost:4000/api/categories/update/{id}
     *
     * @param id  ID de la categoría a actualizar.
     * @param dto Objeto CategoryDTO que contiene los datos actualizados de la categoría.
     * @return ResponseEntity con la categoría actualizada en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.updateCategory(id, dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Bloquea una categoría.
     * URL: http://localhost:4000/api/categories/block/{id}
     *
     * @param id ID de la categoría a bloquear.
     * @return ResponseEntity con la categoría bloqueada en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PutMapping("/block/{id}")
    public ResponseEntity<?> blockCategory(@PathVariable Long id) {
        try {
            Category category = service.blockCategory(id);
            return ResponseEntity.status(HttpStatus.OK).body(category);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Desbloquea una categoría.
     * URL: http://localhost:4000/api/categories/unlock/{id}
     *
     * @param id ID de la categoría a desbloquear.
     * @return ResponseEntity con la categoría desbloqueada en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PutMapping("/unlock/{id}")
    public ResponseEntity<?> unlockCategory(@PathVariable Long id) {
        try {
            Category category = service.unlockCategory(id);
            return ResponseEntity.status(HttpStatus.OK).body(category);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Obtiene todas las categorías desbloqueadas de un tipo específico.
     * URL: http://localhost:4000/api/categories/filter/unlocked/{categoryType}
     *
     * @param categoryType Tipo de categoría (ejemplo: "ingredient").
     * @return ResponseEntity con la lista de categorías desbloqueadas en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @GetMapping("/filter/unlocked/type/{categoryType}")
    public ResponseEntity<?> findUnlockedCategoriesByType(@PathVariable String categoryType) {
        try {
            List<CategoryDTO> unlockedCategories = service.findUnlockedCategoriesByType(categoryType);
            return ResponseEntity.status(HttpStatus.OK).body(unlockedCategories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Obtiene todas las categorías desbloqueadas excepto una categoría específica.
     * URL: http://localhost:4000/api/categories/filter/unlocked/{id}
     *
     * @param id ID de la categoría a excluir.
     * @return ResponseEntity con la lista de categorías desbloqueadas en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @GetMapping("/filter/unlocked/id/{id}")
    public ResponseEntity<?> findUnlockedCategoriesExceptId(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findUnlockedCategoriesByTypeExceptId(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Obtiene todas las categorías de ingredientes.
     * URL: http://localhost:4000/api/categories/filter/ingredient
     *
     * @return ResponseEntity con la lista de categorías de ingredientes en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @GetMapping("/filter/ingredient")
    public ResponseEntity<?> findIngredientCategories() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findIngredientCategories());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Obtiene todas las categorías de productos.
     * URL: http://localhost:4000/api/categories/filter/product
     *
     * @return ResponseEntity con la lista de categorías de productos en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @GetMapping("/filter/product")
    public ResponseEntity<?> findProductCategories() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findProductCategories());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Obtiene todas las categorías de productos manufacturados.
     * URL: http://localhost:4000/api/categories/filter/manufactured-product
     *
     * @return ResponseEntity con la lista de categorías de productos manufacturados en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @GetMapping("/filter/manufactured-product")
    public ResponseEntity<?> findManufacturedProductCategories() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findManufacturedProductCategories());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Obtiene todas las categorías generales.
     * URL: http://localhost:4000/api/categories/filter/general
     *
     * @return ResponseEntity con la lista de categorías generales en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @GetMapping("/filter/general")
    public ResponseEntity<?> findGeneralCategories() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.findGeneralCategories());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }
}
