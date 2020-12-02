package com.example.needlevision;

import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

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
    // for Post Activity
    @Rule
    public IntentsTestRule<PostActivity> activityRule2 =
            new IntentsTestRule<>(PostActivity.class);

    //  Check Widget UI Display for login page
    @Test
    public void onLoginDisplay() {
        // sigin button
        onView(withId(R.id.sign_in_button)).check(matches(isDisplayed()));
        onView(withId(R.id.sign_in_button)).check(matches(isEnabled()));
        // guest button
        onView(withId(R.id.guest_btn)).check(matches(isDisplayed()));
        onView(withId(R.id.guest_btn)).check(matches(isClickable()));
        // logo Display
        onView(withId(R.id.login_logo)).check(matches(isDisplayed()));
        // app_name display
        onView(withId(R.id.app_name)).check(matches(isDisplayed()));
        onView(withId(R.id.app_name)).check(matches(withText("Needle Vision")));
    }

    // Check Guest View display
    @Test
    public void onGuestDisplay(){
        // go into guest view
        onView(withId(R.id.guest_btn)).perform(click());
        // check display
        onView(withId(R.id.map_frag)).check(matches(isDisplayed()));
        onView(withId(R.id.lvPosts)).check(matches(isDisplayed()));
        onView(withId(R.id.lvPosts)).check(matches(isClickable()));
    }

    // Check List click
    @Test
    public void onPostListClick(){
        // go into guest view
        onView(withId(R.id.guest_btn)).perform(click());
        // list view item click
        onData(anything()).inAdapterView(withId(R.id.lvPosts)).atPosition(0).perform(click());
        onData(anything()).inAdapterView(withId(R.id.lvPosts)).atPosition(2).perform(click());
        onData(anything()).inAdapterView(withId(R.id.lvPosts)).atPosition(4).perform(click());
    }

    // swipe between fragments
    @Test
    public void onSwipe(){
        // go into guest view
        onView(withId(R.id.guest_btn)).perform(click());
        // swipe
        onView(withId(R.id.lvPosts)).perform(swipeLeft());
        onView(withId(R.id.postPageList)).check(matches(isDisplayed()));
        onView(withId(R.id.postPageList)).perform(swipeRight());
    }

    @Test
    public void validatePostScenario() {
        // go into guest view
        onView(withId(R.id.guest_btn)).perform(click());
        // bring up camera
        onView(withId(R.id.fab)).check(matches(isDisplayed()));

        // Create a bitmap we can use for our simulated camera image
        Bitmap icon = BitmapFactory.decodeResource(
                InstrumentationRegistry.getTargetContext().getResources(),
                R.mipmap.ic_launcher);
        // Build a result to return from the Camera app
        Intent resultData = new Intent();
        resultData.putExtra("data", icon);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(PostActivity.RESULT_OK, resultData);

        // Stub out the Camera. When an intent is sent to the Camera, this tells Espresso to respond
        // with the ActivityResult we just created
        intending(toPackage("com.android.camera2")).respondWith(result);

        // Now that we have the stub in place, click on the button in our app that launches into the Camera
        onView(withId(R.id.fab)).perform(click());

        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
        // with the ActivityResult we just created
        intended(toPackage("com.android.camera2"));
        onView(withId(R.id.et_description)).perform(typeText("In front of gas station"), closeSoftKeyboard());
        // onView(withId(R.id.photo_send_btn)).perform(click());
    }
}
