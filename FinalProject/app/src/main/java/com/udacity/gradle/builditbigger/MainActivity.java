package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.androidjokeslib.AndroidJokeActivity;
import com.example.android.javajokes.ReallyFunnyJokes;


public class MainActivity extends AppCompatActivity  implements EndpointsAsyncTask.OnTaskCompleted {

    // Member variable of the joke from the joke Java class
    public static final String EXTRA_JOKE = "EXTRA_JOKE";

    // Variable for the Button
    public static Button mJokeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the button
        mJokeButton = (Button) findViewById(R.id.joke_button);

        // Create the click listener
        mJokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Upon click, make a call to the endpoint, passing in the context and a name
                EndpointsAsyncTask endpointsAsyncTask = new EndpointsAsyncTask(MainActivity.this);
                endpointsAsyncTask.execute(MainActivity.this);
            }
        });
    }

    // Override the onTaskComplete from the AsyncTask
    @Override
    public void onTaskCompleted(String apiResponse) {
        // Set the joke intent that opens up the joke from the Android lib
        Intent jokeIntent = new Intent(MainActivity.this, AndroidJokeActivity.class);
        jokeIntent.putExtra(EXTRA_JOKE, apiResponse);
        startActivity(jokeIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
