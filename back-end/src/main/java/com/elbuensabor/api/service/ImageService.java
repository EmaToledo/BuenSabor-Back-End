package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.ImageDTO;
import com.elbuensabor.api.entity.Image;
import org.springframework.web.multipart.MultipartFile;


public interface ImageService extends GenericService<Image, ImageDTO, Long> {

    // Guarda una imagen
    Image saveImageFile(ImageDTO dto, MultipartFile image) throws Exception;

    // Reemplaza una imagen
    Image replaceImage(Long id, ImageDTO dto, MultipartFile newImage) throws Exception;

    // Elimina una imagen
    String deleteImageFile(Long id) throws Exception;

    ImageDTO getImageIdbyFilter(Long id , String filter) throws Exception;

}
