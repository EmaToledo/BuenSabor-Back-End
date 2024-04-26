package com.elbuensabor.api.service.impl;

import com.elbuensabor.api.dto.AddressDTO;
import com.elbuensabor.api.entity.Address;
import com.elbuensabor.api.entity.User;
import com.elbuensabor.api.mapper.AddressMapper;
import com.elbuensabor.api.mapper.GenericMapper;
import com.elbuensabor.api.repository.IAddressRepository;
import com.elbuensabor.api.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl extends GenericServiceImpl<Address, AddressDTO, Long> implements AddressService {

    @Autowired
    private IAddressRepository addressRepository;

    private final AddressMapper addressMapper = AddressMapper.getInstance();
    public AddressServiceImpl(com.elbuensabor.api.repository.IGenericRepository<Address, Long> IGenericRepository, GenericMapper<Address, AddressDTO> genericMapper, IAddressRepository addressRepository) {
        super(IGenericRepository, genericMapper);
        this.addressRepository = addressRepository;
    }
    @Transactional(readOnly = true)
    public List<AddressDTO> getAddressDTOsByUserId(User user) throws Exception{
        return addressMapper.toDTOList(addressRepository.findByUser(user));
    };

    @Override
    @Transactional
    public Address saveAddress(AddressDTO dto,User user) throws Exception {
        try {
            Address address = addressMapper.toEntity(dto);
            address.setUser(user);
            return addressRepository.save(address);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
