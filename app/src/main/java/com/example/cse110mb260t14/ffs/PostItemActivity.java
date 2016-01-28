package com.example.cse110mb260t14.ffs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.parse.ParseObject;

public class PostItemActivity extends AppCompatActivity {

    private Spinner locationSpinner;
    private Button postListingButton;
    private EditText postTitle;
    private EditText postPrice;
    private EditText postDescripttion;
    private EditText postCategories;

    private String itemTitle;
    private String itemPrice;
    private String itemDescription;
    private String itemCategories;
    private String itemLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);

        locationSpinner = (Spinner) findViewById(R.id.location_spinner);
        postListingButton = (Button) findViewById(R.id.post_listing_button);
        postTitle = (EditText)findViewById(R.id.item_title_edit_text);
        postPrice = (EditText)findViewById(R.id.item_price_edit_text);
        postDescripttion = (EditText)findViewById(R.id.item_description_edit_text);
        postCategories = (EditText)findViewById(R.id.item_categories_edit_text);




        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.locations_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        locationSpinner.setAdapter(adapter);


        if(getIntent().hasExtra("Title")){
            setDataFromConfirmIntent();
        }


        postListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean posted = getItemData();


                AlertDialog.Builder db = new AlertDialog.Builder(PostItemActivity.this);

                if (!posted){
                    db.setMessage("Please double check your data")
                            .setTitle("Failed Post");
                }

                db.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int arg1) {
                        d.cancel();
                    }
                });
                db.show();


                if (posted){
                    Intent confirmPageIntent = new Intent(PostItemActivity.this, ConfirmItemListing.class);

                    confirmPageIntent.putExtra("Location", itemLocation);
                    confirmPageIntent.putExtra("Price", itemPrice);
                    confirmPageIntent.putExtra("Title", itemTitle);
                    confirmPageIntent.putExtra("Description", itemDescription);
                    confirmPageIntent.putExtra("Categories", itemCategories);


                    startActivity(confirmPageIntent);
                }
            }
        });

    }

    private boolean getItemData(){
        itemTitle = postTitle.getText().toString();
        itemPrice = postPrice.getText().toString();
        itemDescription = postDescripttion.getText().toString();
        itemCategories = postCategories.getText().toString();
        itemLocation = locationSpinner.getSelectedItem().toString();

        if(locationSpinner.getSelectedItemPosition() == 0){
            System.out.println("Please select a location");
            return false;
        }
        if (itemTitle.equals("") || itemPrice.equals("") || itemDescription.equals("") || itemCategories.equals("")){
            System.out.println("PLEASE MAKE SURE TO FILL IN ALL THE INFORMATION!");
            return false;
        }

        return true;

    }


    private void setDataFromConfirmIntent(){
        itemTitle = getIntent().getExtras().getString("Title");
        itemPrice = getIntent().getExtras().getString("Price");
        itemDescription = getIntent().getExtras().getString("Description");
        itemCategories = getIntent().getExtras().getString("Categories");
        itemLocation = getIntent().getExtras().getString("Location");

        postTitle.setText(itemTitle);
        postPrice.setText(itemPrice);
        postDescripttion.setText(itemDescription);
        postCategories.setText(itemCategories);
        locationSpinner.setSelection(((ArrayAdapter<String>)locationSpinner.getAdapter()).getPosition(itemLocation));

    }


}
