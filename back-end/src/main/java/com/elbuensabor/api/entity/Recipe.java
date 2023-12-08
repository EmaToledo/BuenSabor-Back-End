package com.elbuensabor.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recipe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "id_recipe"))
public class Recipe extends GenericEntity {

    @Column(name = "denomination")
    private String denomination;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "idManufacturedProduct")
    @JsonBackReference
    private ManufacturedProduct manufacturedProduct;

}
