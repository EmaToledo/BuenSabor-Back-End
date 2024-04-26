package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.Image;
import org.springframework.stereotype.Repository;

@Repository
public interface IImageRepository extends IGenericRepository<Image, Long> {

}
