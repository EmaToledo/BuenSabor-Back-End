package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.Role;
import com.elbuensabor.api.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends IGenericRepository<Role, Long> {

}
