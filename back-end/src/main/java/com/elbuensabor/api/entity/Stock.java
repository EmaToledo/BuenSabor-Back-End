package com.elbuensabor.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "stock")
@Getter
@Setter
@AttributeOverride(name = "id", column = @Column(name = "id_stock"))
public class Stock extends GenericEntity{
    @Column(name = "actual_stock")
    private Long actualStock;

    @Column(name = "min_stock")
    private Long minStock;

    @ManyToOne
    @JoinColumn(name = "id_product")
    @JsonBackReference
    private Product productStock;

    @ManyToOne
    @JoinColumn(name = "id_ingredient")
    @JsonBackReference
    private Ingredient ingredientStock;
}