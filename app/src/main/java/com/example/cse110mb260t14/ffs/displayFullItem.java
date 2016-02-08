package com.example.cse110mb260t14.ffs;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
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


            }

        });

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
