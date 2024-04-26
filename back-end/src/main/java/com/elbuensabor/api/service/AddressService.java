package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.AddressDTO;
import com.elbuensabor.api.entity.Address;
import com.elbuensabor.api.entity.User;

import java.util.List;

public interface AddressService extends GenericService<Address, AddressDTO, Long>{

  List<AddressDTO> getAddressDTOsByUserId(User user) throws Exception;

  Address saveAddress(AddressDTO dto,User user) throws Exception;
}
