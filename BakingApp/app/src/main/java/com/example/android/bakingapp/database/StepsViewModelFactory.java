package com.example.android.bakingapp.database;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * Created by micha on 8/18/2018.
 */

public class StepsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    // Variables for the DB and the specific recipe ID
    private RecipeStepsDatabase mDB;
    private int mRecipeId;

    // Constructor
    public StepsViewModelFactory(RecipeStepsDatabase dB, int recipeId) {
        mDB = dB;
        mRecipeId = recipeId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new StepsViewModel(mDB, mRecipeId);
    }
}
