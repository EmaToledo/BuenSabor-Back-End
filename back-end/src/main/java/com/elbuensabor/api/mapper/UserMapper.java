package com.elbuensabor.api.mapper;
import com.elbuensabor.api.dto.Auth0UserDTO;
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
    UserDTO toDTO(User user);

    @Mapping(target = "idAuth0User", source = "auth0UserId")
    User toEntity(UserDTO userDTO);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "logged", constant = "false")
    @Mapping(target = "idAuth0User", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "apartment", ignore = true)
    User Auth0toEntity(Auth0UserDTO userDTO);


    List<UserDTO> toDTOList(List<User> source);

    List<User> toEntityList(List<UserDTO> source);



}
