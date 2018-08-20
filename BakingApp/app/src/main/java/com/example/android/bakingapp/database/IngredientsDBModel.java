package com.example.android.bakingapp.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.android.bakingapp.models.IngredientModel;
/**
 * Created by micha on 8/16/2018.
 */

@Entity(tableName = "recipeIngredients")
public class IngredientsDBModel {

    // Empty variables
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int ingredientsIndex;
    public int recipeId;
    public float quantity;
    public String measure;
    public String ingredient;

    // Empty constructor for serialization
    public IngredientsDBModel() {
        // Nothing to see here
    }

    // Constructor for the model
    public IngredientsDBModel(int ingredientsIndex, int recipeId, float quantity, String measure, String ingredient) {

        // Assign the variables
        this.ingredientsIndex = ingredientsIndex;
        this.recipeId = recipeId;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;

    }

    // Constructor for the model
    public static IngredientsDBModel createIngredientsDBModel(int ingredientsIndex, int recipeId, IngredientModel ingredientModel) {

        // Create the stepDBmodel object
        IngredientsDBModel ingredientsDBModel = new IngredientsDBModel(
                ingredientsIndex,
                recipeId,
                ingredientModel.quantity,
                ingredientModel.measure,
                ingredientModel.ingredient
        );

        // Return the constructed DB model
        return ingredientsDBModel;
    }

}
