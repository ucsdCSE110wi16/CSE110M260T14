package com.example.cse110mb260t14.ffs;

import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;

/**
 * Scenario：login->click sell->type item's information->click post->click confirm
 * Created on 16/3/2.
 */
@RunWith(AndroidJUnit4.class)
public class SellTabTest {
    
    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule(DrawerMenuActivity.class);


    //test post
    @Test
    public void testconfirm() {

        onView(withText("SELL")).perform(click());
        onView(withId(R.id.item_title_edit_text)).perform(typeText("mactest"),closeSoftKeyboard());
        onView(withId(R.id.item_description_edit_text)).perform(typeText("macbooktesttest"),closeSoftKeyboard());
        onView(withId(R.id.item_price_edit_text)).perform(typeText("1000"),closeSoftKeyboard());
        onView(withId(R.id.item_categories_checkbox1)).perform(click());
        //vehicles
        onView(withId(R.id.item_categories_spinner1)).perform(click());
        onData(allOf(is(instanceOf(String.class)),is("Vehicles"))).perform(click());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //onView(withText("vehicles")).perform(click());
      //  onData(withText("Effective Java ")).inAdapterView(allOf(withId(R.id.item_categories_checkbox2),isDisplayed())).perform(click());
        onView(withId(R.id.post_listing_button)).perform(click());
        closeSoftKeyboard();
        onView(withId(R.id.confirm_button)).perform(click());


    }
}
