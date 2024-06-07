package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.ImageDTO;
import com.elbuensabor.api.entity.Image;
import org.springframework.web.multipart.MultipartFile;


public interface ImageService extends GenericService<Image, ImageDTO, Long> {

    // Guarda una imagen
    Image saveImageFile(Long filter,Long id, MultipartFile image) throws Exception;

    // Reemplaza una imagen
    Image replaceImage(Long idImage, Long filter,Long idFilter, MultipartFile newImage) throws Exception;

    // Elimina una imagen
    String deleteImageFile(Long id) throws Exception;

    ImageDTO getImageIdbyFilter(Long id , String filter) throws Exception;

}
