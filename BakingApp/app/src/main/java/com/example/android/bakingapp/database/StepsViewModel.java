package com.example.android.bakingapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

/**
 * Created by micha on 8/14/2018.
 */

public class StepsViewModel extends ViewModel {

    // Add logging tag
    private static final String TAG = StepsViewModel.class.getSimpleName();

    // Member variable that handles the "get all" and "get single" methods
    LiveData<StepsDBModel[]> mSteps;

    // Constructor for getting all of the tasks
    public StepsViewModel(RecipeStepsDatabase dB, int recipeId) {
        Log.d(TAG, "Just made a reference to a view model: STEPS");
        // Getting all ingredients
        mSteps = dB.stepsDao().loadRecipeSteps(recipeId);
    }

    // Getter function for the ingredients that belong to that activity
    public LiveData<StepsDBModel[]> getmSteps() {
        // Return
        return mSteps;
    }

}
