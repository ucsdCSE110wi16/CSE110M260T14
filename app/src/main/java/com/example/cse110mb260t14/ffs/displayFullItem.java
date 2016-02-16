package com.example.cse110mb260t14.ffs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class displayFullItem extends AppCompatActivity {

    private String objectId;
    private TextView TitleTV, PriceTV, DescriptionTV, CategoriesTV, LocationTV;
    private Button watchListButton, makeOfferButton;
    private boolean WatchListAdd;
    private ImageView photo_display;
    private ParseFile photoByteArray;
    private Bitmap photoBitmap;
    ParseUser user = ParseUser.getCurrentUser();
    ArrayList<String> watchList = (ArrayList)user.get("WatchList");
    private boolean fullscreen = false;

    ParseObject listing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_full_item);

        objectId = getIntent().getExtras().getString("objectID");

        TitleTV = (TextView)findViewById(R.id.itemTitle);
        PriceTV = (TextView) findViewById(R.id.itemPrice);
        DescriptionTV = (TextView)findViewById(R.id.itemDescription);
        CategoriesTV = (TextView)findViewById(R.id.itemCategories);
        LocationTV = (TextView)findViewById(R.id.itemLocation);
        photo_display = (ImageView)findViewById(R.id.photo_display);
        watchListButton = (Button)findViewById(R.id.AddToWatchListButton);
        makeOfferButton = (Button)findViewById(R.id.makeOfferButton);

        if(watchList != null && watchList.contains(objectId)){
            watchListButton.setText("Remove from WatchList");
            WatchListAdd = false;
        }
        else{
            watchListButton.setText("Add to WatchList");
            WatchListAdd = true;
        }


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Listings");
        query.whereContains("objectId", objectId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> found, ParseException e) {
                listing = found.get(0);
                TitleTV.setText((String) listing.get("Title"));
                DescriptionTV.setText((String) listing.get("Description"));
                PriceTV.setText("$" + (String) listing.get("Price"));
                CategoriesTV.setText(Arrays.asList(listing.get("Categories")).get(0).toString());
                LocationTV.setText("Pickup Address at: " + ParseUser.getCurrentUser().getString("address")
                        + ", " + ParseUser.getCurrentUser().getString("city"));

                photoByteArray = (ParseFile) listing.get("photo_byte_array");
                if (photoByteArray != null) {
                    try {
                        photoBitmap = BitmapFactory.decodeByteArray(photoByteArray.getData(), 0, photoByteArray.getData().length);
                        photo_display.setImageBitmap(photoBitmap);

                    } catch (ParseException pe) {
                        Log.v(displayFullItem.this.toString(), "error decoding byte array");
                    }
                }
            }

        });

        /* TODO: added enlarge image capability
        photo_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fullscreen) {
                    TitleTV.setVisibility(View.GONE);
                    PriceTV.setVisibility(View.GONE);
                    DescriptionTV.setVisibility(View.GONE);
                    CategoriesTV.setVisibility(View.GONE);
                    LocationTV.setVisibility(View.GONE);
                    findViewById(R.id.AddToWatchListButton).setVisibility(View.GONE);
                    findViewById(R.id.BuyItemButton).setVisibility(View.GONE);
                    findViewById(R.id.full_listing_relative_layout).setVisibility(View.GONE);
                    photo_display.setMinimumHeight(findViewById(R.id.full_listing_relative_layout).getHeight());
                    photo_display.setMinimumWidth(findViewById(R.id.full_listing_relative_layout).getWidth());
                    photo_display.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    fullscreen = true;
                } else {
                    TitleTV.setVisibility(View.VISIBLE);
                    PriceTV.setVisibility(View.VISIBLE);
                    DescriptionTV.setVisibility(View.VISIBLE);
                    CategoriesTV.setVisibility(View.VISIBLE);
                    LocationTV.setVisibility(View.VISIBLE);
                    findViewById(R.id.AddToWatchListButton).setVisibility(View.VISIBLE);
                    findViewById(R.id.BuyItemButton).setVisibility(View.VISIBLE);
                    findViewById(R.id.full_listing_relative_layout).setVisibility(View.VISIBLE);
                    photo_display.setMaxWidth(250);
                    photo_display.setMaxHeight(250);
                    fullscreen = false;
                }
            }
        });
        */
        LocationTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseGeoPoint geoLoc = (ParseGeoPoint) ParseUser.getCurrentUser().get("location");
                double latitude = geoLoc.getLatitude();
                double longitude = geoLoc.getLongitude();
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });




        watchListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (WatchListAdd) {
                    watchList.add(objectId);

                    watchListButton.setText("Remove from WatchList");
                    WatchListAdd = false;
                }
                else {
                    watchList.remove(objectId);
                    watchListButton.setText("Add to WatchList");
                    WatchListAdd = true;
                }
                user.put("WatchList", watchList);
                user.saveInBackground();
            }
        });


        makeOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> offers_id = (ArrayList<String>) (listing.get("offer_buyer_id"));
                ArrayList<Integer> offer_value = (ArrayList<Integer>) (listing.get("offer_value"));
                ArrayList<String> user_offers_id = (ArrayList<String>) (user.get("offer_made_id"));
                ArrayList<Integer> user_offers_value = (ArrayList<Integer>) (user.get("offer_made_value"));


                if (listing!= null && offers_id != null && user_offers_id!= null && (offers_id.contains(user.getObjectId()) || user_offers_id.contains(listing.getObjectId()))) {

                } else {


                    offers_id.add(user.getObjectId());
                    offer_value.add(1);
                    listing.put("offer_buyer_id", offers_id);
                    listing.put("offer_value", offer_value);
                    listing.saveInBackground();
                    user_offers_id.add(listing.getObjectId());
                    user_offers_value.add(1);
                    user.put("offer_made_id", user_offers_id);
                    user.put("offer_made_value", user_offers_value);
                    user.saveInBackground();
                }
            }
        });




    }
}
