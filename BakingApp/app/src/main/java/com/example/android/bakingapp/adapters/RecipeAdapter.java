package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.database.RecipeDBModel;
import com.example.android.bakingapp.models.RecipeModel;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by micha on 8/13/2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.recipeViewHolder> {

    // TAG variable for debugging
    private final String TAG = RecipeAdapter.class.getSimpleName();

    // Adapter ID
    public final int ADAPTER_ID = 88;

    // Context member variable
    private final Context mContext;

    // Interface that receives the ID of the recipe
    final private recipeClickHandler mRecipeClickHandler;

    // String array that contains the movie JSON objects
    private RecipeDBModel[] mRecipes;

    // Create an interface for the RecipeActivity click handler
    public interface recipeClickHandler {
        void onRecipeItemClick(int clickedRecipeId);
    }

    // Create the StepAdapter
    public RecipeAdapter(@NonNull Context context, recipeClickHandler clickHandler) {
        mContext = context;
        mRecipeClickHandler = clickHandler;
    }

    // Called when the view holder is created and the recyclerView is laid out
    @Override
    public recipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Context variable
        Context context = parent.getContext();

        // Get the ID of the tile
        int layoutId;
        layoutId = R.layout.recipe_item;

        // Inflate the tile template
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        view.setFocusable(true);
        // Return the view
        return new recipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(recipeViewHolder holder, final int position) {
        // Get the position of the RecyclerView item
        RecipeDBModel recipe = mRecipes[position];

        // Add the recipe name to the text view
        String recipeName = recipe.name;
        // Logging for making sure it is coming through
        Log.d(TAG, "Name of the Recipe: " + recipeName);
        holder.mRecipeName.setText(recipeName);

    }

    // Get item function. Return 0 if the cursor is null
    @Override
    public int getItemCount() {
        if (mRecipes == null || mRecipes.length == 0) {
            return 0;
        } else {
            // Get the count of the recipe count
            int recipeSize = mRecipes.length;

            // Return the size
            return recipeSize;
        }
    }

    // Create a ViewHolder and place inside a ClickListener
    class recipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Member variable for the image/text/card view
        private TextView mRecipeName;

        // ViewHolder class
        public recipeViewHolder(View recipeView) {
            super(recipeView);

            // Define the text view
            mRecipeName = (TextView) recipeView.findViewById(R.id.recipe_item_name);

            // Pass the context to the click listener
            recipeView.setOnClickListener(this);
        }

        // Upon click, get the position of the recipe within the list, and pass the recipe object
        @Override
        public void onClick(View view) {
            // Create logic to open the recipe detail intent

            // Get the current position
            int adapterPosition = getAdapterPosition();

            // Get the recipe in that position of the adapter
            RecipeDBModel clickedRecipeModel = mRecipes[adapterPosition];

            // Pass it to the click handler interface
            mRecipeClickHandler.onRecipeItemClick(clickedRecipeModel.id);
        }
    }

    // Used to set the new data if there is new data. No need to reconstruct the StepAdapter
    public void setRecipeResponseData(RecipeDBModel[] recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }

}
