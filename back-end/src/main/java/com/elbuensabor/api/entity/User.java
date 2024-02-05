package com.elbuensabor.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@AttributeOverride(name = "id", column = @Column(name = "id_user"))
public class User extends GenericEntity {
    @Column(name = "id_auth0_user")
    private String idAuth0User;

    @Column(name = "email")
    private String email;

    @Column(name = "blocked")
    private boolean blocked;

    @Column(name = "logged")
    private boolean logged;

    @Column(name = "idrol", nullable = true)
    private String userRoleId;

    @Column(name = "password")
    private String password;

}
