package com.elbuensabor.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter
@Setter
@AttributeOverride(name = "id", column = @Column(name = "id_address"))
public class Address extends GenericEntity {
    @Column(name = "address")
    private String address;

    @Column(name = "departament")
    private String departament;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

}
