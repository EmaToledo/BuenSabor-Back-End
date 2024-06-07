package com.elbuensabor.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Table(name = "item")
@Getter
@Setter
@AttributeOverride(name = "id", column = @Column(name = "id_item"))
public class Item extends GenericEntity {
    @ManyToOne
    @JoinColumn(name = "id_product")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "id_manufactured_product")
    private ManufacturedProduct manufacturedProduct;
}