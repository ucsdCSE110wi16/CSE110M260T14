package com.example.cse110mb260t14.ffs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class displayFullItem extends AppCompatActivity {

    private String objectId;
    private TextView TitleTV, PriceTV, DescriptionTV, CategoriesTV, LocationTV, OfferTV, DeleteItemTV, MarkAsSoldTV;
    private Button makeOfferButton, OfferSubmitButton;
    private Switch watchListSwitch;
    private ImageView photo_display, image_stamp;
    private ParseFile photoByteArray;
    private Bitmap photoBitmap;
    ParseUser user;
    ArrayList<String> watchList;
    private boolean fullscreen = false, showOffers = false;



    ParseObject listing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (ParseUser.class == null) {
            Parse.initialize(this);
        }
        user = ParseUser.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_full_item);
        try
        {
            watchList = (ArrayList<String>) user.get("WatchList");
        }catch(java.lang.ClassCastException e) {
            Log.v("displayFullItem: ","Problem with ArrayList cast");
        }
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
            TitleTV.setText( listing.getString("Title"));
            DescriptionTV.setText("Description: " + listing.getString("Description"));
            PriceTV.setText("$" + listing.getString("Price"));
            String categoriesString = listing.get("Categories").toString();
            String finalCatString = makefinalCatString(categoriesString);
            CategoriesTV.setText("Categories: " + finalCatString);
            LocationTV.setText("Sold in area: " + listing.getString("ZipCode"));

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
        MarkAsSoldTV = (TextView) findViewById(R.id.mark_sold_text_view);
        image_stamp = (ImageView)findViewById(R.id.image_stamp);
        if(listing != null && listing.getString("SellerID").equals(ParseUser.getCurrentUser().getObjectId())){

            if((int)listing.get("Status")==1){
                MarkAsSoldTV.setVisibility(View.GONE);
                image_stamp.setImageResource(R.drawable.sold_stamp);
                image_stamp.setVisibility(View.VISIBLE);
                MarkAsSoldTV.setVisibility(View.GONE);
            }
            else {
                MarkAsSoldTV.setVisibility(View.VISIBLE);
                MarkAsSoldTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(displayFullItem.this);
                        builder.setTitle("Are you sure you want to mark this item as sold?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listing.put("Status", 1);
                                listing.saveInBackground();
                                image_stamp.setImageResource(R.drawable.sold_stamp);
                                image_stamp.setVisibility(View.VISIBLE);
                                MarkAsSoldTV.setVisibility(View.GONE);
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
            if((int)listing.get("Status") ==2) {
                DeleteItemTV.setText("Item Deleted");
                image_stamp.setImageResource(R.drawable.deleted_stamp);
                image_stamp.setVisibility(View.VISIBLE);
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
                                image_stamp.setImageResource(R.drawable.deleted_stamp);
                                image_stamp.setVisibility(View.VISIBLE);
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



        if(watchList.contains((String) objectId)){
            watchListSwitch.setChecked(true);
        }
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


        if(listing.getString("SellerID").equals(user.getObjectId())){
            makeOfferButton.setVisibility(View.GONE);
        }
        makeOfferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OfferTV = (TextView) findViewById(R.id.offerTextView);
                if(!showOffers) {
                    OfferTV.setVisibility(View.VISIBLE);
                    OfferSubmitButton.setVisibility(View.VISIBLE);
                    showOffers = true;
                }
                else {
                    OfferTV.setVisibility(View.GONE);
                    OfferSubmitButton.setVisibility(View.GONE);
                    showOffers = false;
                }

            }
        });


        OfferSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

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
                    OfferTV.setText("");
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


        if((int)listing.get("Status")==1){
            image_stamp.setVisibility(View.VISIBLE);
        }
    }
    public String makefinalCatString(String categoriesString) {
            String[] allCats = categoriesString.replace('[', ' ').replace(']',' ').replaceAll(" ", "").split(",");
            String finalCatString = allCats[0];
            if(!allCats[2].equals("null")){
                finalCatString += ", " + allCats[1] + " and " +allCats[2];
            }
            else if (!allCats[1].equals("null")){
                finalCatString += " and " + allCats[1];
            }
            return finalCatString;
    }

}
