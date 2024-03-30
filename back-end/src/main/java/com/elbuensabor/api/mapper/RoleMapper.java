package com.elbuensabor.api.mapper;

import com.elbuensabor.api.dto.RoleDTO;
import com.elbuensabor.api.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper extends GenericMapper<Role, RoleDTO> {
    static RoleMapper getInstance() { return Mappers.getMapper(RoleMapper.class); }

    //@Mapping(source = "idAuth0Role", target = "auth0RoleId")
    RoleDTO toDTO(Role role);

    //@Mapping(target = "idAuth0Role", source = "auth0RoleId")
    Role toEntity(RoleDTO roleDTO);

    List<RoleDTO> toDTOList(List<Role> source);

    List<Role> toEntityList(List<RoleDTO> source);

}
