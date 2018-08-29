package com.example.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.RecipeActivity;
import com.example.android.bakingapp.activities.RecipeDetailActivity;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, getRecipeRemoteView(context));
    }

    // Method that will create the grid view for the remote view
    private static RemoteViews getRecipeRemoteView(Context context) {
        // Get a remote view
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        // Get the intent for the refresh service
        Intent refreshServiceIntent = new Intent(context, RefreshRecipeListService.class);
        // Set the action
        refreshServiceIntent.setAction(RefreshRecipeListService.REFRESH_ACTION);

        // Wrap the intent in a pending intent for the widget to interact with
        // 0 are for the flags
        PendingIntent refreshServicePendingIntent = PendingIntent.getService(context, 0, refreshServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Set the click listener on the refresh button view
        views.setOnClickPendingIntent(R.id.refresh_button, refreshServicePendingIntent);

        // Get the intent for the grid class and set it as the adapter
        Intent gridClassIntent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.recipe_widget_grid_view, gridClassIntent);

        // Set the pending intent to launch the main activity
        Intent recipeAppIntent = new Intent(context, RecipeDetailActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, recipeAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.recipe_widget_grid_view, pendingIntent);

        // Return the view
        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        RefreshRecipeListService.startRefreshAction(context);
    }

    // Method that will start the intents
    public static void updateWidgets(Context context, AppWidgetManager appWidgetManager, int[] widgetIds) {
        for (int widgetId : widgetIds) {
            updateAppWidget(context, appWidgetManager, widgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

