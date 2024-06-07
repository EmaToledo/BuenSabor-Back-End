package com.elbuensabor.api.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "order_detail")
@Getter
@Setter
@AttributeOverride(name = "id", column = @Column(name = "id_order_detail"))
public class OrderDetail extends GenericEntity{

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "subtotal")
    private Double subtotal;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_order")
    @JsonBackReference
    private Order order;

//    @ManyToOne(optional = false)
//    @JoinColumn(name = "id_item")
//    private Item item;

    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "id_manufactured_product")
    private ManufacturedProduct manufacturedProduct;
}
