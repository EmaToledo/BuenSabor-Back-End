package com.elbuensabor.api.repository;
import java.util.Optional;
import com.elbuensabor.api.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserRepository extends IGenericRepository<User, Long> {

    Optional<User> findByEmail(String email);
    // Busca user mediante id Auth0
   @Query("SELECT u FROM User u WHERE u.idAuth0User = :idAuth0User")
    Optional<User> findByIdAuth0User(String idAuth0User);

    @Query("SELECT MAX(id) FROM User")
    Long findLastUserId();

}
