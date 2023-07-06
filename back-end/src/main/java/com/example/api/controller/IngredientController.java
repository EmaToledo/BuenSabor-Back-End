package com.example.api.controller;

import com.example.api.controller.impl.GenericControllerImpl;
import com.example.api.dtos.IngredientDTO;
import com.example.api.entity.Ingredient;
import com.example.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/ingredients")
public class IngredientController extends GenericControllerImpl<Ingredient, IngredientDTO> {

    @Autowired
    private ProductService service;

    private static final String ERROR_MESSAGE = "{\"error\":\"Error. Por favor intente nuevamente.\"}";

}
