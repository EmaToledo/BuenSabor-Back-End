package com.example.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@Entity
@Table(name = "manufactured_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "id_manufactured_product"))
public class ManufacturedProduct extends GenericEntity {

    @Column(name = "denomination")
    private String denomination;

    @Column(name = "description")
    private String description;

    @Column(name = "cooking_time")
    private Time cookingTime;

    @Column(name = "availability")
    private Boolean availability;

    @Column(name = "url_image")
    private String urlImage;

    @ManyToOne
    @JoinColumn(name = "id_category")
    @JsonBackReference
    private Category manufacturedProductCategory;

}
