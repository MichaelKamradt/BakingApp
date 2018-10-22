import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.gradle.builditbigger.MainActivity;
import com.udacity.gradle.builditbigger.R;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by micha on 10/21/2018.
 */

@RunWith(AndroidJUnit4.class)
public class JokeTellingUnitTest {

    // Create a rule for the activity
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    // Activity member variables
    public MainActivity mMainActivity;

    // Launch the activity before the test
    @Before
    public void setup() {
        mMainActivity = mActivityRule.getActivity();
    }

    // Run the test
    @Test
    public void ButtonClick_OpensActivityWithJoke() throws Exception {
        // Enable the test to click the button
        Espresso.onView(ViewMatchers.withId(R.id.joke_button)).perform(ViewActions.click());
        // Check that the retrieved joke is not an empty string
        Espresso.onView(ViewMatchers.withId(R.id.joke_text_view)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.withText(""))));
    }

}
