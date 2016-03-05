package com.example.cse110mb260t14.ffs;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.doubleClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;





/**
 *
 Scenarios：login->click buy->select one item->click MAKE OFFER-> enter price->click SUBMIT OFFER->check if it is successful
 * Created on 16/3/2.
 */
@RunWith(AndroidJUnit4.class)
public class BuyTabTest {

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule(DrawerMenuActivity.class);
    @Test
    public void testbuy() {
        //onView(withText("BUY")).perform(click());
        onView(withId(R.id.Search)).check(matches(isDisplayed()));
        //onView(withId(R.id.EditTextId)).perform(typeText("Doge"), closeSoftKeyboard());
        //onView(withId(R.id.Search)).perform(click());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //onView(withId(R.id.main_listings)).atPosition(0).perform(click());
        onData(anything()).inAdapterView(withId(R.id.main_listings)).atPosition(3).perform(click());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //onData(allOf(is(instanceOf(ParseObject.class)), is("Doge"))).perform(click());
        onView(withId(R.id.makeOfferButton)).perform(click());
        onView(withId(R.id.offerTextView)).perform(typeText("10"), closeSoftKeyboard());
        onView(withId(R.id.offer_submit_button)).check(matches(isDisplayed()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onView(withId(R.id.offer_submit_button)).perform(click());


    }


}
