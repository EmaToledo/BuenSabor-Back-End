package com.example.api.repository;

import com.example.api.entity.GenericEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface IGenericRepository<Entity extends GenericEntity, ID extends Serializable> extends JpaRepository<Entity, ID> {
}
