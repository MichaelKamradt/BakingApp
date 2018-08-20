package com.example.android.bakingapp.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by micha on 8/13/2018.
 */

public class NetworkUtils {

    // Getting a string TAG for simple debugging purposes
    private static final String TAG = NetworkUtils.class.getSimpleName();

    // Building blocks of the API calls / link constructor
    private static final String RECIPE_PROJECT_URL_STRING = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    // Build Single Movie URL, to be used in the Detail View
    public static URL buildRecipeUrl() {
        // Try the URL builder and catch the exception
        try {
            URL finalRecipeUrl = new URL(RECIPE_PROJECT_URL_STRING);
            Log.v(TAG, "URL: " + finalRecipeUrl);
            return finalRecipeUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Create the function that gets the JSON object from Recipe API
    public static String getRecipeApiHttpResponse(URL url) throws IOException {
        // Open the connection
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            // Create an input stream for the data to get into the phone
            InputStream in = urlConnection.getInputStream();

            // Create an scanner that looks for "next" objects, looking at the data from the inoput stream
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            // If it has next, keep going. Else, close it
            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;

        } finally {
            // Disconnect the connection
            urlConnection.disconnect();
        }
    }
}
