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
import com.example.android.bakingapp.database.StepsDBModel;
import com.example.android.bakingapp.models.RecipeModel;
import com.example.android.bakingapp.models.StepModel;

/**
 * Created by micha on 8/13/2018.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.stepViewHolder> {

    // TAG variable for debugging
    private final String TAG = RecipeAdapter.class.getSimpleName();

    // Adapter ID
    public final int ADAPTER_ID = 88;

    // Context member variable
    private final Context mContext;

    // Interface that receives the ID of the recipe
    final private StepAdapter.stepClickHandler mStepClickHandler;

    // String array that contains the movie JSON objects
    private StepsDBModel[] mSteps;

    // Create an interface for the RecipeActivity click handler
    public interface stepClickHandler {
        void onStepItemClick(int clickedStepId);
    }

    // Create the StepAdapter
    public StepAdapter(@NonNull Context context, stepClickHandler clickHandler) {
        mContext = context;
        mStepClickHandler = clickHandler;
    }

    // Called when the view holder is created and the recyclerView is laid out
    @Override
    public StepAdapter.stepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Context variable
        Context context = parent.getContext();

        // Get the ID of the tile
        int layoutId;
        layoutId = R.layout.step_item;

        // Inflate the tile template
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        view.setFocusable(true);
        // Return the view
        return new StepAdapter.stepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepAdapter.stepViewHolder holder, final int position) {
        // Get the position of the RecyclerView item
        StepsDBModel step = mSteps[position];

        // Add the recipe name to the text view
        String stepShortDescription = step.shortDescription;
        // Logging for making sure it is coming through
        Log.d(TAG, "Recipe Step ID Shown: " + step.id);
        holder.mShortDescription.setText(stepShortDescription);

    }

    // Get item function. Return 0 if the cursor is null
    @Override
    public int getItemCount() {
        if (mSteps == null || mSteps.length == 0) {
            return 0;
        } else {
            // Get the count of the recipe count
            int recipeSize = mSteps.length;

            // Return the size
            return recipeSize;
        }
    }

    // Create a ViewHolder and place inside a ClickListener
    class stepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Member variable for the image/text/card view
        private TextView mShortDescription;

        // ViewHolder class
        public stepViewHolder(View stepView) {
            super(stepView);

            // Define the text view
            mShortDescription = (TextView) stepView.findViewById(R.id.step_short_description_text_view);

            // Pass the context to the click listener
            stepView.setOnClickListener(this);
        }

        // Upon click, get the position of the recipe within the list, and pass the recipe object
        @Override
        public void onClick(View view) {
            // Create logic to open the recipe detail intent

            // Get the current position
            int adapterPosition = getAdapterPosition();

            // Get the recipe in that position of the adapter
            StepsDBModel clickedStepModel = mSteps[adapterPosition];

            // Pass it to the click handler interface
            mStepClickHandler.onStepItemClick(clickedStepModel.stepIndex);
        }
    }

    // Used to set the new data if there is new data. No need to reconstruct the StepAdapter
    public void setStepData(StepsDBModel[] steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

}
