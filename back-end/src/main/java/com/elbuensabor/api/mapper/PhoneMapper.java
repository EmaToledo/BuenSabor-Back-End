package com.elbuensabor.api.mapper;

import com.elbuensabor.api.dto.PhoneDTO;
import com.elbuensabor.api.entity.Phone;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PhoneMapper extends GenericMapper<Phone, PhoneDTO>{
    static PhoneMapper getInstance() {
        return Mappers.getMapper(PhoneMapper.class);
    }

    PhoneDTO toDTO(Phone phone);


    Phone toEntity(PhoneDTO phoneDTO);

    List<PhoneDTO> toDTOList(List<Phone> source);

    List<Phone> toEntityList(List<PhoneDTO> source);
}
