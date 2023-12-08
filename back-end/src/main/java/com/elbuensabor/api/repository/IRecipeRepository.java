package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.Recipe;
import org.springframework.stereotype.Repository;

@Repository
public interface IRecipeRepository extends IGenericRepository<Recipe, Long> {

}
