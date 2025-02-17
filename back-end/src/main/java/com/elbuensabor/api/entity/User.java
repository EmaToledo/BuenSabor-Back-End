package com.elbuensabor.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@AttributeOverride(name = "id", column = @Column(name = "id_user"))
public class User extends GenericEntity {
    @Column(name = "id_auth0_user")
    private String idAuth0User;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address")
    private String address;

    @Column(name = "apartment")
    private String apartment;

    @Column(name = "phone")
    private String phone;

    @Column(name = "blocked")
    private boolean blocked;

    @Column(name = "logged", nullable = true)
    private boolean logged;


    @ManyToOne(optional = false)
    @JoinColumn(name = "id_rol")
    private Role role;

    @Column(name = "descuento")
    private Long descuento;

}
