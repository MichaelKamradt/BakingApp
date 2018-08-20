package com.example.android.bakingapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.database.IngredientsDBModel;
import com.example.android.bakingapp.models.IngredientModel;

/**
 * Created by micha on 8/16/2018.
 */

public class IngredientAdapter extends ArrayAdapter<IngredientsDBModel> {

    // Set the member variables
    private Context mContext;
    private int resourceId;
    private IngredientsDBModel[] mIngredients;

    // Constructor for the Adapter
    public IngredientAdapter(Context context, int resourceId, IngredientsDBModel[] ingredients) {
        super(context, resourceId, ingredients);
        this.mContext = context;
        this.resourceId = resourceId;
        this.mIngredients = ingredients;
    }

    // Create the ViewHolder
    static class ViewHolder {
        TextView mIngredientTextView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Inflate the UI
        ViewHolder viewHolder = null;
        IngredientsDBModel currentIngredient = getItem(position);

        if (convertView == null) {
            // If it's not recycled, initialize some attributes
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mIngredientTextView = (TextView) convertView.findViewById(R.id.ingredient_grid_view_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();;
        }

        // Construct the String
        String ingredientQuantity = String.valueOf(currentIngredient.quantity);
        String ingredientMeasurement = currentIngredient.measure;
        String ingredientName = currentIngredient.ingredient;
        String ingredientString = ingredientQuantity + " " + ingredientMeasurement + " " + ingredientName;

        viewHolder.mIngredientTextView.setText(ingredientString);
        return convertView;
    }
}
