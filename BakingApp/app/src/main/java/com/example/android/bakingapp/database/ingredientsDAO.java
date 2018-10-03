package com.example.android.bakingapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

/**
 * Created by micha on 8/16/2018.
 */

// Dao for the ingredients database
@Dao
public interface ingredientsDAO {

    @Query("SELECT * FROM recipeIngredients WHERE recipeId = :recipeId ORDER BY id ASC")
    LiveData<IngredientsDBModel[]> loadIngredients(int recipeId);

    @Query("SELECT * FROM recipeIngredients WHERE recipeId = :recipeId ORDER BY id ASC")
    IngredientsDBModel[] loadIngredientsNonLiveData(int recipeId);

    @Insert
    void insertIngredient(IngredientsDBModel ingredientsDBModel);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateIngredient(IngredientsDBModel ingredientsDBModel);

    @Delete
    void deleteIngredient(IngredientsDBModel ingredientsDBModel);

    @Query("SELECT * FROM recipeIngredients WHERE ingredientsIndex = :ingredientsIndex AND recipeId = :recipeId")
    IngredientsDBModel getSingleIngredient(int ingredientsIndex, int recipeId);

}
