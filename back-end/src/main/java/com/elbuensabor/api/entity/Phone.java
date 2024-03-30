package com.elbuensabor.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "phone")
@Getter
@Setter
@AttributeOverride(name = "id", column = @Column(name = "id_phone"))
public class Phone extends GenericEntity{
    @Column(name = "phone")
    private Long phone;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;
}
