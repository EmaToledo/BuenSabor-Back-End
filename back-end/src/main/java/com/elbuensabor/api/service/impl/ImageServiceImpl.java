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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

@Service
public class ImageServiceImpl extends GenericServiceImpl<Image, ImageDTO, Long> implements ImageService {

   private final String IMAGE_UPLOAD_PATH = "C:/imagenes_proyecto/uploads";
//   @Value("${user.dir}")
//    private  String IMAGE_UPLOAD_PATH;
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
    public Image saveImageFile(Long filter,Long id, MultipartFile imageFile) throws Exception {

        try {

            String fileName = changeFileName(imageFile.getOriginalFilename(), filter);
            String filePath = changeFilePath(fileName, filter);
            String base64 = convertMultipartFileToBase64(imageFile);

            // creacion de la entidad imagen a guardar
            Image image = new Image();

            // setting de atributos
            setIdRelationsIfExists(filter,id, image);
            image.setName(fileName);
            image.setRoute(filePath);
            image.setBase64(base64);

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
    public Image replaceImage(Long idImage, Long filter,Long idFilter, MultipartFile newImage) throws Exception {
        try {
            Image existingImage = imageRepository.findById(idImage).orElseThrow(() -> new Exception("La imagen a actualizar no existe."));

            String existingFilePath = existingImage.getRoute();

            String fileName = changeFileName(newImage.getOriginalFilename(), filter);
            String newFilePath = changeFilePath(fileName, filter);
            String base64 = convertMultipartFileToBase64(newImage);

            File existingImageFile = new File(existingFilePath);

            if (existingImageFile.delete()) {
                File localImageFile = new File(newFilePath);

                existingImage = new Image();

                // setting de atributos
                existingImage.setId(idImage);
                existingImage.setName(fileName);
                existingImage.setRoute(newFilePath);
                existingImage.setBase64(base64);

                setIdRelationsIfExists(filter,idFilter, existingImage);

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

    private String changeFileName(String fileName, long filter) throws Exception {

        try {
            String newFileName = UUID.randomUUID().toString() + "_";

            if (filter == 1) {
                newFileName += "user_" + fileName;
            } else if (filter == 2) {
                newFileName += "product_" + fileName;
            } else if (filter == 3) {
                newFileName += "manufactured_" + fileName;
            }else{
                throw new Exception("filtro incorrecto");
            }

            newFileName = newFileName.replace(" ", "-");

            // Nombre para cambiar el formato de la imagen
            String finalFileName = newFileName.substring(0, newFileName.lastIndexOf(".")) + ".jpg";

            return finalFileName;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private String changeFilePath(String fileName, long filter) throws Exception {

        try {
            String newFilePath = IMAGE_UPLOAD_PATH;

            if (filter == 1) {
                newFilePath += "/users";
            } else if (filter == 2) {
                newFilePath += "/products";
            } else if (filter == 3) {
                newFilePath += "/manufactured_products";
            } else {
                throw new Exception("filtro incorrecto");
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

    @Override
    @Transactional(readOnly = true)
    public ImageDTO getImageIdbyFilter(Long idFilter , String filter) {
        if (idFilter != null && !filter.isEmpty()) {
            Image image = null;
            if (filter.equals("p")) {
                image = imageRepository.findImageIdByIdProduct(idFilter);
            } else if (filter.equals("u")) {
                image = imageRepository.findImageIdByIdUser(idFilter);
            } else if (filter.equals("mp")) {
                image = imageRepository.findImageIdByIdManufacturedProduct(idFilter);
            }

            if (image != null) {
                return imageMapper.toDTO(image);
            }
        }
        return null;
    }

    private void setIdRelationsIfExists(long filter,long id, Image image) throws Exception {
        try {

            if (filter == 2) {
                Product product = productRepository.findById(id).orElseThrow(() -> new Exception("El producto con id " + id + " no existe"));
                image.setIdProduct(product);
                image.setIdManufacturedProduct(null);
                image.setIdUser(null);
            } else if (filter == 3) {
                ManufacturedProduct manufacturedProduct = manufacturedProductRepository.findById(id).orElseThrow(() -> new Exception("El producto manufacturado con id " + id + " no existe"));
                image.setIdManufacturedProduct(manufacturedProduct);
                image.setIdProduct(null);
                image.setIdUser(null);
            } else if (filter == 1) {
                User user = userRepository.findById(id).orElseThrow(() -> new Exception("El usuario con id " + id + " no existe"));
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

    public String convertMultipartFileToBase64(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("El archivo es nulo o está vacío.");
        }
        byte[] bytes = file.getBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }

}