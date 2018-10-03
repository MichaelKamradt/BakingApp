package com.example.android.bakingapp.widget;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activities.RecipeDetailActivity;
import com.example.android.bakingapp.database.IngredientsDBModel;
import com.example.android.bakingapp.database.IngredientsDatabase;
import com.example.android.bakingapp.database.RecipeDBModel;
import com.example.android.bakingapp.database.RecipeDatabase;

/**
 * Created by micha on 8/23/2018.
 */

// Service that will run the GridView factory
public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientGridRemoteViewFactory(this.getApplicationContext());
    }
}

class IngredientGridRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    // Key for recipe Id
    private static final String RECIPE_ID = "recipe_id";

    // Get the member variables for the context and database
    private Context mContext;
    private IngredientsDBModel[] mIngredientsObjects;

    // Constructor
    public IngredientGridRemoteViewFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    // Empty
    @Override
    public void onCreate() {
    }

    // Called when the data set changes
    @Override
    public void onDataSetChanged() {
        // Create the Async objects and go
        mIngredientsObjects = IngredientsDatabase.getsInstance(mContext).ingredientsDAO().loadIngredientsNonLiveData(1);
        Log.d(mContext.getClass().getSimpleName(), "Able to get the values: " + mIngredientsObjects.length);
    }

    // Get the count of the network output of the recipe model
    @Override
    public int getCount() {
        int ingredientSize;
        if (mIngredientsObjects == null || mIngredientsObjects.length == 0) {
            ingredientSize = 0;
        } else {
            // Get the count of the recipe count
            ingredientSize = mIngredientsObjects.length;
        }
        // Return the size
        return ingredientSize;
    }

    // Version of on bind view holder, but for RemoteViews!
    @Override
    public RemoteViews getViewAt(int position) {

        // Get the remote view object
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_widget_item);

        // Get the position of the RecyclerView item
        IngredientsDBModel ingredient = mIngredientsObjects[position];

        // Add the recipe name to the text view
        String ingreidentName = ingredient.ingredient;
        views.setTextViewText(R.id.recipe_widget_item_name, ingreidentName);

        // Set the pending intent to handle the click and open the detail view
        Intent fillinIntent = new Intent();
        fillinIntent.putExtra(RecipeDetailActivity.RECIPE_ID, ingredient.recipeId);
        views.setOnClickFillInIntent(R.id.recipe_widget_item_name, fillinIntent);

        // Finally, return the views
        return views;
    }

    // Things that have to be overridden
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
