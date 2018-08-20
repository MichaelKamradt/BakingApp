package com.example.android.bakingapp.models;
import org.parceler.Parcel;

/**
 * Created by micha on 8/13/2018.
 */

@Parcel
public class StepModel {

    // Empty variables
    public int id;
    public String shortDescription;
    public String description;
    public String videoURL;
    public String thumbnailURL;

    // Empty constructor for serialization
    public StepModel() {
        // Nothing to see here
    }

    // Constructor for the model
    public StepModel(int id, String shortDescription, String description, String videoURL, String thumbnailURL) {

        // Assign the variables
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;

    }

}
