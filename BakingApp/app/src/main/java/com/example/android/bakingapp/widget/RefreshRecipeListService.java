package com.example.android.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.RecipeActivity;
import com.example.android.bakingapp.asynctasks.RecipeActivityAsyncTask;
import com.example.android.bakingapp.database.DBUtils;
import com.example.android.bakingapp.database.IngredientsDatabase;
import com.example.android.bakingapp.database.RecipeDatabase;
import com.example.android.bakingapp.database.RecipeStepsDatabase;
import com.example.android.bakingapp.models.RecipeModel;
import com.example.android.bakingapp.utils.GsonUtils;
import com.example.android.bakingapp.utils.NetworkUtils;

/**
 * Created by micha on 8/23/2018.
 */

// Intent service to handle refreshing the recipe list from the endpoint
// Though it won't necessarily change, it is a POC for a practical use
public class RefreshRecipeListService extends IntentService implements
        RecipeActivityAsyncTask.OnTaskCompleted {

    // Default constructor
    public RefreshRecipeListService() {
        super("RefreshRecipeListService");
    }

    // Defining the name of our refresh action
    public static final String REFRESH_ACTION = "com.example.android.bakingapp.action.refresh";

    // Handler to start the service
    public static void startRefreshAction(Context context) {
        // Intent the starts the service from within the service
        Intent refreshServiceIntent = new Intent(context, RefreshRecipeListService.class);
        // Set the action
        refreshServiceIntent.setAction(REFRESH_ACTION);
        // Start the service
        context.startService(refreshServiceIntent);
    }

    // Get an intent to handle the action
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            // Get the action name
            final String action = intent.getAction();
            if (REFRESH_ACTION.equals(action)) {
                handleRefreshAction();
            }
        }
    }

    // Actually create the action
    public void handleRefreshAction() {
        // Create the Async objects and go
        RecipeActivityAsyncTask recipeActivityAsyncTask = new RecipeActivityAsyncTask(this);
        recipeActivityAsyncTask.execute(NetworkUtils.buildRecipeUrl());

        // Get an instance of the app widget manager
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        // Get the App Widget Ids
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        // Notify that the data has changed
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_widget_grid_view);
        // Call refresh on the widget
        RecipeWidget.updateWidgets(this, appWidgetManager, appWidgetIds);
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
}
