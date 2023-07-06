package com.example.api.controller;

import com.example.api.dtos.GenericDTO;
import com.example.api.entity.GenericEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;

public interface GenericController<Entity extends GenericEntity, Dto extends GenericDTO, ID extends Serializable> {
    public ResponseEntity<?> getAll();

    public ResponseEntity<?> getOneById(@PathVariable ID id);

    public ResponseEntity<?> save(@RequestBody Dto dto);

    public ResponseEntity<?> update(@PathVariable ID id, @RequestBody Dto dto);

    public ResponseEntity<?> delete(@PathVariable ID id);
}
