package com.example.android.bakingapp.utils;

import android.util.Log;

import com.example.android.bakingapp.models.RecipeModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * Created by micha on 8/13/2018.
 */

public class GsonUtils {

    // Create a function that turns the JSON String into a Java object
    public static RecipeModel[] deserializeRecipeFromJson(String networkJsonResponse) {
        // Gson object
        Gson gson = new Gson();
        // Get an array of the
        RecipeModel[] recipeArray = gson.fromJson(networkJsonResponse, RecipeModel[].class);
        return recipeArray;
    }

}
