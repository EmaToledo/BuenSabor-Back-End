package com.elbuensabor.api.mapper;

import com.elbuensabor.api.dto.UserDTO;
import com.elbuensabor.api.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends GenericMapper<User, UserDTO> {
    static UserMapper getInstance() { return Mappers.getMapper(UserMapper.class); }

    @Mapping(source = "idAuth0User", target = "auth0UserId")
    @Mapping(source = "userRoleId", target = "roleId")
    UserDTO toDTO(User user);

    @Mapping(target = "idAuth0User", source = "auth0UserId")
    @Mapping(target = "userRoleId", source = "roleId")
    User toEntity(UserDTO userDTO);

    List<UserDTO> toDTOList(List<User> source);

    List<User> toEntityList(List<UserDTO> source);

}
