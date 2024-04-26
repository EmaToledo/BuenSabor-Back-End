package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.Phone;
import com.elbuensabor.api.entity.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IPhoneRepository extends IGenericRepository<Phone, Long>{
    @Query("SELECT p FROM Phone p WHERE p.user = :user")
    List<Phone>  findByUser(User user);
}
