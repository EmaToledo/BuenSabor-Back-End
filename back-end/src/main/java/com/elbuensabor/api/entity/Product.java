package com.elbuensabor.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "availability")
    private Boolean availability;

    @ManyToOne
    @JoinColumn(name = "id_category")
    @JsonBackReference
    private Category productCategory;

    @Column(name = "categoryDenomination")
    private String categoryDenomination;


}
