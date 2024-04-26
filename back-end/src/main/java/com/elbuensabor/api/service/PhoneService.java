package com.elbuensabor.api.service;

import com.elbuensabor.api.dto.PhoneDTO;
import com.elbuensabor.api.entity.Phone;
import com.elbuensabor.api.entity.User;

import java.util.List;

public interface PhoneService extends GenericService<Phone, PhoneDTO, Long>{

  List<PhoneDTO> getPhoneDTOsByUserId(User user ) throws Exception;
  Phone savePhone(PhoneDTO dto,User user) throws Exception;
}
