package com.example.cse110mb260t14.ffs;

import android.app.Activity;
import android.location.Address;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import junit.framework.TestCase;

import java.util.Locale;

/**
 * Created by George on 3/5/2016.
 */
public class ConfirmItemListingTest extends TestCase {
    ConfirmItemListing test;
    public void setUp() {
       test = new ConfirmItemListing();
    }

    public void testPutItems() {
        ParseObject item = new ParseObject("Listings");
        test.putItems(item, "55", "TeSt_TiTlE", "TeSt_DeScRiPtIoN", new String[]{"cat1", "cat2"}, "testID123");

        assertEquals(item.get("Price"), "55");
        assertEquals(item.get("Title"), "TeSt_TiTlE");
        assertEquals(item.get("Description"), "TeSt_DeScRiPtIoN");
        assertEquals(item.get("SellerID"), "testID123");
        assertEquals(item.get("Title_lower"), "test_title");
        assertEquals(item.get("Description_lower"), "test_description");
        assertEquals(item.get("Status"), 0);
    }

    public void testMakeGeopoint() {
        ParseGeoPoint testGeoPoint = test.makeGeopoint(37.4530553, -122.1178261);
        assertEquals(testGeoPoint.getLatitude(), 37.4530553);
        assertEquals(testGeoPoint.getLongitude(), -122.1178261);
    }
}
