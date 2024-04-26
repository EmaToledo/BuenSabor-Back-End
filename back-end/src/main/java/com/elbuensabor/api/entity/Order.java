package com.elbuensabor.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "customer_order")
@Getter
@Setter
@AttributeOverride(name = "id", column = @Column(name = "id_order"))
public class Order  extends GenericEntity {

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_Time")
    private Date dateTime;

    @Column(name = "delivery_method")
    private String deliveryMethod;

    @Column(name = "state")
    private boolean state;

    @Column(name = "is_canceled")
    private boolean isCanceled;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

}
