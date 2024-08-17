package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.ImageDTO;
import com.elbuensabor.api.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ImageService extends GenericService<Image, ImageDTO, Long> {

    // Guarda una imagen
    Image saveImageFile(Character relationType, Long relationId, MultipartFile imageFile) throws Exception;

    // Reemplaza una imagen
    Image replaceImage(Long id, MultipartFile newImage) throws Exception;

    // Elimina una imagen
    String deleteImageFile(Long id) throws Exception;

    // Encuentra todas las imagenes que coincidan con el filtro
    List<ImageDTO> findImagesByType(Character imageType) throws Exception;

    // Encuentra una imagen por id coincidan con el filtro
    ImageDTO findImageIdByType(Long relationId, Character relationType) throws Exception;

}
