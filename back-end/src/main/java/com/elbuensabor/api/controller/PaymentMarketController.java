package com.elbuensabor.api.controller;


import com.elbuensabor.api.dto.ItemPaymentMarketDTO;
import com.elbuensabor.api.dto.OrderDTO;
import com.elbuensabor.api.service.BillService;
import com.elbuensabor.api.service.PaymentMarketService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/payment")
public class PaymentMarketController {
    @Autowired
    private PaymentMarketService paymentMarketService;
    private static final String ERROR_MESSAGE = "{\"error\":\"Error. Por favor intente nuevamente.\"}";


    /**
     * crea una Preferencia.
     * URL: http://localhost:4000/api/payment/preference/creates
     *
     * @param dto dto de Order para crear un Preferencia en Mercado Pago
     * @return ResponseEntity con la preferencia guardada en el cuerpo de la respuesta.
     * HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PostMapping("/preference/create")
    public ResponseEntity<?> createPreference(@RequestBody OrderDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(paymentMarketService.savePreferenceID(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE + e.getMessage());
        }
    }



    /**
     * Edita una el atributo Paid de Order y el atributo paymentID de ItemPaymentMarket.
     * URL: http://localhost:4000/api/payment/paid/order
     *
     * @param dto dto de ItemPaymentMarket para editar pago.
     * @return ResponseEntity con la ItemPaymentMarketDto guardada en el cuerpo de la respuesta.
     * HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PutMapping("/paid/order")
    public ResponseEntity<?> paidOrderFromPaymentMarket(@RequestBody ItemPaymentMarketDTO dto) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(paymentMarketService.paidOrder(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE + e.getMessage());
        }
    }

    /**
     * Trae la Preferencia de La Orden a buscar.
     * URL: http://localhost:4000/api/payment/preference/order/{id}
     *
     * @param orderId id del Orden a buscar su Preferencia.
     * @return ResponseEntity con la Preferencia  en el cuerpo de la respuesta.
     * HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @GetMapping("/preference/order/{orderId}")
    public ResponseEntity<?> getPreferenceByOrderId(@PathVariable Long orderId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(paymentMarketService.getPreferenceByOrderId(orderId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE + e.getMessage());
        }
    }
}
