package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.dto.ImageDTO;
import com.elbuensabor.api.entity.Image;
import com.elbuensabor.api.entity.ManufacturedProduct;
import com.elbuensabor.api.entity.Product;
import com.elbuensabor.api.mapper.GenericMapper;
import com.elbuensabor.api.mapper.ImageMapper;
import com.elbuensabor.api.repository.IImageRepository;
import com.elbuensabor.api.repository.IManufacturedProductRepository;
import com.elbuensabor.api.repository.IProductRepository;
import com.elbuensabor.api.service.ImageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageServiceImpl extends GenericServiceImpl<Image, ImageDTO, Long> implements ImageService {

    private final String IMAGE_UPLOAD_PATH = "C:/imagenes_proyecto";

    @Autowired
    private IImageRepository imageRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IManufacturedProductRepository manufacturedProductRepository;

    private final ImageMapper imageMapper = ImageMapper.getInstance();

    public ImageServiceImpl(com.elbuensabor.api.repository.IGenericRepository<Image, Long> IGenericRepository, GenericMapper<Image, ImageDTO> genericMapper) {
        super(IGenericRepository, genericMapper);
    }

    /*  POR REVISAR
        **  Verificar en donde se guardaran las imagenes
            para tener mas orden podrian guardarse en carpetas diferentes (Pr y MaPr)
            o dejarlas todas en una misma carpeta ya que se diferencian por los nombres y los id

        **  UUID.randomUUID().toString() + "_" +
            Posible uso de UUID para los nombres, para no tener nombres repetidos en el directorio
            de las imagenes, se puede agregar el nombre del producto al que pertenece para tener
            en claro a donde pertence cada imagen
    */
    @Override
    @Transactional
    public Image saveImageFile(ImageDTO dto, MultipartFile imageFile) throws Exception {

        try {
            String fileName = imageFile.getOriginalFilename().replace(" ", "-");

            // Nombre para cambiar el formato de la imagen
            String newFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".jpg";

            String filePath = IMAGE_UPLOAD_PATH + File.separator + newFileName;

            // creacion de la entidad imagen a guardar
            Image image = imageMapper.toEntity(dto);

            // setting de atributos
            setIdRelationsIfExists(dto, image);
            image.setName(newFileName);
            image.setRoute(filePath);

            File localImageFile = new File(filePath);
            imageFile.transferTo(localImageFile);

            convertImageType(localImageFile);

            image.setType("image/jpg");
            image.setSize((int) localImageFile.length());

            Image savedImage = imageRepository.save(image);

            return savedImage;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Image replaceImage(Long id, ImageDTO dto, MultipartFile newImage) throws Exception {
        try {
            Image existingImage = imageRepository.findById(id)
                    .orElseThrow(() -> new Exception("La imagen a actualizar no existe."));

            String existingFilePath = existingImage.getRoute();

            String fileName = newImage.getOriginalFilename().replace(" ", "-");
            String newFileName = fileName.substring(0, fileName.lastIndexOf(".")) + ".jpg";
            String newFilePath = IMAGE_UPLOAD_PATH + File.separator + newFileName;

            File existingImageFile = new File(existingFilePath);

            if (existingImageFile.delete()) {
                File localImageFile = new File(newFilePath);

                existingImage = imageMapper.toEntity(dto);

                // setting de atributos
                existingImage.setId(id);
                existingImage.setName(newFileName);
                existingImage.setRoute(newFilePath);

                setIdRelationsIfExists(dto, existingImage);

                newImage.transferTo(localImageFile);
                convertImageType(localImageFile);

                existingImage.setType("image/jpg");
                existingImage.setSize((int) localImageFile.length());

                Image updatedImage = imageRepository.save(existingImage);

                return updatedImage;
            } else {
                throw new Exception("No se pudo eliminar el archivo de imagen existente.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public void convertImageType(File localImageFile) throws Exception {
        try {
            BufferedImage bufferedImage = ImageIO.read(localImageFile);

            // Cambio de formato
            BufferedImage convertedImage = new BufferedImage(bufferedImage.getWidth(),
                    bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);

            Graphics2D graph2d = convertedImage.createGraphics();
            graph2d.drawImage(bufferedImage, 0, 0, null);
            graph2d.dispose();

            ImageIO.write(convertedImage, "jpeg", localImageFile);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public String deleteImageFile(Long id) throws Exception {
        try {
            Image image = imageRepository.findById(id)
                    .orElseThrow(() -> new Exception("La imagen a eliminar no existe."));

            File imageFile = new File(image.getRoute());

            if (imageFile.delete()) {
                imageRepository.deleteById(image.getId());

                return "La imagen con id: " + image.getId() + " se ha eliminado correctamente.";
            } else {
                throw new Exception("No se pudo eliminar el archivo de imagen en el directorio de imagenes");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void setIdRelationsIfExists(ImageDTO dto, Image image) throws Exception {
        Long productId = dto.getProductId();
        Long manufacturedProductId = dto.getManufacturedProductId();

        if (productId != null) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new Exception("El producto con id " + productId + " no existe"));
            image.setIdProduct(product);
            image.setIdManufacturedProduct(null);
        } else if (manufacturedProductId != null) {
            ManufacturedProduct manufacturedProduct = manufacturedProductRepository.findById(manufacturedProductId)
                    .orElseThrow(() -> new Exception("El producto manufacturado con id " + manufacturedProductId + " no existe"));
            image.setIdManufacturedProduct(manufacturedProduct);
            image.setIdProduct(null);
        }
    }

}