package com.elbuensabor.api.repository;

import com.elbuensabor.api.dto.OrderDetailDTO;
import com.elbuensabor.api.entity.OrderDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderDetailRepository extends IGenericRepository<OrderDetail, Long>{
    @Query("SELECT od FROM OrderDetail od WHERE od.order.id = :orderId")
    List<OrderDetail> findOrderDetailsByOrder(Long orderId);
}
