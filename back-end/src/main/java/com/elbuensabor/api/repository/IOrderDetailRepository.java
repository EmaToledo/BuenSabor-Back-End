package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.OrderDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderDetailRepository extends IGenericRepository<OrderDetail, Long>{
}
