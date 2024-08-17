package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.Image;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IImageRepository extends IGenericRepository<Image, Long> {
    @Query(value = "SELECT * FROM image WHERE relation_type = 'U'", nativeQuery = true)
    List<Image> findUserImages();

    @Query(value = "SELECT * FROM image WHERE relation_type = 'P'", nativeQuery = true)
    List<Image> findProductImages();

    @Query(value = "SELECT * FROM image WHERE relation_type = 'M'", nativeQuery = true)
    List<Image> findManufacturedProductImages();

    @Query("SELECT i FROM Image i WHERE i.idManufacturedProduct.id = :idManufacturedProduct")
    Image findImageByIdFromManufacturedProducts(Long idManufacturedProduct);

    @Query("SELECT i FROM Image i WHERE i.idUser.id = :idUser")
    Image findImageByIdFromUsers(Long idUser);

    @Query("SELECT i FROM Image i WHERE i.idProduct.id = :idProduct")
    Image findImageByIdFromProducts(Long idProduct);

}

