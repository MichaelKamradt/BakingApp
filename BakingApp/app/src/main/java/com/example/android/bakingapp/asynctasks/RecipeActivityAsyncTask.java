package com.example.android.bakingapp.asynctasks;

import android.os.AsyncTask;

import com.example.android.bakingapp.utils.NetworkUtils;

import java.io.IOException;

import java.net.URL;

/**
 * Created by micha on 8/13/2018.
 */

public class RecipeActivityAsyncTask extends AsyncTask<URL, Void, String> {

    // Get an onTaskCompleted Object
    private OnTaskCompleted mOnTaskCompleted;

    // Class constructor for the AsyncTask
    public RecipeActivityAsyncTask(OnTaskCompleted activityContext) {
        this.mOnTaskCompleted = activityContext;
    }

    @Override
    protected String doInBackground(URL... urls) {
        // Get the Recipe Url
        URL recipeUrl = urls[0];

        // Initialized response
        String recipeStringResponse = null;

        // Make the Network call and catch the IOException
        try {
            recipeStringResponse = NetworkUtils.getRecipeApiHttpResponse(recipeUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the results
        return recipeStringResponse;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mOnTaskCompleted.onTaskCompleted(s);
    }

    // Inferface to handle what happens when the task is done
    public interface OnTaskCompleted {
        void onTaskCompleted(String networkJsonResponse);
    }

}
