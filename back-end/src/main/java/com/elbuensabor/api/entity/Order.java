package com.elbuensabor.api.entity;

import com.elbuensabor.api.Enum.OrderStatus;
import com.elbuensabor.api.Enum.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_order")
@Getter
@Setter
@AttributeOverride(name = "id", column = @Column(name = "id_order"))
public class Order  extends GenericEntity {

    @Column(name = "date_Time")
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "paid")
    private PaymentStatus paid;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "total")
    private Double total;

    @Column(name = "estimated_time")
    private Time estimatedTime;

    @Column(name = "delivery_method")
    private String deliveryMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private OrderStatus state;

    @Column(name = "payment_type")
    private String paymentType;


    @Column(name = "is_canceled")
    private boolean isCanceled;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true ,fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<OrderDetail> orderDetails;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

}
