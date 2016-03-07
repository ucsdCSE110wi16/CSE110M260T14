package com.example.cse110mb260t14.ffs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class DisplayOffersPage extends AppCompatActivity {

    private ParseObject listing;

    private String itemObjectId;
    private TextView itemTitle, itemPrice, itemDescription, NoneTV;
    private ImageView photo_display;
    private ListView offer_listview;

    private ArrayList<String> userid_offers, value_offers;
    private ArrayList<OfferObject> allOffers = new ArrayList<>();
    private boolean MyOffersOnly= false;


    private ParseFile photoByteArray;
    private Bitmap photoBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_offers_page);

        itemObjectId = getIntent().getExtras().getString("objectID");
        MyOffersOnly = getIntent().getBooleanExtra("MyOffersOnly", false);

        itemTitle = (TextView)findViewById(R.id.itemTitle);
        itemPrice = (TextView)findViewById(R.id.itemPrice);
        itemDescription = (TextView)findViewById(R.id.itemDescription);
        photo_display = (ImageView)findViewById(R.id.photo_display);
        offer_listview = (ListView) findViewById(R.id.show_offers_on_item);

        ParseQuery<ParseObject> query = new ParseQuery<>("Listings");
        query.whereEqualTo("objectId", itemObjectId);
        try {
            List<ParseObject> found = query.find();
            listing = found.get(0);


            itemTitle.setText(listing.getString("Title").toString());
            itemDescription.setText(listing.getString("Description").toString());
            itemPrice.setText(listing.getString("Price").toString());

            photoByteArray = (ParseFile) listing.get("photo_byte_array");
            if (photoByteArray != null) {
                try {
                    photoBitmap = BitmapFactory.decodeByteArray(photoByteArray.getData(), 0, photoByteArray.getData().length);
                    photo_display.setImageBitmap(photoBitmap);

                } catch (ParseException pe) {
                    Log.v(DisplayOffersPage.this.toString(), "error decoding byte array");
                }
            }

            userid_offers = (ArrayList)listing.get("offer_buyer_id");
            value_offers = (ArrayList) listing.get("offer_value");

            if(userid_offers.size()==0){
                NoneTV = (TextView)findViewById(R.id.offers_none);
                NoneTV.setVisibility(View.VISIBLE);
            }
            else {


                if (userid_offers.size() == value_offers.size()) {
                    for (int i = 0; i < userid_offers.size(); i++) {
                        if (userid_offers.get(i) != null && value_offers.get(i) != null) {
                            if (MyOffersOnly) {
                                if (!userid_offers.get(i).equals(ParseUser.getCurrentUser().getObjectId())) {
                                    continue;
                                }
                            }
                            OfferObject obj = new OfferObject(userid_offers.get(i), value_offers.get(i));
                            allOffers.add(obj);
                        }
                    }
                    OfferListViewAdapter myAdapter = new OfferListViewAdapter(DisplayOffersPage.this, allOffers);
                    offer_listview.setAdapter(myAdapter);


                    if(!MyOffersOnly){
                        offer_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                                // do later
                                Intent intent = new Intent(DisplayOffersPage.this, FullPageBuyer.class);
                                intent.putExtra("objectId", itemObjectId);
                                OfferObject obj = allOffers.get(position);
                                intent.putExtra("OfferValue", obj.retrieveValue());
                                intent.putExtra("userid", obj.retrieveUser().getObjectId());
                                intent.putExtra("offerIndex", position);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
        }
        catch (ParseException e){};









    }

}
