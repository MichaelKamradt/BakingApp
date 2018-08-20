package com.example.android.bakingapp.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragments.ButtonFragment;
import com.example.android.bakingapp.fragments.StepDetailFragment;

public class StepDetailActivity extends AppCompatActivity implements ButtonFragment.onStepIdChange {

    // TAG for logging
    private static final String TAG = StepDetailActivity.class.getSimpleName();

    // Key for the recipe Id
    private static final String RECIPE_ID = "recipe_id";
    private static final String NUM_STEPS = "num_steps";
    private static final String STEP_ID = "step_id";

    // Member Step variable
    private int mNumSteps;
    private int mStepId;
    private int mRecipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);

        // Get the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get the instant state logic, start anew if it isn't there
        if (savedInstanceState != null) {
            mRecipeId = savedInstanceState.getInt(RECIPE_ID);
            mStepId = savedInstanceState.getInt(STEP_ID);
            mNumSteps = savedInstanceState.getInt(NUM_STEPS);
        } else {
            // Get the intent data
            Intent passedIntent = getIntent();

            // Ensure it has the right extras
            if (passedIntent.hasExtra(RECIPE_ID) &&
                    passedIntent.hasExtra(STEP_ID) &&
                    passedIntent.hasExtra(NUM_STEPS)) {

                // Member variables for passing to the next intent
                // Subtracting 1 as the IDs of the steps follows an index starting at 0, not 1
                mNumSteps = passedIntent.getIntExtra(NUM_STEPS, 0);
                mStepId = passedIntent.getIntExtra(STEP_ID, 0);
                mRecipeId = passedIntent.getIntExtra(RECIPE_ID, 0);
            } else {
                Log.d(TAG, "The recipe was not loaded properly");
            }
        }
        // Run the UI Logic
        instantiateStepDetailFragments(mStepId);
        // Set up the buttons
        instantiateButtonFragments();
    }

    // Instantiate Step detail Fragments
    public void instantiateStepDetailFragments(int stepId) {
        // Load the long instructions in the fragment
        StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(mRecipeId, stepId);

        // Get a fragment manager to handle the fragment
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin the transaction and commit the fragment
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_step_detail_text_video, stepDetailFragment)
                .commit();
    }

    // Instantiate Button Fragments
    public void instantiateButtonFragments() {
        // Load the long instructions in the fragment
        ButtonFragment buttonFragment = ButtonFragment.newInstance(mStepId, mNumSteps);

        // Get a fragment manager to handle the fragment
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin the transaction and commit the fragment
        fragmentManager.beginTransaction()
                .replace(R.id.button_fragment_view, buttonFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Create the Intent to handle the clickedRecipe
            Intent clickedRecipeIntent = new Intent(this, RecipeDetailActivity.class);

            // Add the clicked recipe parcel to the intent
            clickedRecipeIntent.putExtra(RecipeActivity.RECIPE_ID, mRecipeId);

            // Start the activity with the passed values
            startActivity(clickedRecipeIntent);

            // Finish the current activity
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // Define the step Id change
    @Override
    public void onStepIdChange(int incrementedStepId) {

        // Capture the current fragment
        StepDetailFragment stepDetailFragment = (StepDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_step_detail_text_video);

        // If the fragment is there, update it with the new step Id and update the step Id
        mStepId = incrementedStepId;
        getSupportFragmentManager().beginTransaction()
                .detach(stepDetailFragment)
                .commit();
        instantiateStepDetailFragments(incrementedStepId);
    }

    // Release the video and text in on Pause
    @Override
    protected void onPause() {
        super.onPause();
        // Capture the current fragment
        StepDetailFragment stepDetailFragment = (StepDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_step_detail_text_video);

        getSupportFragmentManager().beginTransaction()
                .detach(stepDetailFragment)
                .commit();
    }

    // Recipe Activity saved instant state logic
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Pass the recipe Id between states
        outState.putInt(RECIPE_ID, mRecipeId);
        outState.putInt(STEP_ID, mStepId);
        outState.putInt(NUM_STEPS, mNumSteps);
    }
}
