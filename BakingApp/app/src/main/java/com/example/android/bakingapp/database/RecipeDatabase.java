package com.example.android.bakingapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

/**
 * Created by micha on 8/27/2018.
 */

// To contain the Metadata of the recipe
@Database(entities = {RecipeDBModel.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

    // For logging identification
    private static final String LOG_TAG = RecipeStepsDatabase.class.getSimpleName();
    // Create an empty object
    private static final Object LOCK = new Object();
    // Variable for the database name
    private static final String DATABASE_NAME = "recipeDBObjects";
    // Instance of the database
    private static RecipeDatabase sInstance;

    // Function to get the instance
    public static RecipeDatabase getsInstance(Context context) {
        // If it is null, create it. Otherwise, get the current instance
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance.");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        RecipeDatabase.class, RecipeDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the preexisting database instance");
        return sInstance;
    }

    public abstract recipeDAO recipeDAO();

}
