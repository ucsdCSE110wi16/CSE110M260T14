package com.example.cse110mb260t14.ffs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


//import com.example.yoonchung.fragmenttest.R;

/**
 * Created by hp1 on 21-01-2015.
 */
public class SellTab extends Fragment {



    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Spinner categoriesSpinner1,categoriesSpinner2,categoriesSpinner3;
    private Button postListingButton;
    private Button camera_button;
    private ImageView photo_preview;
    private EditText postTitle;
    private EditText postPrice;
    private EditText postDescripttion;
    private EditText postCategories;
    private EditText locationText;

    private CheckBox addCat1, addCat2, locationCheckBox;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitude, longitude;
    private Geocoder geocoder;
    private List<Address> addresses;


    private final int maxCategories = 3;
    private String itemTitle;
    private String itemPrice;
    private String itemDescription;
    private String ZipCode;
    private String[] itemCategories = new String[maxCategories];




    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sell_layout,container,false);



        categoriesSpinner1 = (Spinner)v.findViewById(R.id.item_categories_spinner1);
        categoriesSpinner2 = (Spinner)v.findViewById(R.id.item_categories_spinner2);
        categoriesSpinner3 = (Spinner)v.findViewById(R.id.item_categories_spinner3);
        postListingButton = (Button) v.findViewById(R.id.post_listing_button);
        postTitle = (EditText)v.findViewById(R.id.item_title_edit_text);
        postPrice = (EditText)v.findViewById(R.id.item_price_edit_text);
        postDescripttion = (EditText)v.findViewById(R.id.item_description_edit_text);
        addCat1 = (CheckBox)v.findViewById(R.id.item_categories_checkbox1);
        addCat2 = (CheckBox)v.findViewById(R.id.item_categories_checkbox2);
        camera_button = (Button) v.findViewById(R.id.camera_button);
        photo_preview = (ImageView) v.findViewById(R.id.photo_preview);
        locationCheckBox = (CheckBox)v.findViewById(R.id.location_checkbox);
        locationText = (EditText) v.findViewById(R.id.location_editText);


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(),
                R.array.categories_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

        locationCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(locationCheckBox.isChecked()){
                    locationText.setVisibility(View.GONE);
                }
                else {
                    locationText.setVisibility(View.VISIBLE);
                }
            }
        });

        postListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean posted = getItemData();
                AlertDialog.Builder db = new AlertDialog.Builder(getActivity());

                if (!posted){
                    db.setMessage("Please double check your data")
                            .setTitle("Failed Post");

                    db.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface d, int arg1) {
                            d.cancel();
                        }
                    });
                    db.show();
                    }



                if (posted){
                    Intent confirmPageIntent = new Intent(getActivity(), ConfirmItemListing.class);
                    confirmPageIntent.putExtra("Price", itemPrice);
                    confirmPageIntent.putExtra("Title", itemTitle);
                    confirmPageIntent.putExtra("Description", itemDescription);
                    confirmPageIntent.putExtra("Categories0", itemCategories[0]);
                    confirmPageIntent.putExtra("Categories1", itemCategories[1]);
                    confirmPageIntent.putExtra("Categories2", itemCategories[2]);
                    confirmPageIntent.putExtra("ZipCode", ZipCode);
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
                if(cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
            }

        });

        return v;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            photo_preview.setImageBitmap(imageBitmap);
            photo_preview.setVisibility(View.VISIBLE);
        }
    }
    private boolean getItemData(){
        itemTitle = postTitle.getText().toString();
        itemPrice = postPrice.getText().toString();
        itemDescription = postDescripttion.getText().toString();
        if (!locationCheckBox.isChecked()) {
            ZipCode = locationText.getText().toString();
        }
        else {
            getZipCode(locationManager, locationListener);
        }
        itemCategories[0] = categoriesSpinner1.getSelectedItem().toString();
        if(addCat1.isChecked() && categoriesSpinner2.getSelectedItemPosition()!=0) {
            itemCategories[1] = categoriesSpinner2.getSelectedItem().toString();
            if (addCat2.isChecked() && categoriesSpinner3.getSelectedItemPosition()!=0) {
                itemCategories[2] = categoriesSpinner3.getSelectedItem().toString();
            }
        }

        if (itemTitle.equals("") || itemPrice.equals("") || itemDescription.equals("")){
            System.out.println("PLEASE MAKE SURE TO FILL IN ALL THE INFORMATION!");
            return false;
        }

        return true;

    }



    private void getZipCode(LocationManager locationManager, LocationListener locationListener) {
        // start the location manager for retrieving GPS coordinates
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                geocoder = new Geocoder(getActivity(), Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    ZipCode = addresses.get(0).getPostalCode();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };
    }
}
