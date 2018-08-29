package com.example.android.bakingapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

/**
 * Created by micha on 8/27/2018.
 */

@Dao
public interface recipeDAO {

    @Query("SELECT * FROM recipeDBObjects ORDER BY id ASC")
    RecipeDBModel[] loadRecipes();

    @Insert
    void insertStep(RecipeDBModel stepModel);

    @Query("SELECT * FROM recipeDBObjects WHERE id = :recipeId")
    RecipeDBModel getSingleRecipe(int recipeId);

}
