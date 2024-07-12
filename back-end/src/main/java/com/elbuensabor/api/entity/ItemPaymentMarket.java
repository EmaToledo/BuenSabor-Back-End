package com.elbuensabor.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "item_payment_market")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "id_payment"))
public class ItemPaymentMarket extends GenericEntity {

    @Column(name = "preference_id")
    private  String preferenceId;

    @Column(name = "payment_id")
    private Long paymentID;

    @Column(name = "mount_refund")
    private Double mountRefund;

    @OneToOne(optional = false)
    @JoinColumn(name = "id_order")
    private Order order;
}
