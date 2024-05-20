package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.Image;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface IImageRepository extends IGenericRepository<Image, Long> {

    @Query("SELECT i FROM Image i WHERE i.idManufacturedProduct.id = :idManufacturedProduct")
    Image findImageIdByIdManufacturedProduct(Long idManufacturedProduct);

    @Query("SELECT i FROM Image i WHERE i.idUser.id = :idUser")
    Image findImageIdByIdUser(Long idUser);

    @Query("SELECT i FROM Image i WHERE i.idProduct.id = :idProduct")
    Image findImageIdByIdProduct(Long idProduct);

}

