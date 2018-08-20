package com.example.android.bakingapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

/**
 * Created by micha on 8/18/2018.
 */

public class SingleStepViewModel extends ViewModel {

    // Add logging tag
    private static final String TAG = StepsViewModel.class.getSimpleName();

    // Member variable that handles the "get all" and "get single" methods
    StepsDBModel mSingleStep;

    // Constructor for getting all of the tasks
    public SingleStepViewModel(RecipeStepsDatabase dB, int recipeId, int stepId) {
        Log.d(TAG, "Just made a reference to a view model: STEPS");
        // Getting all ingredients
        mSingleStep = dB.stepsDao().getSingleStep(recipeId, stepId);
    }

    // Getter function for the ingredients that belong to that activity
    public StepsDBModel getmSingleStep() {
        // Return
        return mSingleStep;
    }

}
