package com.example.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "id_product"))
public class Product extends GenericEntity {
    
    @Column(name = "denomination")
    private String denomination;

    @Column(name = "description")
    private String description;

    @Column(name = "cooking_time", columnDefinition = "TIME")
    private Time cooking_time;

    @Column(name = "availability")
    private Boolean availability;

    @ManyToOne
    @JoinColumn(name = "id_category")
    @JsonBackReference
    private Category productCategory;

}
