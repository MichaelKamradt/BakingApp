package com.example.android.bakingapp.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

/**
 * Created by micha on 8/16/2018.
 */

// View Model for the Ingredients
public class IngredientsViewModel extends AndroidViewModel {

    // Add logging tag
    private static final String TAG = IngredientsViewModel.class.getSimpleName();

    // Member variable that handles the "get all" method
    LiveData<IngredientsDBModel[]> mIngredients;

    // Constructor for getting all of the tasks
    public IngredientsViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "Just made a reference to a view model: INGREDIENTS");
    }

    // Getter function for the ingredients that belong to that activity
    public LiveData<IngredientsDBModel[]> getmIngredients(int recipeId) {
        // Database instance
        IngredientsDatabase ingredientsDatabase = IngredientsDatabase.getsInstance(this.getApplication());
        // Getting all ingredients
        mIngredients = ingredientsDatabase.ingredientsDAO().loadIngredients(recipeId);
        // Return
        return mIngredients;
    }

}
