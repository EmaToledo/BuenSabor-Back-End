package com.elbuensabor.api.repository;

import com.elbuensabor.api.entity.ItemPaymentMarket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IItemPaymentMarketRepository extends IGenericRepository<ItemPaymentMarket, Long> {
    @Query("SELECT i FROM ItemPaymentMarket i WHERE i.order.id = :orderId")
    ItemPaymentMarket findItemPaymentMarketByOrderId(Long orderId);

    @Query("SELECT i.preferenceId FROM ItemPaymentMarket i WHERE i.order.id = :orderId")
    String getPreferenceByOrderId(Long orderId);

    @Query("SELECT i FROM ItemPaymentMarket i WHERE i.preferenceId = :preferenceId")
    ItemPaymentMarket findItemPaymentMarketByPreferenceId(String preferenceId);
}
