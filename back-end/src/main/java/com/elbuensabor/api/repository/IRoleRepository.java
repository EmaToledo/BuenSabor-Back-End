package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends IGenericRepository<Role, Long> {
}
