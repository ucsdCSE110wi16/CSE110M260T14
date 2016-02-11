package com.example.cse110mb260t14.ffs;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.facebook.login.widget.LoginButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp1 on 21-01-2015.
 */
public class BuyTab extends Fragment {


    private LoginButton login_button;
    private Button postButton;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitude, longitude;
    private Geocoder geocoder;
    private List<Address> addresses;
    View v;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =inflater.inflate(R.layout.buy_layout,container,false);



        login_button = (LoginButton) v.findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });


        postButton = (Button) v.findViewById(R.id.sell_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostItemActivity.class);
                startActivity(intent);
            }
        });



        ParseQuery<ParseObject> query = ParseQuery.getQuery("Listings");
        query.whereContains("Title", "");
        query.whereContains("objectId", "");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> found, ParseException e) {
                ArrayList<ParseObject> objects = new ArrayList<ParseObject>(found);
                ListingAdapter adapter = new ListingAdapter(getActivity(), objects);
                ListView listView = (ListView) v.findViewById(R.id.main_listings);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                        // do later
                        Intent intent = new Intent(getActivity(), displayFullItem.class);
                        intent.putExtra("objectID", ((ParseObject) adapter.getItemAtPosition(position)).getObjectId());
                        startActivity(intent);
                    }
                });
            }
        });

        return v;
    }

}