package com.example.android.bakingapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.util.Pair;

import com.example.android.bakingapp.models.StepModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by micha on 8/14/2018.
 */
// Creating the DAO that will contain the IO functions for the steps DB

@Dao
public interface stepsDAO {

    @Query("SELECT * FROM recipeSteps WHERE recipeId = :recipeId ORDER BY id ASC")
    LiveData<StepsDBModel[]> loadRecipeSteps(int recipeId);

    @Insert
    void insertStep(StepsDBModel stepModel);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateStep(StepsDBModel stepModel);

    @Delete
    void deleteStep(StepsDBModel stepModel);

    @Query("SELECT * FROM recipeSteps WHERE recipeId = :recipeId AND stepIndex = :stepIndex")
    StepsDBModel getSingleStep(int recipeId, int stepIndex);

}
