package com.example.android.bakingapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapters.RecipeAdapter;
import com.example.android.bakingapp.asynctasks.RecipeActivityAsyncTask;
import com.example.android.bakingapp.database.AppExecutors;
import com.example.android.bakingapp.database.DBUtils;
import com.example.android.bakingapp.database.IngredientsDatabase;
import com.example.android.bakingapp.database.RecipeDBModel;
import com.example.android.bakingapp.database.RecipeDatabase;
import com.example.android.bakingapp.database.RecipeStepsDatabase;
import com.example.android.bakingapp.database.StepsDBModel;
import com.example.android.bakingapp.models.RecipeModel;
import com.example.android.bakingapp.utils.GsonUtils;
import com.example.android.bakingapp.utils.NetworkUtils;

import org.parceler.Parcels;

public class RecipeActivity extends AppCompatActivity implements
        RecipeActivityAsyncTask.OnTaskCompleted,
        RecipeAdapter.recipeClickHandler {

    // TAG for logging purposes
    private static final String TAG = RecipeActivity.class.getSimpleName();

    // String for the intent key name
    public static final String RECIPE_ID = "recipe_id";

    // Layout member activities
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        // Get a reference to the Recycler View to show the content
        mRecyclerView = (RecyclerView) findViewById(R.id.recipe_list_recycler_view);

        // Set the Adapter
        mRecipeAdapter = new RecipeAdapter(this, this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        // Set the layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        // Get the data and inflate the RecyclerView from the database
        setUpRecipes();

        // Create the Async objects and go
        RecipeActivityAsyncTask recipeActivityAsyncTask = new RecipeActivityAsyncTask(RecipeActivity.this);
        recipeActivityAsyncTask.execute(NetworkUtils.buildRecipeUrl());
    }

    // Override the onTaskComplete from the AsyncTask
    @Override
    public void onTaskCompleted(String networkJsonResponse) {
        if (networkJsonResponse != null && !networkJsonResponse.equals("")) {
            // Get the Java objects from Gson
            RecipeModel[] recipesResult = GsonUtils.deserializeRecipeFromJson(networkJsonResponse);
            // Write the result to the DB
            DBUtils.writeDataToDatabases(RecipeDatabase.getsInstance(this),
                    RecipeStepsDatabase.getsInstance(this),
                    IngredientsDatabase.getsInstance(this),
                    recipesResult);
        }
    }

    // Set up the ViewModel for the steps
    private void setUpRecipes() {

        // Create an instance of a runnable that will query for the step on the IO thread
        AppExecutors.getsInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Query for the step. To be run on every fragment load
                // Not that I am not using LiveData as the data is not expected to change
                final RecipeDBModel[] recipes = RecipeDatabase.getsInstance(RecipeActivity.this).recipeDAO().loadRecipes();
                // Run on the main RecipeActivity thread
                RecipeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Send the result to the adapter
                        mRecipeAdapter.setRecipeResponseData(recipes);
                    }
                });
            }
        });
    }

    // Overriding the clickHandler
    @Override
    public void onRecipeItemClick(int clickedRecipeId) {
        // Create the Intent to handle the clickedRecipe
        Intent clickedRecipeIntent = new Intent(this, RecipeDetailActivity.class);

        // Add the clicked recipe parcel to the intent
        clickedRecipeIntent.putExtra(RECIPE_ID, clickedRecipeId);

        // Start the activity with the passed values
        startActivity(clickedRecipeIntent);
    }
}
