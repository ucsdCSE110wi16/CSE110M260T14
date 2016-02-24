package com.example.cse110mb260t14.ffs;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by liujie on 16/2/22.
 */
public class CategoriesTest extends AndroidTestCase {

    public static  final String TAG="AppTest";
    private ParseQuery<ParseObject> query;
    String description;
    String[] Split_description;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Parse.enableLocalDatastore(getContext());
        Parse.initialize(getContext());
    }

    public void testgoodslist()
    {
        //check the function of categories
        query = ParseQuery.getQuery("Listings");
        query.whereEqualTo("Status", 0);
        query.whereNotEqualTo("SellerID", ParseUser.getCurrentUser().getObjectId());
        query.whereContains("Title", "");
        query.whereContains("objectId", "");

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> found, ParseException e) {

                ArrayList<ParseObject> objects = new ArrayList<ParseObject>(found);
                for (ParseObject parseobject:objects)
                {
                    Log.d(TAG, "print: "+parseobject.getString("Title"));
                }
                 assertEquals(true,objects==null?false:true);

            }

        });
    }
    public void testsearch()
    {
        //check if the item is null

       String keyworld="mac";
        description = keyworld;
        int sub_postion = 0;
        for(int i = 0; i < description.length(); i++){
            if(description.charAt(i) == ' '){
                sub_postion++;
            }
            else{
                break;
            }
        }
        description = description.substring(sub_postion);
        System.out.println("What is :" + description);
        Split_description = description.split("\\s+");
        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        int word_limit = 0;
        if(Split_description.length <=5){
            word_limit = Split_description.length;
        }
        else{
            word_limit = 5;
        }
        for (int i = 0; i < word_limit; i++) {
            ParseQuery<ParseObject> title = ParseQuery.getQuery("Listings");
            ParseQuery<ParseObject> description = ParseQuery.getQuery("Listings");
            String lowerCase_description = new String(Split_description[i].toLowerCase());
            title.whereContains("objectId", "");
            title.whereContains("Title_lower", lowerCase_description);
            description.whereContains("objectId", "");
            description.whereContains("Description_lower", lowerCase_description);
            queries.add(title);
            queries.add(description);

        }
        Log.d(TAG, "done: ");
        query = ParseQuery.or(queries);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> found, ParseException e) {
                Log.d(TAG, "resut");
                ArrayList<ParseObject> objects = new ArrayList<ParseObject>(found);
                for (ParseObject parseobject:objects)
                {
                    Log.d(TAG, "done: "+parseobject.getString("Title"));
                }
                if (objects!=null)
                {
                    assertEquals(true,objects.get(0).getString("Title").indexOf("mac")==-1?false:true);

                }


            }
        });

    }
    public void testsell()
    {
        //check if the sell button can uploand the item
        saveItem();
    }
    private void saveItem(){
        ParseObject  item = new ParseObject("Listings");
        Bitmap photo_bitmap=null;
        item.put("Location", "");
        item.put("Price", "");
        item.put("Title", "");
        item.put("Description", "");
        item.put("Categories", Arrays.asList(""));
        item.put("SellerID", "");
        item.put("Title_lower", "");
        item.put("Description_lower", "");
        item.put("Status", 0);
        item.put("offer_buyer_id", Arrays.asList());
        item.put("offer_value", Arrays.asList());

        if (photo_bitmap != null) {
            // add bitmap byte array as file
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            //retrieve the byte array
            byte[] byteArray = stream.toByteArray();

            if (byteArray != null) {
                System.out.println(byteArray.toString());
                // name of file is objectId + ".jpg"
                ParseFile photo_file = new ParseFile("listing_photo.jpg", byteArray);
                item.put("photo_byte_array", photo_file);
                photo_file.saveInBackground();
            }
        }

        item.saveInBackground();

    }
    public void testaddwatch()
    {
        //check if the watchlist is ok
        ParseUser user = ParseUser.getCurrentUser();

        ArrayList<String> watchList = (ArrayList)user.get("WatchList");
        watchList.remove(user.getObjectId());
        Log.d(TAG, "testaddwatch: Add to WatchList");
        user.put("WatchList", watchList);
        user.saveInBackground();
    }
    public void testdelwath()
    {
        //check if can un-watch the item
        ParseUser user = ParseUser.getCurrentUser();
        ArrayList<String> watchList = (ArrayList)user.get("WatchList");
        watchList.add(user.getObjectId());
        Log.d(TAG, "testaddwatch: Remove from WatchList");
        user.saveInBackground();
    }
    public void testTransactionHistoryActivity()
    {
        //check the transaction history
        populateListings();
        populateOffers();

    }
    private void populateListings(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Listings");
        query.whereEqualTo("SellerID", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> found, ParseException e) {
                ArrayList<ParseObject> objects = new ArrayList<ParseObject>(found);
                for (ParseObject parseobject:objects)
                {
                    Log.d(TAG, "done: "+parseobject.getString("Title"));
                }
                if (objects!=null)
                {
                    assertEquals(true,objects==null?false:true);

                }

            }
        });


    }

    private void populateOffers(){
        final ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Listings");
        query.whereContainsAll("offer_buyer_id", Arrays.asList(user.getObjectId()));
         query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> found, ParseException e) {
                  ArrayList<ParseObject> objects = new ArrayList<ParseObject>(found);

                for (ParseObject parseobject:objects)
                {
                    Log.d(TAG, "done: "+parseobject.getString("Title"));
                }
                if (objects!=null)
                {
                    assertEquals(true,objects==null?false:true);

                }

            }
        });
    }
}
