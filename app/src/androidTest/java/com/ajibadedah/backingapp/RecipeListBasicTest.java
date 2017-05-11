package com.ajibadedah.backingapp;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

import android.support.test.annotation.UiThreadTest;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
//
//import com.tunjid.androidbootstrap.test.TestUtils;
//import com.tunjid.androidbootstrap.test.idlingresources.FragmentVisibleIdlingResource;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by ajibade on 5/10/17
 */

@RunWith(AndroidJUnit4.class)
public class RecipeListBasicTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private MainActivity mainActivity;
    private RecipeListFragment fragment;
    private IdlingResource mIdlingResource;

    @Before
    public void setup() {
        fragment = new RecipeListFragment();
        mainActivity = mActivityTestRule.getActivity();
        mIdlingResource = fragment.getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @After
    public void tearDown() {
        mainActivity.finish();
        mainActivity = null;
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
//        TestUtils.unregisterAllIdlingResources();
    }

    @Test
    public void ClickRecipeShowsSimpleExoPlayerViewFragment() {
        mainActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment, "RecipeListFragment").commit();

//        FragmentVisibleIdlingResource idlingResource = new FragmentVisibleIdlingResource(mActivityTestRule.getActivity(), "RecipeListFragment", true);
//        Espresso.registerIdlingResources(idlingResource);

        //Perform click on the first recipe item in the list
        Matcher<View> matcher = allOf(hasSibling(withId(R.id.ingredient_list_recycler)), withId(R.id.recipe_list_recycler));
        onView(matcher).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

//        FragmentVisibleIdlingResource idlingResourceTwo = new FragmentVisibleIdlingResource(mActivityTestRule.getActivity(), "RecipeStepListFragment", true);
//        Espresso.registerIdlingResources(idlingResourceTwo);

        //Perform click on the second item which is the first recipe step
        onView(matcher).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));

        //Check if the SimpleExoPlayerView is displayed
        Matcher<View> matcherTwo = allOf(hasSibling(withId(R.id.horizontalHalf)), withId(R.id.step_description));
        onView(matcherTwo).check(matches(isDisplayed()));
    }
}
