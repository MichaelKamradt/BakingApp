package com.example.android.bakingapp.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.GridView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapters.IngredientAdapter;
import com.example.android.bakingapp.adapters.StepAdapter;
import com.example.android.bakingapp.database.IngredientsDBModel;
import com.example.android.bakingapp.database.IngredientsViewModel;
import com.example.android.bakingapp.database.RecipeStepsDatabase;
import com.example.android.bakingapp.database.StepsViewModel;
import com.example.android.bakingapp.database.StepsDBModel;
import com.example.android.bakingapp.database.StepsViewModelFactory;
import com.example.android.bakingapp.fragments.ButtonFragment;
import com.example.android.bakingapp.fragments.RecipeDetailFragment;
import com.example.android.bakingapp.fragments.StepDetailFragment;

public class RecipeDetailActivity extends AppCompatActivity implements
    RecipeDetailFragment.OnMasterDetailStepItemClick {

    // TAG for logging
    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    private static final String STEP_DETAIL_FRAGMENT_TAG = StepDetailFragment.class.getSimpleName();
    private static final String BUTTON_FRAGMENT_TAG = ButtonFragment.class.getSimpleName();

    // Recipe member variables
    private int mRecipeId;
    private int mStepId;

    // Key for recipe Id
    public static final String STEP_ID = "step_id";
    public static final String RECIPE_ID = "recipe_id";
    public static final String MASTER_DETAIL_ID = "master_detail_id";

    // Master Detail View
    private boolean mMasterDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // savedInstanceState Logic
        if (savedInstanceState != null) {
            mRecipeId = savedInstanceState.getInt(RECIPE_ID);
            mStepId = savedInstanceState.getInt(STEP_ID);
            mMasterDetail = savedInstanceState.getBoolean(MASTER_DETAIL_ID);
        } else {
            // Get the intent data
            Intent passedIntent = getIntent();

            // Ensure it has the right extra
            if (passedIntent.hasExtra(RecipeActivity.RECIPE_ID)) {
                // Get the recipe item and title
                mRecipeId = passedIntent.getIntExtra(RecipeActivity.RECIPE_ID, 0);
                // Set the step equal to the first upon instantiation
                mStepId = 0;
            } else {
                Log.d(TAG, "The recipe was not loaded properly");
            }
            // Depending on the layout, we either want to inflate just the recipe detail or step and recipe detail
            if (findViewById(R.id.recipe_master_detail_linear_layout) != null) {
                // Set the layout bool
                mMasterDetail = true;
                // Inflate the Recipe Detail. Step Detail inflate upon click
                instantiateStepDetailFragments(mRecipeId, mStepId);
                inflateRecipeDetailFragment();
            } else {
                // Set the layout bool
                mMasterDetail = false;
                inflateRecipeDetailFragment();
            }
        }
    }

    // Inflate the Recipe Detail Fragment
    private void inflateRecipeDetailFragment() {

        // Load the long instructions in the fragment
        RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(mRecipeId);

        // Get a fragment manager to handle the fragment
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Begin the transaction and commit the fragment
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_view_recipe_detail, recipeDetailFragment)
                .commit();
    }

    // Instantiate Step detail Fragments
    public void instantiateStepDetailFragments(int recipeId, int stepId) {
        // Get a fragment manager to handle the fragment
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Instantiate a new version of the Step Detail Fragment
        StepDetailFragment stepDetailFragment = StepDetailFragment.newInstance(mRecipeId, stepId);

        // Begin the transaction and commit the fragment
        fragmentManager.beginTransaction()
                .add(R.id.fragment_step_detail_text_video, stepDetailFragment, STEP_DETAIL_FRAGMENT_TAG)
                .commit();
    }

    // Implement click logic only when it is in Master-Detail
    @Override
    public void onMasterDetailStepItemClick(int stepId) {
        if (mMasterDetail) {

            // Define the new stepId
            mStepId = stepId;

            // Capture the current fragment
            StepDetailFragment stepDetailFragment = (StepDetailFragment) getSupportFragmentManager()
                    .findFragmentByTag(STEP_DETAIL_FRAGMENT_TAG);

            // If the fragment is there, update it with the new step Id and update the step Id
            getSupportFragmentManager().beginTransaction()
                    .remove(stepDetailFragment)
                    .commit();

            // Create a new step detail fragment
            instantiateStepDetailFragments(mRecipeId, stepId);
        }
    }

    // Bring back the video after a lock screen
    @Override
    protected void onResume() {
        super.onResume();
        if (mMasterDetail) {
            // Capture the current fragment
            StepDetailFragment stepDetailFragment = (StepDetailFragment) getSupportFragmentManager()
                    .findFragmentByTag(STEP_DETAIL_FRAGMENT_TAG);

            getSupportFragmentManager().beginTransaction()
                    .attach(stepDetailFragment)
                    .commit();
        }
    }

    // Detach the video during rotation or screenlock
    @Override
    protected void onPause() {
        super.onPause();
        if (mMasterDetail) {
            // Capture the current fragment
            StepDetailFragment stepDetailFragment = (StepDetailFragment) getSupportFragmentManager()
                    .findFragmentByTag(STEP_DETAIL_FRAGMENT_TAG);

            getSupportFragmentManager().beginTransaction()
                    .detach(stepDetailFragment)
                    .commit();
        }
    }

    // Recipe Activity saved instant state logic
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Pass the recipe Id / title / step / masterdetail between states
        outState.putInt(RECIPE_ID, mRecipeId);
        outState.putInt(STEP_ID, mStepId);
        outState.putBoolean(MASTER_DETAIL_ID, mMasterDetail);
    }
}