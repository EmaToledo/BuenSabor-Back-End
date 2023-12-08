package com.elbuensabor.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recipe_step")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "id_recipe_step"))
public class RecipeStep extends GenericEntity {

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "idRecipe")
    @JsonBackReference
    private Recipe recipe;

}
