package com.elbuensabor.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ingredient_recipe_link")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "id_ingredient_recipe_link"))
public class IngredientRecipeLink extends GenericEntity {

    @ManyToOne
    @JoinColumn(name = "idRecipe")
    @JsonBackReference
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "idIngredient")
    @JsonBackReference
    private Ingredient ingredient;

    @Column(name = "quantity")
    private Long quantity;

}
