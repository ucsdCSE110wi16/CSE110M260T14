package com.example.cse110mb260t14.ffs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class ConfirmItemListing extends AppCompatActivity {

    private ParseObject item;
    private String itemTitle;
    private String itemPrice;
    private String itemDescription;
    private final int maxCategories = 3;
    private String[] itemCategories = new String[maxCategories];
    private String itemLocation;
    private String itemSellerID;
    private Bitmap photo_bitmap;

    private boolean successful = true;

    private Button confirmButton, editButton;
    private TextView titleTextView;
    private TextView priceTextView;
    private TextView descriptionTextView;
    private TextView categoriesTextView;
    private TextView locationTextView;
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
        locationTextView = (TextView)findViewById(R.id.item_location_confirm);
        photo_confirm = (ImageView)findViewById(R.id.photo_confirm);



        itemTitle = getIntent().getExtras().getString("Title");
        itemPrice = getIntent().getExtras().getString("Price");
        itemDescription = getIntent().getExtras().getString("Description");
        itemCategories[0] = getIntent().getExtras().getString("Categories0");
        itemCategories[1] = getIntent().getExtras().getString("Categories1");
        itemCategories[2] = getIntent().getExtras().getString("Categories2");
        itemLocation = getIntent().getExtras().getString("Location");
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
        System.out.println("About to set data\n Should see " + itemTitle);
        setTextViewData();


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String[] CategoriesArray = new String[1];

                item = new ParseObject("Listings");
                item.put("Location", itemLocation);
                item.put("Price", itemPrice);
                item.put("Title", itemTitle);
                item.put("Description", itemDescription);
                item.put("Categories", Arrays.asList(itemCategories));
                item.put("SellerID", itemSellerID);
                item.put("Status", 0);

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

                AlertDialog.Builder db = new AlertDialog.Builder(ConfirmItemListing.this);
                db.setMessage("You posted an item!")
                        .setTitle("Congrats!");



                /* Dont need this, never use .save(), always use .saveInBackground()
                try {
                }
                catch (ParseException e) {
                    successful = false;
                    db.setMessage("There was an error with the data")
                            .setTitle("Error");
                }
                */

                db.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int arg1) {
                        d.cancel();
                        Intent nextStep;
                        if (successful) {
                            nextStep = new Intent(ConfirmItemListing.this, DrawerMenuActivity.class);
                        } else {
                            nextStep = new Intent(ConfirmItemListing.this, PostItemActivity.class);
                        }
                        startActivity(nextStep);

                    }

                });
                db.show();


            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_back = new Intent(ConfirmItemListing.this, PostItemActivity.class);
                go_back.putExtra("Title",itemTitle);
                go_back.putExtra("Price", itemPrice);
                go_back.putExtra("Description", itemDescription);
                go_back.putExtra("Categories0", itemCategories[0]);
                go_back.putExtra("Categories1", itemCategories[1]);
                go_back.putExtra("Categories2", itemCategories[2]);
                go_back.putExtra("Location", itemLocation);
                if (photo_confirm.getDrawable() != null) {
                    go_back.putExtra("Photo", ((BitmapDrawable) photo_confirm.getDrawable()).getBitmap());
                }
                startActivity(go_back);
            }
        });

    }


    private void setTextViewData(){
        titleTextView.setText(itemTitle);
        priceTextView.setText(itemPrice);
        descriptionTextView.setText(itemDescription);
        categoriesTextView.setText(itemCategories[0] + ", " + itemCategories[1] + ", " + itemCategories[2]);
        locationTextView.setText(itemLocation);
    }

}
