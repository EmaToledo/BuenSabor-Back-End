package com.elbuensabor.api.mapper;

import com.elbuensabor.api.dto.ImageDTO;
import com.elbuensabor.api.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ImageMapper extends GenericMapper<Image, ImageDTO> {

    // obtener una instancia del Image Mapper
    static ImageMapper getInstance() { return Mappers.getMapper(ImageMapper.class); }

    // mapea una entidad Image a un DTO ImageDTO
    @Mapping(target = "relationId", ignore = true)
    ImageDTO toDTO(Image source);

    // mapea un DTO ImageDTO a una entidad Image
    @Mapping(target = "idProduct", ignore = true)
    @Mapping(target = "idUser", ignore = true)
    @Mapping(target = "idManufacturedProduct", ignore = true)
    @Mapping(target = "size", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "route", ignore = true)
    @Mapping(target = "type", ignore = true)
    Image toEntity(ImageDTO source);


}
