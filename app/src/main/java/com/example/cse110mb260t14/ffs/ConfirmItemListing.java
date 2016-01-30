package com.example.cse110mb260t14.ffs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ConfirmItemListing extends AppCompatActivity {

    private ParseObject item;
    private String itemTitle;
    private String itemPrice;
    private String itemDescription;
    private String itemCategories;
    private String itemLocation;

    private boolean successful = true;

    private Button confirmButton, editButton;


    private TextView titleTextView;
    private TextView priceTextView;
    private TextView descriptionTextView;
    private TextView categoriesTextView;
    private TextView locationTextView;


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



        itemTitle = getIntent().getExtras().getString("Title");
        itemPrice = getIntent().getExtras().getString("Price");
        itemDescription = getIntent().getExtras().getString("Description");
        itemCategories = getIntent().getExtras().getString("Categories");
        itemLocation = getIntent().getExtras().getString("Location");

        System.out.println("About to set data\n Should see " + itemTitle);
        setTextViewData();


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                item = new ParseObject("Listings");
                item.put("Location", itemLocation);
                item.put("Price", itemPrice);
                item.put("Title", itemTitle);
                item.put("Description", itemDescription);
                item.put("Categories", itemCategories);
                item.put("SellerID", ParseUser.getCurrentUser().getObjectId());

                AlertDialog.Builder db = new AlertDialog.Builder(ConfirmItemListing.this);
                db.setMessage("You posted an item!")
                        .setTitle("Congrats!");

                try {
                    item.save();

                } catch (ParseException e) {
                    successful = false;
                    db.setMessage("There was an error with the data")
                            .setTitle("Error");
                }

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
                Intent intent = new Intent(ConfirmItemListing.this, PostItemActivity.class);
                intent.putExtra("Title", itemTitle);
                intent.putExtra("Price", itemPrice);
                intent.putExtra("Description", itemDescription);
                intent.putExtra("Location", itemLocation);
                intent.putExtra("Categories", itemCategories);
                startActivity(intent);

            }
        });

    }


    private void setTextViewData(){
        titleTextView.setText(itemTitle);
        priceTextView.setText(itemPrice);
        descriptionTextView.setText(itemDescription);
        categoriesTextView.setText(itemCategories);
        locationTextView.setText(itemLocation);
    }

}