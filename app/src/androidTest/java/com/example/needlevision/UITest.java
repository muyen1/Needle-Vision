package com.example.needlevision;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Automated UI Testing for Needle-Vision
 *
 * 2020 Fall Term - Network Security
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class UITest {

    // For MainActivity
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    //  Check Widget UI Display for login page
    @Test
    public void onDisplay_Login() {
        onView(withId(R.id.sign_in_button)).check(matches(isDisplayed()));
        onView(withId(R.id.guest_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.sign_in_button)).check(matches(isEnabled()));
        onView(withId(R.id.guest_btn)).check(matches(isClickable()));
    }

    // Check if Login buttons are clickable
    @Test
    public void onDispl(){
        onView(withId(R.id.guest_btn)).perform(click());
        onView(withId(R.id.map_frag)).check(matches(isDisplayed()));
    }


}
