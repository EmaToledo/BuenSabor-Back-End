package com.example.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ingredient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "id_ingredient"))
public class Ingredient extends GenericEntity {

    @Column(name = "denomination")
    private String denomination;

    @Column(name = "min_stock")
    private Long minStock;

    @Column(name = "actual_stock")
    private Long actualStock;

    @Column(name = "unit")
    private String unit;

    @Column(name = "availability")
    private Boolean availability;

    @ManyToOne
    @JoinColumn(name = "id_category")
    @JsonBackReference
    private Category ingredientCategory;

}
