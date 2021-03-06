package com.example.android.bakingapp.database;

import android.util.Log;

import com.example.android.bakingapp.models.IngredientModel;
import com.example.android.bakingapp.models.RecipeModel;
import com.example.android.bakingapp.models.StepModel;

/**
 * Created by micha on 8/14/2018.
 */

public class DBUtils {

    // Handler to write all of them
    public static void writeDataToDatabases(final RecipeDatabase recipeDatabase,
                                     final RecipeStepsDatabase recipeStepsDatabase,
                                     final IngredientsDatabase recipeIngredientsDatabase,
                                     RecipeModel[] recipes) {
        writeRecipesToDatabase(recipeDatabase, recipes);
        writeStepsToDatabase(recipeStepsDatabase, recipes);
        writeIngredientsToDatabase(recipeIngredientsDatabase, recipes);
    }

    // Handler to take the recipes and write them to the DB
    public static void writeRecipesToDatabase(final RecipeDatabase recipeDatabase, RecipeModel[] recipes) {

        // Loop through the recipes to get the steps
        for (int i = 0; i < recipes.length; i++) {
            // Recipe ID and Name
            final int recipeId = recipes[i].id;
            final String recipeName = recipes[i].name;
            final int recipeServings = recipes[i].servings;
            final String recipeImage = recipes[i].image;
            // Make the recipe object
            final RecipeDBModel recipeDBModel = new RecipeDBModel(recipeId, i, recipeName, recipeImage, recipeServings);
            // Write the Step to the DB
            AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    if (recipeDatabase.recipeDAO().getSingleRecipe(recipeId) == null) {
                        recipeDatabase.recipeDAO().insertStep(recipeDBModel);
                    } else {
                        Log.d("Adding Recipe item", "Recipe already added" + recipeId);
                    }
                }
            });
            }
    }

    // Handler to take the StepModels, Recipe ID, and write them to the DB
    public static void writeStepsToDatabase(final RecipeStepsDatabase recipeStepsDatabase, RecipeModel[] recipes) {

        // Loop through the recipes to get the steps
        for (int i = 0; i < recipes.length; i++) {
            // Recipe ID and Name
            final int recipeId = recipes[i].id;
            final String recipeName = recipes[i].name;
            // Steps of the recipe
            StepModel[] steps = recipes[i].steps;
            // Loop through the steps and add them to the DB
            for (int stepI = 0; stepI < steps.length; stepI++) {
                // Step index
                final int stepIndex = stepI;
                // Create the StepDBModel
                final StepsDBModel stepsDBModel = StepsDBModel.createStepsDBModel(steps[stepI], recipeId, stepIndex, recipeName);
                // Write the Step to the DB
                AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (recipeStepsDatabase.stepsDao().getSingleStep(recipeId, stepIndex) == null) {
                            recipeStepsDatabase.stepsDao().insertStep(stepsDBModel);
                        } else {
                            Log.d("Adding Step item", "Step already added" + stepIndex + " REC: " + recipeId);
                        }
                    }
                });
            }
        }
    }


    // Handler to take the IngredientModels, Recipe ID, and write them to the DB
    public static void writeIngredientsToDatabase(final IngredientsDatabase recipeIngredientsDatabase, RecipeModel[] recipes) {

        // Loop through the recipes to get the steps
        for (int i = 0; i < recipes.length; i++) {
            // Recipe ID
            final int recipeId = recipes[i].id;
            // Ingredients of the recipe
            IngredientModel[] ingredientModels = recipes[i].ingredients;
            // Loop through the ingredients and add them to the DB
            for (int iI = 0; iI < ingredientModels.length; iI++) {
                // Ing. index
                final int ingredientIndex = iI;
                // Create the IngredientDBModel
                final IngredientsDBModel ingredientsDBModel = IngredientsDBModel.createIngredientsDBModel(ingredientIndex, recipeId, ingredientModels[iI]);
                // Write the Ingredient to the DB
                AppExecutors.getsInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (recipeIngredientsDatabase.ingredientsDAO().getSingleIngredient(ingredientIndex, recipeId) == null) {
                            recipeIngredientsDatabase.ingredientsDAO().insertIngredient(ingredientsDBModel);
                        } else {
                            Log.d("Adding Ingredient item", "Ingredient already added");
                        }
                    }
                });
            }
        }
    }
}
