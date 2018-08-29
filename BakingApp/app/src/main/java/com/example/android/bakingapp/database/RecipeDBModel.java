package com.example.android.bakingapp.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.android.bakingapp.models.IngredientModel;
import com.example.android.bakingapp.models.StepModel;

/**
 * Created by micha on 8/27/2018.
 */

@Entity(tableName = "recipeDBObjects")
public class RecipeDBModel {

    // Empty variables
    public int id;
    @PrimaryKey
    public int recipeId;
    public String name;
    public String image;
    public int servings;

    // Empty constructor for serialization
    public RecipeDBModel() {
        // Nothing to see here
    }

    // Constructor for the model
    public RecipeDBModel(int id, int recipeId, String name, String image, int servings) {

        // Assign the variables
        this.id = id;
        this.recipeId = recipeId;
        this.name = name;
        this.image = image;
        this.servings = servings;

    }

}
