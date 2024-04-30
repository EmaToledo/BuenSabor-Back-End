package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.dto.ImageDTO;
import com.elbuensabor.api.entity.Image;
import com.elbuensabor.api.entity.ManufacturedProduct;
import com.elbuensabor.api.entity.Product;
import com.elbuensabor.api.entity.User;
import com.elbuensabor.api.mapper.GenericMapper;
import com.elbuensabor.api.mapper.ImageMapper;
import com.elbuensabor.api.repository.IImageRepository;
import com.elbuensabor.api.repository.IManufacturedProductRepository;
import com.elbuensabor.api.repository.IProductRepository;
import com.elbuensabor.api.repository.IUserRepository;
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

    private final String IMAGE_UPLOAD_PATH = "C:/imagenes_proyecto/uploads";

    @Autowired
    private IImageRepository imageRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IManufacturedProductRepository manufacturedProductRepository;

    @Autowired
    private IUserRepository userRepository;

    private final ImageMapper imageMapper = ImageMapper.getInstance();

    public ImageServiceImpl(com.elbuensabor.api.repository.IGenericRepository<Image, Long> IGenericRepository, GenericMapper<Image, ImageDTO> genericMapper) {
        super(IGenericRepository, genericMapper);
    }

    @Override
    @Transactional
    public Image saveImageFile(ImageDTO dto, MultipartFile imageFile) throws Exception {

        try {

            String fileName = changeFileName(imageFile.getOriginalFilename(), dto);
            String filePath = changeFilePath(fileName, dto);

            // creacion de la entidad imagen a guardar
            Image image = imageMapper.toEntity(dto);

            // setting de atributos
            setIdRelationsIfExists(dto, image);
            image.setName(fileName);
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
            Image existingImage = imageRepository.findById(id).orElseThrow(() -> new Exception("La imagen a actualizar no existe."));

            String existingFilePath = existingImage.getRoute();

            String fileName = changeFileName(newImage.getOriginalFilename(), dto);
            String newFilePath = IMAGE_UPLOAD_PATH + File.separator + fileName;

            File existingImageFile = new File(existingFilePath);

            if (existingImageFile.delete()) {
                File localImageFile = new File(newFilePath);

                existingImage = imageMapper.toEntity(dto);

                // setting de atributos
                existingImage.setId(id);
                existingImage.setName(fileName);
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

    @Override
    @Transactional
    public String deleteImageFile(Long id) throws Exception {
        try {
            Image image = imageRepository.findById(id).orElseThrow(() -> new Exception("La imagen a eliminar no existe."));

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

    public void convertImageType(File localImageFile) throws Exception {
        try {
            BufferedImage bufferedImage = ImageIO.read(localImageFile);

            // Cambio de formato
            BufferedImage convertedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);

            Graphics2D graph2d = convertedImage.createGraphics();
            graph2d.drawImage(bufferedImage, 0, 0, null);
            graph2d.dispose();

            ImageIO.write(convertedImage, "jpeg", localImageFile);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private String changeFileName(String fileName, ImageDTO dto) throws Exception {

        try {
            String newFileName = UUID.randomUUID().toString() + "_";

            if (dto.getUserId() != null) {
                newFileName += "user_" + fileName;
            } else if (dto.getManufacturedProductId() != null) {
                newFileName += "manufactured_" + fileName;
            } else if (dto.getProductId() != null) {
                newFileName += "product_" + fileName;
            }

            newFileName = newFileName.replace(" ", "-");

            // Nombre para cambiar el formato de la imagen
            String finalFileName = newFileName.substring(0, newFileName.lastIndexOf(".")) + ".jpg";

            return finalFileName;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private String changeFilePath(String fileName, ImageDTO dto) throws Exception {

        try {
            String newFilePath = IMAGE_UPLOAD_PATH;
            if (dto.getUserId() != null) {
                newFilePath += "/users";
            } else if (dto.getManufacturedProductId() != null) {
                newFilePath += "/manufactured_products";
            } else if (dto.getProductId() != null) {
                newFilePath += "/products";
            }

            // Verificar si la ruta existe, si no, crearla
            File directory = new File(newFilePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            newFilePath += File.separator + fileName;

            return newFilePath;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void setIdRelationsIfExists(ImageDTO dto, Image image) throws Exception {
        try {
            Long productId = dto.getProductId();
            Long manufacturedProductId = dto.getManufacturedProductId();
            Long userId = dto.getUserId();

            if (productId != null && (manufacturedProductId == null && userId == null)) {
                Product product = productRepository.findById(productId).orElseThrow(() -> new Exception("El producto con id " + productId + " no existe"));
                image.setIdProduct(product);
                image.setIdManufacturedProduct(null);
                image.setIdUser(null);
            } else if (manufacturedProductId != null && (productId == null && userId == null)) {
                ManufacturedProduct manufacturedProduct = manufacturedProductRepository.findById(manufacturedProductId).orElseThrow(() -> new Exception("El producto manufacturado con id " + manufacturedProductId + " no existe"));
                image.setIdManufacturedProduct(manufacturedProduct);
                image.setIdProduct(null);
                image.setIdUser(null);
            } else if (userId != null && (productId == null && manufacturedProductId == null)) {
                User user = userRepository.findById(userId).orElseThrow(() -> new Exception("El usuario con id " + userId + " no existe"));
                image.setIdUser(user);
                image.setIdProduct(null);
                image.setIdManufacturedProduct(null);
            } else {
                throw new Exception("Error al relacionar la imagen con un producto, manufacturado o usuario");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

}