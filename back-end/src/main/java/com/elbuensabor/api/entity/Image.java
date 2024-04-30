package com.elbuensabor.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "image")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "id_image"))
public class Image extends GenericEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "route")
    private String route;

    @Column(name = "type")
    private String type;

    @Column(name = "size")
    private Integer size;

    @ManyToOne
    @JoinColumn(name = "idProduct")
    @JsonBackReference
    private Product idProduct;

    @ManyToOne
    @JoinColumn(name = "idManufacturedProduct")
    @JsonBackReference
    private ManufacturedProduct idManufacturedProduct;

    @ManyToOne
    @JoinColumn(name = "idUser")
    @JsonBackReference
    private User idUser;


}
