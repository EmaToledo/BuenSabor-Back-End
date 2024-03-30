package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.dto.PhoneDTO;
import com.elbuensabor.api.entity.Phone;
import com.elbuensabor.api.entity.User;
import com.elbuensabor.api.mapper.GenericMapper;
import com.elbuensabor.api.mapper.PhoneMapper;
import com.elbuensabor.api.repository.IPhoneRepository;
import com.elbuensabor.api.service.PhoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PhoneServiceImpl extends GenericServiceImpl<Phone, PhoneDTO, Long> implements PhoneService {

    @Autowired
    private IPhoneRepository phoneRepository;
    private final PhoneMapper phoneMapper = PhoneMapper.getInstance();
    public PhoneServiceImpl(com.elbuensabor.api.repository.IGenericRepository<Phone, Long> IGenericRepository, GenericMapper<Phone, PhoneDTO> genericMapper, IPhoneRepository phoneRepository) {
        super(IGenericRepository, genericMapper);
        this.phoneRepository = phoneRepository;
    }

    @Transactional(readOnly = true)
    public List<PhoneDTO> getPhoneDTOsByUserId(User user ) throws Exception{
        try {
            return phoneMapper.toDTOList(phoneRepository.findByUser(user));
        }catch (Exception e){
            throw  new Exception(e.getMessage());
        }

    };
    @Override
    @Transactional
    public Phone savePhone(PhoneDTO dto, User user) throws Exception {
        try {
            Phone phone = phoneMapper.toEntity(dto);
            phone.setUser(user);
            return phoneRepository.save(phone);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
