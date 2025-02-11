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
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ImageServiceImpl extends GenericServiceImpl<Image, ImageDTO, Long> implements ImageService {

    @Autowired
    private IImageRepository imageRepository;

    @Autowired
    private IProductRepository productRepository;

    @Autowired
    private IManufacturedProductRepository manufacturedProductRepository;

    @Autowired
    private IUserRepository userRepository;

    private final ImageMapper imageMapper = ImageMapper.getInstance();

    @Value("${IMAGE_UPLOAD_PATH}")
    private String IMAGE_UPLOAD_PATH;
    private static final char IMAGE_RELATION_TYPE_PRODUCT = 'P';
    private static final char IMAGE_RELATION_TYPE_MANUFACTURED_PRODUCT = 'M';
    private static final char IMAGE_RELATION_TYPE_USER = 'U';

    public ImageServiceImpl(com.elbuensabor.api.repository.IGenericRepository<Image, Long> IGenericRepository, GenericMapper<Image, ImageDTO> genericMapper) {
        super(IGenericRepository, genericMapper);
    }

    @Override
    @Transactional
    public Image saveImageFile(Character relationType, Long relationId, MultipartFile imageFile) throws Exception {

        try {

            String fileName = changeFileName(imageFile.getOriginalFilename(), relationType);
            String filePath = changeFilePath(fileName, relationType);

            // creacion de la entidad imagen a guardar
            Image image = new Image();

            // setting de atributos
            setIdRelationsIfExists(image, relationType, relationId);
            image.setName(fileName);
            image.setRoute(filePath);

            File localImageFile = new File(filePath);
            imageFile.transferTo(localImageFile);

            convertImageType(localImageFile);

            image.setType("image/jpg");
            image.setRelationType(relationType);
            image.setSize((int) localImageFile.length());

            Image savedImage = imageRepository.save(image);

            return savedImage;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    @Transactional
    public Image replaceImage(Long id, MultipartFile newImage) throws Exception {
        try {
            Image existingImage = imageRepository.findById(id).orElseThrow(() -> new Exception("La imagen a actualizar no existe."));

            String existingFilePath = existingImage.getRoute();
            Character existingRelationType = existingImage.getRelationType();

            Long existingRelationId = existingRelationType.equals(IMAGE_RELATION_TYPE_USER) ? existingImage.getIdUser().getId()
                    : existingRelationType.equals(IMAGE_RELATION_TYPE_PRODUCT) ? existingImage.getIdProduct().getId()
                    : existingRelationType.equals(IMAGE_RELATION_TYPE_MANUFACTURED_PRODUCT) ? existingImage.getIdManufacturedProduct().getId() : null;

            String fileName = changeFileName(newImage.getOriginalFilename(), existingRelationType);
            String newFilePath = changeFilePath(fileName, existingRelationType);

            File existingImageFile = new File(existingFilePath);

            if (existingImageFile.delete()) {
                File localImageFile = new File(newFilePath);

                existingImage = new Image();

                // setting de atributos
                existingImage.setId(id);
                existingImage.setName(fileName);
                existingImage.setRoute(newFilePath);

                setIdRelationsIfExists(existingImage, existingRelationType, existingRelationId);

                newImage.transferTo(localImageFile);
                convertImageType(localImageFile);

                existingImage.setType("image/jpg");
                existingImage.setRelationType(existingRelationType);
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

    @Override
    @Transactional(readOnly = true)
    public List<ImageDTO> findImagesByType(Character imageType) {
        List<Image> images;

        switch (imageType) {
            case IMAGE_RELATION_TYPE_USER:
                images = imageRepository.findUserImages();
                break;
            case IMAGE_RELATION_TYPE_MANUFACTURED_PRODUCT:
                images = imageRepository.findManufacturedProductImages();
                break;
            case IMAGE_RELATION_TYPE_PRODUCT:
                images = imageRepository.findProductImages();
                break;
            default:
                throw new IllegalArgumentException("Tipo de imagen invalido " + imageType);
        }

        return imageMapper.toDTOsList(images);

    }

    @Override
    @Transactional(readOnly = true)
    public ImageDTO findImageIdByType(Long relationId, Character relationType) {

        if (relationId != null && relationType != null) {

            Image image;

            switch (relationType) {
                case IMAGE_RELATION_TYPE_USER:
                    image = imageRepository.findImageByIdFromUsers(relationId);
                    break;
                case IMAGE_RELATION_TYPE_MANUFACTURED_PRODUCT:
                    image = imageRepository.findImageByIdFromManufacturedProducts(relationId);
                    break;
                case IMAGE_RELATION_TYPE_PRODUCT:
                    image = imageRepository.findImageByIdFromProducts(relationId);
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de imagen invalido " + relationType);
            }

            if(image != null) {
                return imageMapper.toDTO(image);
            } else {
                throw new EntityNotFoundException("No se encontro la imagen");
            }
        }else {
            throw new IllegalArgumentException("Los parametros no pueden ser nulos");
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

    private void setIdRelationsIfExists(Image image, Character relationType, Long relationId) throws Exception {
        try {
            if (relationType.equals(IMAGE_RELATION_TYPE_PRODUCT)) {
                Product product = productRepository.findById(relationId).orElseThrow(() -> new Exception("El producto con id " + relationId + " no existe"));
                image.setIdProduct(product);
                image.setIdManufacturedProduct(null);
                image.setIdUser(null);
            } else if (relationType.equals(IMAGE_RELATION_TYPE_MANUFACTURED_PRODUCT)) {
                ManufacturedProduct manufacturedProduct = manufacturedProductRepository.findById(relationId).orElseThrow(() -> new Exception("El producto manufacturado con id " + relationId + " no existe"));
                image.setIdManufacturedProduct(manufacturedProduct);
                image.setIdProduct(null);
                image.setIdUser(null);
            } else if (relationType.equals(IMAGE_RELATION_TYPE_USER)) {
                User user = userRepository.findById(relationId).orElseThrow(() -> new Exception("El usuario con id " + relationId + " no existe"));
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

    private String changeFileName(String fileName, Character relationType) throws Exception {

        try {
            String newFileName = UUID.randomUUID().toString() + "_";

            if (relationType.equals(IMAGE_RELATION_TYPE_USER)) {
                newFileName += "user_" + fileName;
            } else if (relationType.equals(IMAGE_RELATION_TYPE_MANUFACTURED_PRODUCT)) {
                newFileName += "manufactured_" + fileName;
            } else if (relationType.equals(IMAGE_RELATION_TYPE_PRODUCT)) {
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

    private String changeFilePath(String fileName, Character relationType) throws Exception {

        try {
            String newFilePath = Paths.get(IMAGE_UPLOAD_PATH).toAbsolutePath().normalize().toString();
            if (relationType.equals(IMAGE_RELATION_TYPE_USER)) {
                newFilePath += "/users";
            } else if (relationType.equals(IMAGE_RELATION_TYPE_MANUFACTURED_PRODUCT)) {
                newFilePath += "/manufactured_products";
            } else if (relationType.equals(IMAGE_RELATION_TYPE_PRODUCT)) {
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

}