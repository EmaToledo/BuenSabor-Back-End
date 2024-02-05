package com.elbuensabor.api.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "role")
@Getter
@Setter
@AttributeOverride(name = "id", column = @Column(name = "id_role"))
public class Role extends GenericEntity {
    @Column(name = "denomination")
    private String denomination;

    @Column(name = "id_auth0_role")
    private String idAuth0Role;
}
