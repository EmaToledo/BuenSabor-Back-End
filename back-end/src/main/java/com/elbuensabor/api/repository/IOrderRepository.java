package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderRepository extends IGenericRepository<Order, Long>{
    @EntityGraph(attributePaths = {"orderDetails", "orderDetails.product","orderDetails.manufacturedProduct"})
    List<Order> findAll();

    @EntityGraph(attributePaths = {"orderDetails", "orderDetails.product","orderDetails.manufacturedProduct"})
    //@Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    List<Order> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT o FROM Order o WHERE o.state = :state")
    List<Order> findAllByOrderstate(@Param("state") boolean state);
}
