package com.example.cse110mb260t14.ffs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class displayFullItem extends AppCompatActivity {

    private String objectId;
    private TextView TitleTV, PriceTV, DescriptionTV, CategoriesTV, LocationTV;
    private ImageView photo_display;
    private ParseFile photoByteArray;
    private Bitmap photoBitmap;
    private boolean fullscreen = false;

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


        System.out.println("OBJECT ID IS: " + objectId);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Listings");
        query.whereContains("objectId", objectId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> found, ParseException e) {

                if (found.size() > 1) {
                    System.out.print("FOUND MORE THAN ONE ITEM WITH THAT OBJECTID!!!");
                }

                TitleTV.setText((String) found.get(0).get("Title"));
                DescriptionTV.setText((String) found.get(0).get("Description"));
                PriceTV.setText("$" + (String) found.get(0).get("Price"));
                CategoriesTV.setText(Arrays.asList(found.get(0).get("Categories")).get(0).toString());
                LocationTV.setText("Pickup Address at: " + ParseUser.getCurrentUser().getString("address")
                        + ", " + ParseUser.getCurrentUser().getString("city"));

                photoByteArray = (ParseFile) found.get(0).get("photo_byte_array");
                try {
                    photoBitmap = BitmapFactory.decodeByteArray(photoByteArray.getData(), 0, photoByteArray.getData().length);
                    photo_display.setImageBitmap(photoBitmap);

                } catch (ParseException pe) {
                    Log.v(displayFullItem.this.toString(), "error decoding byte array");
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









    }
}
