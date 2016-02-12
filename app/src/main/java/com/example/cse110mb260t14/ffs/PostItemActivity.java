package com.example.cse110mb260t14.ffs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class PostItemActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Spinner locationSpinner;
    private Spinner categoriesSpinner1,categoriesSpinner2,categoriesSpinner3;
    private Button postListingButton;
    private Button camera_button;
    private ImageView photo_preview;
    private EditText postTitle;
    private EditText postPrice;
    private EditText postDescripttion;
    private EditText postCategories;
    private Bitmap photo_bitmap;

    private CheckBox addCat1, addCat2;

    private final int maxCategories = 3;
    private String itemTitle;
    private String itemPrice;
    private String itemDescription;
    private String[] itemCategories = new String[maxCategories];
    private String itemLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);




        locationSpinner = (Spinner) findViewById(R.id.location_spinner);
        categoriesSpinner1 = (Spinner)findViewById(R.id.item_categories_spinner1);
        categoriesSpinner2 = (Spinner)findViewById(R.id.item_categories_spinner2);
        categoriesSpinner3 = (Spinner)findViewById(R.id.item_categories_spinner3);
        postListingButton = (Button) findViewById(R.id.post_listing_button);
        postTitle = (EditText)findViewById(R.id.item_title_edit_text);
        postPrice = (EditText)findViewById(R.id.item_price_edit_text);
        postDescripttion = (EditText)findViewById(R.id.item_description_edit_text);
        addCat1 = (CheckBox)findViewById(R.id.item_categories_checkbox1);
        addCat2 = (CheckBox)findViewById(R.id.item_categories_checkbox2);
        camera_button = (Button) findViewById(R.id.camera_button);
        photo_preview = (ImageView) findViewById(R.id.photo_preview);



        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.locations_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        locationSpinner.setAdapter(adapter);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        categoriesSpinner1.setAdapter(adapter2);
        categoriesSpinner2.setAdapter(adapter2);
        categoriesSpinner3.setAdapter(adapter2);



        addCat1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    categoriesSpinner2.setVisibility(View.VISIBLE);
                    addCat2.setVisibility(View.VISIBLE);
                } else {
                    categoriesSpinner2.setVisibility(View.GONE);
                    addCat2.setVisibility(View.GONE);
                    categoriesSpinner3.setVisibility(View.GONE);
                }
            }
        });


        addCat2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    categoriesSpinner3.setVisibility(View.VISIBLE);
                } else {
                    categoriesSpinner3.setVisibility(View.GONE);
                }
            }
        });




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
                    confirmPageIntent.putExtra("Categories0", itemCategories[0]);
                    confirmPageIntent.putExtra("Categories1", itemCategories[1]);
                    confirmPageIntent.putExtra("Categories2", itemCategories[2]);
                    if (photo_preview.getDrawable() != null) {
                        confirmPageIntent.putExtra("Photo", ((BitmapDrawable) photo_preview.getDrawable()).getBitmap());
                    }




                    startActivity(confirmPageIntent);
                }
            }
        });

        // start camera intent (open camera app)
        camera_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(cameraIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
            }

        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photo_preview.setImageBitmap(imageBitmap);
        }
    }
    private boolean getItemData(){
        itemTitle = postTitle.getText().toString();
        itemPrice = postPrice.getText().toString();
        itemDescription = postDescripttion.getText().toString();
        itemLocation = locationSpinner.getSelectedItem().toString();

        itemCategories[0] = categoriesSpinner1.getSelectedItem().toString();
        if(addCat1.isChecked() && categoriesSpinner2.getSelectedItemPosition()!=0) {
            itemCategories[1] = categoriesSpinner2.getSelectedItem().toString();
            if (addCat2.isChecked() && categoriesSpinner3.getSelectedItemPosition()!=0) {
                itemCategories[2] = categoriesSpinner3.getSelectedItem().toString();
            }
        }



        if(locationSpinner.getSelectedItemPosition() == 0 || categoriesSpinner1.getSelectedItemPosition() == 0){
            System.out.println("Select Valid Data");
            return false;
        }
        if (itemTitle.equals("") || itemPrice.equals("") || itemDescription.equals("")){
            System.out.println("PLEASE MAKE SURE TO FILL IN ALL THE INFORMATION!");
            return false;
        }

        return true;

    }


    private void setDataFromConfirmIntent(){
        itemTitle = getIntent().getExtras().getString("Title");
        itemPrice = getIntent().getExtras().getString("Price");
        itemDescription = getIntent().getExtras().getString("Description");
        itemCategories[0] = getIntent().getExtras().getString("Categories0");
        itemCategories[1] = getIntent().getExtras().getString("Categories1");
        itemCategories[2] = getIntent().getExtras().getString("Categories2");
        itemLocation = getIntent().getExtras().getString("Location");
        photo_bitmap = (Bitmap) getIntent().getExtras().get("Photo");

        if (photo_bitmap != null) {
            photo_preview.setImageBitmap(photo_bitmap);
        }

        postTitle.setText(itemTitle);
        postPrice.setText(itemPrice);
        postDescripttion.setText(itemDescription);
        locationSpinner.setSelection(((ArrayAdapter<String>) locationSpinner.getAdapter()).getPosition(itemLocation));

        categoriesSpinner1.setSelection(((ArrayAdapter<String>)categoriesSpinner1.getAdapter()).getPosition(itemCategories[0]));
        categoriesSpinner2.setSelection(((ArrayAdapter<String>)categoriesSpinner2.getAdapter()).getPosition(itemCategories[1]));
        categoriesSpinner3.setSelection(((ArrayAdapter<String>)categoriesSpinner3.getAdapter()).getPosition(itemCategories[2]));
        if(categoriesSpinner2.getSelectedItemPosition()>0){
            addCat1.setChecked(true);
        }
        if(categoriesSpinner3.getSelectedItemPosition()>0){
            addCat2.setChecked(true);
        }


    }


}
