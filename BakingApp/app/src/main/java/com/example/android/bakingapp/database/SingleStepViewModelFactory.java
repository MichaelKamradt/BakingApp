package com.example.android.bakingapp.database;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * Created by micha on 8/18/2018.
 */

public class SingleStepViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    // Variables for the DB and the specific recipe ID
    private RecipeStepsDatabase mDB;
    private int mRecipeId;
    private int mStepId;

    // Constructor
    public SingleStepViewModelFactory(RecipeStepsDatabase dB, int recipeId, int stepId) {
        mDB = dB;
        mRecipeId = recipeId;
        mStepId = stepId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SingleStepViewModel(mDB, mRecipeId, mStepId);
    }

}
