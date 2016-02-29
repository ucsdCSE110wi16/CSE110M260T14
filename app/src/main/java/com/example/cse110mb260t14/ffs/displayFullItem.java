package com.example.cse110mb260t14.ffs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

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
    private TextView TitleTV, PriceTV, DescriptionTV, CategoriesTV, LocationTV, OfferTV, DeleteItemTV;
    private Button makeOfferButton, OfferSubmitButton;
    private Switch watchListSwitch;
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
        makeOfferButton = (Button)findViewById(R.id.makeOfferButton);
        OfferSubmitButton = (Button)findViewById(R.id.offer_submit_button);
        watchListSwitch = (Switch)findViewById(R.id.watchListSwitch);


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Listings");
        query.whereContains("objectId", objectId);

        try {
            List<ParseObject> found = query.find();
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
        }catch (ParseException e){};

        /*
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

        });*/


        DeleteItemTV = (TextView) findViewById(R.id.delete_item_text_view);
        if(listing != null && listing.getString("SellerID").equals(ParseUser.getCurrentUser().getObjectId())){
            if((int)listing.get("Status") ==2) {
                DeleteItemTV.setText("Item Deleted");
            }
            else {
                DeleteItemTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(displayFullItem.this);
                        builder.setTitle("Are you sure you want to delete this item?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listing.put("Status", 2);
                                listing.saveInBackground();
                                DeleteItemTV.setText("Item Deleted");
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();

                    }
                });
            }
            DeleteItemTV.setVisibility(View.VISIBLE);
        }



        /*
         //TODO: added enlarge image capability
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
                    findViewById(R.id.makeOfferButton).setVisibility(View.GONE);
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
                    findViewById(R.id.makeOfferButton).setVisibility(View.VISIBLE);
                    findViewById(R.id.full_listing_relative_layout).setVisibility(View.VISIBLE);
                    photo_display.setMaxWidth(250);
                    photo_display.setMaxHeight(250);
                    fullscreen = false;
                }
            }
        }); */

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




        watchListSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (watchListSwitch.isChecked()) {
                    watchList.add(objectId);
                } else {
                    watchList.remove(objectId);
                }
                user.put("WatchList", watchList);
                user.saveInBackground();
            }
        });


        makeOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OfferTV = (TextView) findViewById(R.id.offerTextView);
                OfferTV.setVisibility(View.VISIBLE);
                OfferSubmitButton.setVisibility(View.VISIBLE);

            }
        });


        OfferSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ArrayList<String> offers_id = (ArrayList<String>) (listing.get("offer_buyer_id"));
                ArrayList<String> offer_value = (ArrayList<String>) (listing.get("offer_value"));
                ArrayList<String> user_offers_id = (ArrayList<String>) (user.get("offer_made_id"));
                ArrayList<String> user_offers_value = (ArrayList<String>) (user.get("offer_made_value"));


                if (listing == null) {
                } else {

                    if (offers_id == null) {
                        offers_id = new ArrayList<String>();
                    }
                    if (user_offers_id == null) {
                        user_offers_id = new ArrayList<String>();
                    }
                    if (offer_value == null) {
                        offer_value = new ArrayList<String>();
                    }
                    if (user_offers_value == null) {
                        user_offers_value = new ArrayList<String>();
                    }
                    String offer = OfferTV.getText().toString();
                    offers_id.add(user.getObjectId());
                    offer_value.add(offer);
                    listing.put("offer_buyer_id", offers_id);
                    listing.put("offer_value", offer_value);
                    listing.saveInBackground();
                    user_offers_id.add(listing.getObjectId());
                    user_offers_value.add(offer);
                    user.put("offer_made_id", user_offers_id);
                    user.put("offer_made_value", user_offers_value);
                    user.saveInBackground();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(displayFullItem.this);
                    builder.setTitle("You offered $" + offer + " for this item!");
                    builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
                OfferTV.setVisibility(View.GONE);
                OfferSubmitButton.setVisibility(View.GONE);
            }
        });




    }
}
