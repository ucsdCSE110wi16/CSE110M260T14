package com.example.cse110mb260t14.ffs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ConfirmItemListing extends AppCompatActivity {

    private ParseObject item;
    private String itemTitle;
    private String itemPrice;
    private String zipcode;
    private String itemDescription;
    private final int maxCategories = 3;
    private String[] itemCategories = new String[maxCategories];
    private String itemSellerID;
    private Bitmap photo_bitmap;

    private boolean successful = true;

    private Button confirmButton, editButton;
    private TextView titleTextView;
    private TextView priceTextView;
    private TextView descriptionTextView;
    private TextView categoriesTextView;
    private TextView ZipcodeTextView;
    private ImageView photo_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_item_listing);


        confirmButton = (Button) findViewById(R.id.confirm_button);
        editButton = (Button) findViewById(R.id.edit_button);
        titleTextView = (TextView)findViewById(R.id.item_title_confirm);
        priceTextView = (TextView)findViewById(R.id.item_price_confirm);
        descriptionTextView = (TextView)findViewById(R.id.item_description_confirm);
        categoriesTextView = (TextView)findViewById(R.id.item_categories_confirm);
        ZipcodeTextView = (TextView) findViewById(R.id.zipcode_confirm);
        photo_confirm = (ImageView)findViewById(R.id.photo_confirm);



        itemTitle = getIntent().getExtras().getString("Title");
        itemPrice = getIntent().getExtras().getString("Price");
        itemDescription = getIntent().getExtras().getString("Description");
        itemCategories[0] = getIntent().getExtras().getString("Categories0");
        itemCategories[1] = getIntent().getExtras().getString("Categories1");
        itemCategories[2] = getIntent().getExtras().getString("Categories2");
        zipcode = getIntent().getExtras().getString("Zipcode");

        //TODO fix zipcode issue
        if(zipcode==null||zipcode.equals("")){
            zipcode="";
        }
        photo_bitmap = (Bitmap) getIntent().getExtras().get("Photo");

        if(itemCategories[2] == null || itemCategories[2].equals(itemCategories[1])){
            itemCategories[2] = "null";
        }
        if(itemCategories[2] == null || itemCategories[2].equals(itemCategories[0])){
            itemCategories[2] = "null";
        }
        if(itemCategories[1] == null || itemCategories[1].equals(itemCategories[0])){
            if(itemCategories[2] != null && !itemCategories[2].equals("null")) {
                itemCategories[1] = itemCategories[2];
                itemCategories[2] = "null";
            }
            else {
                itemCategories[1] = "null";
            }
        }
        if (photo_bitmap != null) {
            photo_confirm.setImageBitmap(photo_bitmap);
        }

        itemSellerID = ParseUser.getCurrentUser().getObjectId();
        setTextViewData();


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveItem();
                sendAlertBox();

            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void setTextViewData(){
        titleTextView.setText(itemTitle);
        priceTextView.setText(itemPrice);
        descriptionTextView.setText(itemDescription);
        ZipcodeTextView.setText(zipcode);
        String finalCatString = itemCategories[0];
        if(!itemCategories[2].equals("null")){
            finalCatString += ", " + itemCategories[1] + " and " + itemCategories[2];
        }
        else {
            if(!itemCategories[1].equals("null")){
                finalCatString += " and " + itemCategories[1];
            }
        }
        categoriesTextView.setText(finalCatString);
        //categoriesTextView.setText(itemCategories[0] + ", " + itemCategories[1] + ", " + itemCategories[2]);
    }

    private void sendAlertBox(){
        AlertDialog.Builder db = new AlertDialog.Builder(ConfirmItemListing.this);
        db.setMessage("You posted an item!")
                .setTitle("Congrats!");
        db.setNeutralButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int arg1) {
                d.cancel();
                Intent nextStep;
                if (successful) {
                    nextStep = new Intent(ConfirmItemListing.this, DrawerMenuActivity.class);
                    startActivity(nextStep);
                } else {
                    finish();
                    Toast.makeText(ConfirmItemListing.this, "Unsuccessful post, please review your listing",Toast.LENGTH_SHORT);
                }
            }
        });
        db.show();
    }

    private void saveItem(){
        item = new ParseObject("Listings");

        putItems(item, itemPrice, itemTitle, itemDescription, itemCategories, itemSellerID);
        if (zipcode == null || zipcode == "") {
            item.put("geopoint", ParseUser.getCurrentUser().getParseGeoPoint("location"));
        }
        else {
            Geocoder geocoder = new Geocoder(ConfirmItemListing.this);
            try {
                Address address = geocoder.getFromLocationName(zipcode, 1).get(0);
                // make geopoint
                ParseGeoPoint geoPoint = makeGeopoint(address.getLatitude(), address.getLongitude());
                item.put("geopoint", geoPoint);
            }
            catch (IOException e) {
                Toast.makeText(ConfirmItemListing.this, "Error getting location. Please check your network.", Toast.LENGTH_SHORT).show();
                finish();
            }
            catch (IndexOutOfBoundsException e) {
                Toast.makeText(ConfirmItemListing.this, "Zipcode invalid, please try again.", Toast.LENGTH_SHORT).show();
                finish();

            }
        }

        if (photo_bitmap != null) {
            // add bitmap byte array as file
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            //retrieve the byte array
            byte[] byteArray = stream.toByteArray();

            if (byteArray != null) {
                // name of file is objectId + ".jpg"
                ParseFile photo_file = new ParseFile("listing_photo.jpg", byteArray);
                item.put("photo_byte_array", photo_file);
                photo_file.saveInBackground();
            }
        }

        item.saveInBackground();
    }

    public void putItems(ParseObject item, String itemPrice, String itemTitle, String itemDescription, String itemCategories[], String itemSellerID) {
        item.put("Price", itemPrice);
        item.put("Title", itemTitle);
        item.put("Description", itemDescription);
        item.put("Categories", Arrays.asList(itemCategories));
        item.put("SellerID", itemSellerID);
        item.put("Title_lower", itemTitle.toLowerCase());
        item.put("Description_lower", itemDescription.toLowerCase());
        item.put("Status", 0);
        item.put("offer_buyer_id", Arrays.asList());
        item.put("offer_value", Arrays.asList());
    }

    public ParseGeoPoint makeGeopoint(double latitude, double longitude) {
        return new ParseGeoPoint(latitude, longitude);
    }

}
