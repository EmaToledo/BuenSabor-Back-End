package com.elbuensabor.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "price")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "id_price"))
public class Price extends GenericEntity {

    @Column(name = "sell_price")
    private Double sellPrice;

    @Column(name = "sell_price_date")
    private LocalDateTime sellPriceDate;

    @Column(name = "cost_price")
    private Double costPrice;

    @Column(name = "cost_price_date")
    private LocalDateTime costPriceDate;

    @ManyToOne()
    @JoinColumn(name = "idProduct")
    @JsonBackReference
    private Product product;

    @ManyToOne()
    @JoinColumn(name = "idManufacturedProduct")
    @JsonBackReference
    private ManufacturedProduct manufacturedProduct;

}
