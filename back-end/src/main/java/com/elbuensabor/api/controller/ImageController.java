package com.elbuensabor.api.controller;

import com.elbuensabor.api.controller.impl.GenericControllerImpl;
import com.elbuensabor.api.dto.ImageDTO;
import com.elbuensabor.api.entity.Image;
import com.elbuensabor.api.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "api/images")
public class ImageController extends GenericControllerImpl<Image, ImageDTO> {

    @Autowired
    private ImageService imageService;

    private static final String ERROR_MESSAGE = "{\"error\":\"Error. Por favor intente nuevamente.\"}";

    /**
     * Guarda una imagen.
     * URL: http://localhost:4000/api/images/save
     *
     * @param filter  filtro para saber si la imagen es de user,product o Manufactured product
     * @param id   del user,product o Manufactured product relacionado a la imagen
     * @param imageFile MultipartFile imageFile que representa la imagen a guardar.
     * @return ResponseEntity con la image guardada en el cuerpo de la respuesta.
     * HttpStatus OK si la operación se realiza correctamente, o BAD_REQUEST si hay un error.
     */
    @PostMapping(value = "/save-image/{filter}/{id}")
    public ResponseEntity<?> saveImage(@PathVariable Long id,@PathVariable Long filter,@RequestParam("imageFile") MultipartFile imageFile) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.saveImageFile(filter,id, imageFile));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE + e.getMessage());
        }
    }

    @PostMapping(value = "/replace-image/{filter}/{idFilter}/{idImage}")
    public ResponseEntity<?> replaceImage(@PathVariable Long idImage,@PathVariable Long filter,@PathVariable Long idFilter, @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.replaceImage(idImage,filter,idFilter,imageFile));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE + e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete-image/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.deleteImageFile(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE + e.getMessage());
        }
    }
    @GetMapping(value = "/{filter}/{id}")
    public ResponseEntity<?> getImageByIdFilter(@PathVariable Long id,@PathVariable String filter) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(imageService.getImageIdbyFilter(id,filter));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ERROR_MESSAGE + e.getMessage());
        }
    }

}
