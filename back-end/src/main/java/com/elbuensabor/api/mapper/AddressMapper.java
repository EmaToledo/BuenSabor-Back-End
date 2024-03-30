package com.elbuensabor.api.mapper;

import com.elbuensabor.api.dto.AddressDTO;
import com.elbuensabor.api.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AddressMapper extends GenericMapper<Address, AddressDTO>{
    static AddressMapper getInstance() {
        return Mappers.getMapper(AddressMapper.class);
    }
    AddressDTO toDTO(Address address);


    Address toEntity(AddressDTO addressDTO);

    List<AddressDTO> toDTOList(List<Address> source);

    List<Address> toEntityList(List<AddressDTO> source);
}
