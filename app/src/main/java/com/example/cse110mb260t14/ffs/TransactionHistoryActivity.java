package com.example.cse110mb260t14.ffs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransactionHistoryActivity extends AppCompatActivity {

    private ListView offersListView, listingsListView, SoldListingsList;
    private TextView myOffersTV, myItemsTV, myItemsSoldTV, soldNone, itemsNone, OffersNone;
    private boolean showOffers=false, showItems=false, showSoldItems=false, noOffers=false, noItems=false, noSold=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);

        offersListView = (ListView) findViewById(R.id.my_offers_list);
        listingsListView =  (ListView) findViewById(R.id.my_listings_list);
        SoldListingsList =  (ListView) findViewById(R.id.my_sold_items_list);
        myOffersTV = (TextView) findViewById(R.id.my_offers);
        myItemsTV = (TextView)findViewById(R.id.my_listings);
        myItemsSoldTV = (TextView)findViewById(R.id.my_items_sold);
        itemsNone = (TextView)findViewById(R.id.my_items_none);
        OffersNone = (TextView)findViewById(R.id.my_offers_none);
        soldNone = (TextView)findViewById(R.id.my_items_sold_none);



        populateMyListings();
        populateOffers();
        populateMySoldListings();

        myOffersTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showItems = false;
                showSoldItems=false;
                SoldListingsList.setVisibility(View.GONE);
                listingsListView.setVisibility(View.GONE);
                soldNone.setVisibility(View.GONE);
                itemsNone.setVisibility(View.GONE);
                myItemsTV.setText("+ My Items (Available)");
                myItemsSoldTV.setText("+ My Sold Items");
                if (!showOffers) {
                    if(noOffers){
                        OffersNone.setVisibility(View.VISIBLE);
                    }
                    else {
                        offersListView.setVisibility(View.VISIBLE);
                    }
                    showOffers = true;
                    myOffersTV.setText("- My Offers");
                } else {
                    offersListView.setVisibility(View.GONE);
                    OffersNone.setVisibility(View.GONE);
                    showOffers = false;
                    myOffersTV.setText("+ My Offers");
                }

            }
        });

        myItemsTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSoldItems=false;
                SoldListingsList.setVisibility(View.GONE);
                showOffers=false;
                offersListView.setVisibility(View.GONE);
                OffersNone.setVisibility(View.GONE);
                soldNone.setVisibility(View.GONE);
                myOffersTV.setText("+ My Offers");
                myItemsSoldTV.setText("+ My Sold Items");
                if(!showItems){
                    if(noItems){
                        itemsNone.setVisibility(View.VISIBLE);
                    }
                    else {
                        listingsListView.setVisibility(View.VISIBLE);
                    }
                    showItems=true;
                    myItemsTV.setText("- My Items (Available)");
                }
                else {
                    listingsListView.setVisibility(View.GONE);
                    itemsNone.setVisibility(View.GONE);
                    showItems=false;
                    myItemsTV.setText("+ My Items (Available)");

                }

            }
        });

        myItemsSoldTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOffers=false;
                showItems=false;
                listingsListView.setVisibility(View.GONE);
                offersListView.setVisibility(View.GONE);
                OffersNone.setVisibility(View.GONE);
                itemsNone.setVisibility(View.GONE);
                myOffersTV.setText("+ My Offers");
                myItemsTV.setText("+ My Items (Available)");
                if(!showSoldItems){
                    if(noSold){
                        soldNone.setVisibility(View.VISIBLE);
                    }
                    else {
                        SoldListingsList.setVisibility(View.VISIBLE);
                    }
                    showSoldItems=true;
                    myItemsSoldTV.setText("- My Sold Items");

                }
                else {
                    SoldListingsList.setVisibility(View.GONE);
                    soldNone.setVisibility(View.GONE);
                    showSoldItems=false;
                    myItemsSoldTV.setText("+ My Sold Items");
                }
            }
        });
    }

    private void populateMyListings(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Listings");
        query.whereEqualTo("SellerID", ParseUser.getCurrentUser().getObjectId());
        query.whereEqualTo("Status", 0);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> found, ParseException e) {
                OfferListingAdapter adapter = new OfferListingAdapter(TransactionHistoryActivity.this, new ArrayList<ParseObject>());
                ArrayList<ParseObject> objects = new ArrayList<ParseObject>(found);
                if (objects != null) {
                    adapter = new OfferListingAdapter(TransactionHistoryActivity.this, objects);
                }
                if(objects.size()==0){
                    noItems=true;
                }

                listingsListView.setAdapter(adapter);

                listingsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
                        Intent intent = new Intent(TransactionHistoryActivity.this, displayFullItem.class);
                        intent.putExtra("objectID", ((ParseObject) adapter.getItemAtPosition(position)).getObjectId());
                        startActivity(intent);
                        return true;
                    }
                });

                listingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                        Intent intent = new Intent(TransactionHistoryActivity.this, DisplayOffersPage.class);
                        intent.putExtra("objectID", ((ParseObject) adapter.getItemAtPosition(position)).getObjectId());
                        startActivity(intent);
                    }
                });
                }

        });


    }


    private void populateMySoldListings(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Listings");
        query.whereEqualTo("SellerID", ParseUser.getCurrentUser().getObjectId());
        query.whereEqualTo("Status", 1);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> found, ParseException e) {
                OfferListingAdapter adapter = new OfferListingAdapter(TransactionHistoryActivity.this, new ArrayList<ParseObject>());
                ArrayList<ParseObject> objects = new ArrayList<ParseObject>(found);
                if (objects != null) {
                    adapter = new OfferListingAdapter(TransactionHistoryActivity.this, objects);
                }
                if(objects.size()==0){
                    noSold = true;
                }
                else {
                    SoldListingsList.setAdapter(adapter);

                    SoldListingsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {

                            Intent intent = new Intent(TransactionHistoryActivity.this, displayFullItem.class);
                            intent.putExtra("objectID", ((ParseObject) adapter.getItemAtPosition(position)).getObjectId());
                            startActivity(intent);
                            return true;
                        }
                    });

                    SoldListingsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                            Intent intent = new Intent(TransactionHistoryActivity.this, DisplayOffersPage.class);
                            intent.putExtra("objectID", ((ParseObject) adapter.getItemAtPosition(position)).getObjectId());
                            startActivity(intent);
                        }
                    });
                }
            }
        });


    }

    private void populateOffers(){
        final ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Listings");
        query.whereContainsAll("offer_buyer_id", Arrays.asList(user.getObjectId()));
        /*
        query.whereContainedIn(user.getObjectId(), (ArrayList)user.get("offer_made_id"));*/

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> found, ParseException e) {
                ListingAdapter adapter = new ListingAdapter(TransactionHistoryActivity.this, new ArrayList<ParseObject>());
                ArrayList<ParseObject> objects = new ArrayList<ParseObject>(found);

                if (objects != null) {
                    adapter = new ListingAdapter(TransactionHistoryActivity.this, objects);
                }
                if(objects.size()==0){
                    noOffers=true;
                }
                else {
                    offersListView.setAdapter(adapter);
                    offersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
                            Intent intent = new Intent(TransactionHistoryActivity.this, displayFullItem.class);
                            intent.putExtra("objectID", ((ParseObject) adapter.getItemAtPosition(position)).getObjectId());
                            startActivity(intent);
                            return true;
                        }
                    });
                    offersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                            Intent intent = new Intent(TransactionHistoryActivity.this, DisplayOffersPage.class);
                            intent.putExtra("objectID", ((ParseObject) adapter.getItemAtPosition(position)).getObjectId());
                            intent.putExtra("MyOffersOnly", true);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

}
