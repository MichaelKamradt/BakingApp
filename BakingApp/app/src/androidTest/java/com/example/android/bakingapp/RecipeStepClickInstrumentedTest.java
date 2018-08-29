package com.example.android.bakingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.activities.RecipeActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by micha on 8/28/2018.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeStepClickInstrumentedTest {

    // Hard-coded recipe title
    public static final String RECIPE_INTRODUCTION_TITLE_STRING = "Recipe Introduction";

    // Create a rule for the activity
    @Rule
    public ActivityTestRule<RecipeActivity> mActivityRule = new ActivityTestRule<>(RecipeActivity.class);

    // Activity member variables
    public RecipeActivity mRecipeActivity;

    // Launch the activity before the test
    @Before
    public void setup() {
        mRecipeActivity = mActivityRule.getActivity();
    }

    // Run the test
    @Test
    public void RecipeStepClick_OpensStepDetailWithTitle() throws Exception {
        // Start the main activity
        Espresso.onView(ViewMatchers.withId(R.id.recipe_list_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
        // Enable the test to click the recipe detail recyclerview
        Espresso.onView(ViewMatchers.withId(R.id.fragment_steps_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
        // Check that the detail activity has the correct name
        Espresso.onView(ViewMatchers.withId(R.id.step_title)).check(ViewAssertions.matches(ViewMatchers.withText(RECIPE_INTRODUCTION_TITLE_STRING)));
    }
}
