package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.Address;
import com.elbuensabor.api.entity.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IAddressRepository extends IGenericRepository<Address, Long>{
    @Query("SELECT a FROM Address a WHERE a.user = :user")
    List<Address> findByUser(User user);
}
