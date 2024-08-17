package com.elbuensabor.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ingredient_manufactured_link")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "id_ingredient_manufactured_link"))
public class IngredientManufacturedLink extends GenericEntity {

    @ManyToOne
    @JoinColumn(name = "idManufacturedProduct")
    @JsonBackReference
    private ManufacturedProduct manufacturedProduct;

    @ManyToOne
    @JoinColumn(name = "idIngredient")
    @JsonBackReference
    private Ingredient ingredient;
}
