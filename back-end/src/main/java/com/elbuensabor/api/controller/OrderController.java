package com.elbuensabor.api.controller;

import com.elbuensabor.api.controller.impl.GenericControllerImpl;
import com.elbuensabor.api.dto.CategoryDTO;
import com.elbuensabor.api.dto.OrderDTO;
import com.elbuensabor.api.entity.Category;
import com.elbuensabor.api.entity.Order;
import com.elbuensabor.api.service.CategoryService;
import com.elbuensabor.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/order")
public class OrderController extends GenericControllerImpl<Order, OrderDTO> {
    @Autowired
    private OrderService service;
    private static final String ERROR_MESSAGE = "{\"error\":\"Error. Por favor intente nuevamente.\"}";


    /**
     * Trae todas la ordenes con sus relaciones
     * URL: http://localhost:4000/api/order/all/complete
     *
     * @return ResponseEntity con la lista de ordenes con sus relaciones  en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @GetMapping("/all/complete")
    public ResponseEntity<?> getAllOrders() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getAllOrders());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Trae todas la ordenes del Usuario relacionados por el id
     * URL: http://localhost:4000/api/order/all/{id}
     * @param  id  id del Usuario para buscar orden relacionado
     * @return ResponseEntity con la lista de ordenes con sus relaciones  en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @GetMapping("/all/{id}")
    public ResponseEntity<?> getAllOrdersByUserId(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getAllOrdersByUserId(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Trae todas la ordenes con sus relaciones
     * URL: http://localhost:4000/api/order/complete/{id}
     * @param id  id del orden a buscar
     * @return ResponseEntity con la Orden  con sus relaciones  en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @GetMapping("/complete/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getOrder(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Guarda Orden en BD
     * URL: http://localhost:4000/api/order/saveComplete
     * @param dto Cuerpo del Orden a editar
     * @return ResponseEntity con Orden guardado , con sus relaciones  en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PostMapping("/saveComplete")
    public ResponseEntity<?> saveOrder(@RequestBody OrderDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.saveOrder(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Edita Orden
     * URL: http://localhost:4000/api/order/updateComplete/{id}
     * @param id  id del Orden a buscar
     * @param dto Cuerpo del Orden a editar
     * @return ResponseEntity con Orden Editado, con sus relaciones  en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PutMapping("/updateComplete/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody OrderDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.updateOrder(id,dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * Cambia el estado cancel del la Orden a true
     * URL: http://localhost:4000/api/order/cencel/{id}
     * @param id  id del Orden a buscar
     * @param dto Cuerpo del Orden a editar
     * @return ResponseEntity con la lista de ordenes con sus relaciones  en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PutMapping("/cencel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id,@RequestBody OrderDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.cancelOrder(id,dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

    /**
     * trae una lista de ItemsDTO
     * URL: http://localhost:4000/api/order/items
     * @return ResponseEntity con la lista de ItemsDTO en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @GetMapping("/items")
    public ResponseEntity<?> getItemsList() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getItemsList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }

}
