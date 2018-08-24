package com.example.android.bakingapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.example.android.bakingapp.models.StepModel;

/**
 * Created by micha on 8/14/2018.
 */
@Entity(tableName = "recipeSteps")
public class StepsDBModel {

    // Empty variables
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int stepIndex;
    public int stepId;
    public int recipeId;
    public String recipeName;
    public String shortDescription;
    public String description;
    public String videoURL;
    public String thumbnailURL;

    // Empty constructor for serialization
    public StepsDBModel() {
        // Nothing to see here
    }

    // Constructor for the model
    public StepsDBModel(int stepIndex, int stepId, int recipeId, String recipeName, String shortDescription, String description, String videoURL, String thumbnailURL) {

        // Assign the variables
        this.stepIndex = stepIndex;
        this.stepId = stepId;
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;

    }

    // Constructor for the model
    public static StepsDBModel createStepsDBModel(StepModel stepModel, int recipeId, int stepIndex, String recipeName) {

        // Create the stepDBmodel object
        StepsDBModel stepsDBModel = new StepsDBModel(
                stepIndex,
                stepModel.id,
                recipeId,
                recipeName,
                stepModel.shortDescription,
                stepModel.description,
                stepModel.videoURL,
                stepModel.thumbnailURL
        );

        // Return the constructed DB model
        return stepsDBModel;
    }


}
