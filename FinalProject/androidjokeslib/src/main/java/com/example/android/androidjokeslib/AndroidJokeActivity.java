package com.example.android.androidjokeslib;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AndroidJokeActivity extends AppCompatActivity {

    // Member variable for the text view
    public static TextView mJokeTextView;
    public static final String EXTRA_JOKE = "EXTRA_JOKE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android_joke);

        // Get the joke string
        Intent jokeIntent = getIntent();
        String mainActivityJoke = jokeIntent.getStringExtra(EXTRA_JOKE);

        // Initialize the TextView and set the text
        mJokeTextView = (TextView) findViewById(R.id.joke_text_view);
        mJokeTextView.setText(mainActivityJoke);

    }
}
