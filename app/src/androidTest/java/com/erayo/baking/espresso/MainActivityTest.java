package com.erayo.baking.espresso;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.erayo.baking.MainActivity;
import com.erayo.baking.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private static final String NUTELLA_PIE = "Nutella Pie";
    private static final String STARTING_PREP = "Starting prep";

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);


    @Test
    public void shouldShowCorrectRVWhenAppLaunch() {
        onView(withId(R.id.main_recycler_view))
                .check(matches(hasDescendant(withText(NUTELLA_PIE))));
    }

    @Test
    public void shouldShowCorrectRVAndContentWhenAnItemIsClicked() {
        onView(withId(R.id.main_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(NUTELLA_PIE)), click()));
        onView(withId(R.id.recipe_steps_rv))
                .check(matches(hasDescendant(withText(STARTING_PREP))));
    }

    @Test
    public void shouldDirectToStepDetailWhenAStepClicked() {
        onView(withId(R.id.main_recycler_view))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(NUTELLA_PIE)), click()));
        onView(withId(R.id.recipe_steps_rv))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(STARTING_PREP)), click()));
        onView(withId(R.id.step_long_description_tv)).check(matches(isDisplayed()));
    }
}