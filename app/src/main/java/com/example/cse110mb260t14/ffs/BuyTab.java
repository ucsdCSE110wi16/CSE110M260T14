package com.example.cse110mb260t14.ffs;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.EditText;
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
    private Button search_button;
    private ParseQuery<ParseObject> query;
    String description;
    String[] Split_description;
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



        query = ParseQuery.getQuery("Listings");
        query.whereEqualTo("Status", 0);
        query.whereNotEqualTo("SellerID", ParseUser.getCurrentUser().getObjectId());
        query.whereContains("Title", "");
        query.whereContains("objectId", "");
        search_button = (Button) v.findViewById(R.id.Search);
        search_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View a) {
                search_button.setTextColor(Color.BLACK);
                EditText descriptionText = (EditText) v.findViewById(R.id.EditTextId);
                description = descriptionText.getText().toString();
                int sub_postion = 0;
                for(int i = 0; i < description.length(); i++){
                    if(description.charAt(i) == ' '){
                        sub_postion++;
                    }
                    else{
                        break;
                    }
                }
                description = description.substring(sub_postion);
                System.out.println("What is :" + description);
                Split_description = description.split("\\s+");
                List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
                int word_limit = 0;
                if(Split_description.length <=5){
                    word_limit = Split_description.length;
                }
                else{
                    word_limit = 5;
                }
                for (int i = 0; i < word_limit; i++) {
                    ParseQuery<ParseObject> title = ParseQuery.getQuery("Listings");


                    ParseQuery<ParseObject> description = ParseQuery.getQuery("Listings");



                    String lowerCase_description = new String(Split_description[i].toLowerCase());

                    //System.out.println("SOME:" + Split_description[i]);
                    title.whereContains("objectId", "");
                    title.whereContains("Title_lower", lowerCase_description);

                    description.whereContains("objectId", "");
                    description.whereContains("Description_lower", lowerCase_description);

                   // System.out.println("Description is : " + Split_description[i]);

                    queries.add(title);

                    queries.add(description);

                }

                System.out.println("Finish Finding");
                query = ParseQuery.or(queries);
                query.whereNotEqualTo("SellerID", ParseUser.getCurrentUser().getObjectId());
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> found, ParseException e) {
                        search_button.setTextColor(Color.GREEN);
                        ListingAdapter adapter = new ListingAdapter(getActivity(), new ArrayList<ParseObject>());
                        ArrayList<ParseObject> objects = new ArrayList<ParseObject>(found);
                        if (objects != null) {
                            adapter = new ListingAdapter(getActivity(), objects);
                        }
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
                search_button.setTextColor(Color.BLACK);

            }
        });

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> found, ParseException e) {
                ListingAdapter adapter = new ListingAdapter(getActivity(), new ArrayList<ParseObject>());
                ArrayList<ParseObject> objects = new ArrayList<ParseObject>(found);
                if (objects != null) {
                    adapter = new ListingAdapter(getActivity(), objects);
                }
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