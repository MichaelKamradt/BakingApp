package com.example.android.bakingapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.CursorMatchers;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.android.bakingapp.activities.RecipeActivity;
import com.example.android.bakingapp.activities.RecipeDetailActivity;
import com.example.android.bakingapp.database.RecipeDBModel;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.regex.Matcher;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RecipeItemClickInstrumentedTest {

    // Hard-coded recipe title
    public static final String NUTELLA_PIE_TITLE_STRING = "Nutella Pie";

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
    public void RecipeClick_OpensRecipeDetailsWithTitle() throws Exception {
        // Enable the test to click the recyclerview
        Espresso.onView(ViewMatchers.withId(R.id.recipe_list_recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, ViewActions.click()));
        // Check that the detail activity has the correct name
        Espresso.onView(ViewMatchers.withId(R.id.recipe_title)).check(ViewAssertions.matches(ViewMatchers.withText(NUTELLA_PIE_TITLE_STRING)));
    }
}
