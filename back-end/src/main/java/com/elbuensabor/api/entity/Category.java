package com.elbuensabor.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "id_category"))
public class Category extends GenericEntity {

    @Column(name = "denomination")
    private String denomination;

    @Column(name = "type")
    private Character type;

    @Column(name = "availability")
    private Boolean availability;

    @ManyToOne
    @JoinColumn(name = "idFatherCategory")
    @JsonBackReference
    private Category fatherCategory;

    @OneToMany(mappedBy = "fatherCategory", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Category> childCategories;

}