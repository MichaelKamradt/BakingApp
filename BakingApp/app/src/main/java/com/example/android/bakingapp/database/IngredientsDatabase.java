package com.example.android.bakingapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

/**
 * Created by micha on 8/16/2018.
 */

@Database(entities = {IngredientsDBModel.class}, version = 3, exportSchema = false)
public abstract class IngredientsDatabase extends RoomDatabase {

    // For logging identification
    private static final String LOG_TAG = IngredientsDatabase.class.getSimpleName();
    // Create an empty object
    private static final Object LOCK = new Object();
    // Variable for the database name
    private static final String DATABASE_NAME = "recipeIngredients";
    // Instance of the database
    private static IngredientsDatabase sInstance;

    // Function to get the instance
    public static IngredientsDatabase getsInstance(Context context) {
        // If it is null, create it. Otherwise, get the current instance
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance.");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        IngredientsDatabase.class, IngredientsDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the preexisting database instance");
        return sInstance;
    }

    public abstract ingredientsDAO ingredientsDAO();

}
