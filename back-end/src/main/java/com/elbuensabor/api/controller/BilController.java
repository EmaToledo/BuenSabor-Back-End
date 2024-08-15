package com.elbuensabor.api.controller;

import com.elbuensabor.api.controller.impl.GenericControllerImpl;
import com.elbuensabor.api.dto.BillDTO;
import com.elbuensabor.api.entity.Bill;
import com.elbuensabor.api.service.BillService;
import com.elbuensabor.api.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/bill")
public class BilController  extends GenericControllerImpl<Bill, BillDTO> {

    @Autowired
    private BillService service;
    @Autowired
    private EmailSenderService emailSenderService;
    private static final String ERROR_MESSAGE = "{\"error\":\"Error. Por favor intente nuevamente.\"}";

    /**
     * Guarda una factura.
     * URL: http://localhost:4000/api/bill/save
     *
     * @param dto Objeto BillDTO que representa la factura a guardar.
     * @return ResponseEntity con la factura guardada en el cuerpo de la respuesta.
     *         HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody BillDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.saveBill(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }
    /**
     * Trae una Factura de BD mediante el Pedido
     *URL: http://localhost:4000/api/bill/order/{orderId}
     *
     * @param orderId id del Pedido para buscar su Factura
     * @return La Factura encontrada
     * @throws Exception Si ocurre algún error durante el proceso de busqueda
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getBillByOrderID(@PathVariable Long orderId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getBillByOrderId(orderId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE);
        }
    }
}
