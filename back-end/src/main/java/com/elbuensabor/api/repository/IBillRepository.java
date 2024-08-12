package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.Bill;
import com.elbuensabor.api.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBillRepository extends IGenericRepository<Bill, Long>{

    // Busca Facutura por id del Pedido
    @Query("SELECT b FROM Bill b WHERE b.order.id = :orderId")
    Bill findByBillByOrderId(Long orderId);
}
