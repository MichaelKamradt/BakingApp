package com.example.android.bakingapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.example.android.bakingapp.models.StepModel;

/**
 * Created by micha on 8/14/2018.
 */

// Annotate the Recipe Steps DB with the proper Android Components
@Database(entities = {StepsDBModel.class}, version = 4, exportSchema = false)
public abstract class RecipeStepsDatabase extends RoomDatabase {

    // For logging identification
    private static final String LOG_TAG = RecipeStepsDatabase.class.getSimpleName();
    // Create an empty object
    private static final Object LOCK = new Object();
    // Variable for the database name
    private static final String DATABASE_NAME = "recipeSteps";
    // Instance of the database
    private static RecipeStepsDatabase sInstance;

    // Function to get the instance
    public static RecipeStepsDatabase getsInstance(Context context) {
        // If it is null, create it. Otherwise, get the current instance
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance.");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        RecipeStepsDatabase.class, RecipeStepsDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the preexisting database instance");
        return sInstance;
    }

    public abstract stepsDAO stepsDao();

}
