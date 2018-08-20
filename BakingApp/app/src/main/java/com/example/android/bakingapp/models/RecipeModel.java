package com.example.android.bakingapp.models;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by micha on 8/13/2018.
 */

@Parcel
public class RecipeModel {

    // Empty variables
    public int id;
    public String name;
    public IngredientModel[] ingredients;
    public StepModel[] steps;
    public String image;
    public int servings;

    // Empty constructor for serialization
    public RecipeModel() {
        // Nothing to see here
    }

    // Constructor for the model
    public RecipeModel(int id, String name, IngredientModel[] ingredients, StepModel[] steps, String image, int servings) {

        // Assign the variables
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.image = image;
        this.servings = servings;

    }

}
