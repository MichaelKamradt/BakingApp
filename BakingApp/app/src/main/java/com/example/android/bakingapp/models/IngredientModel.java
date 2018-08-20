package com.example.android.bakingapp.models;

import org.parceler.Parcel;

/**
 * Created by micha on 8/13/2018.
 */

@Parcel
public class IngredientModel {

    // Empty variables
    public float quantity;
    public String measure;
    public String ingredient;

    // Empty constructor for serialization
    public IngredientModel() {
        // Nothing to see here
    }

    // Constructor for the model
    public IngredientModel(float quantity, String measure, String ingredient) {

        // Assign the variables
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;

    }

}
