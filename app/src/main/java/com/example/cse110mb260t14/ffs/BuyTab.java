package com.example.cse110mb260t14.ffs;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.LoginButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by hp1 on 21-01-2015.
 */
public class BuyTab extends Fragment {


    private String mActivityTitle;
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


        TextView name_view = (TextView) v.findViewById(R.id.name_view);
        TextView location_view = (TextView) v.findViewById(R.id.location_view);

        if (ParseUser.getCurrentUser().getString("name") != null) {
            name_view.setText(ParseUser.getCurrentUser().getString("name"));
        }

        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        // get location
        startGrabbingLocation(locationManager, locationListener);

        location_view.setText(ParseUser.getCurrentUser().getString("address") + ", " + ParseUser.getCurrentUser().getString("city"));



        final List<String> permissions = Arrays.asList("public_profile", "email");

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

        // setup onclicklistener to update location
        Button location_button = (Button) v.findViewById(R.id.location_button);

        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLocation(ParseUser.getCurrentUser());
                ((TextView) v.findViewById(R.id.location_view)).setText(ParseUser.getCurrentUser().getString("address")
                        + ", " + ParseUser.getCurrentUser().getString("city"));
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

                                                System.out.println("CLICKED ON ITEM  " + ((ParseObject) adapter.getItemAtPosition(position)).getString("Title"));

                                                Intent intent = new Intent(getActivity(), displayFullItem.class);
                                                intent.putExtra("objectID", ((ParseObject) adapter.getItemAtPosition(position)).getObjectId());
                                                System.out.println("LISTING ID IS " + ((ParseObject) adapter.getItemAtPosition(position)).getObjectId());
                                                startActivity(intent);

                                        }
                                });
                        }
                    });





        return v;

    }



    public void startGrabbingLocation(LocationManager locationManager, LocationListener locationListener) {
        // start the location manager for retrieving GPS coordinates
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                ParseUser user = ParseUser.getCurrentUser();
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                try {
                    addresses = geocoder.getFromLocation(latitude,longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateLocation(user);
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
        // Check permissions
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        try {
            Toast.makeText(getActivity(), "Getting Location From Network Provider", Toast.LENGTH_SHORT).show();
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        }
        catch (IllegalArgumentException e) {
        }
    }


    public void updateLocation(ParseUser user) {
        // Update ParseUser
        if (user != null) {
            user.put("location", new ParseGeoPoint(latitude, longitude));

            if (addresses == null || addresses.size() == 0) {
                try {
                    addresses = geocoder.getFromLocation(latitude,longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else if (addresses.get(0) != null) {
                if (addresses.get(0).getAddressLine(0) != null) {
                    user.put("address", addresses.get(0).getAddressLine(0));
                }
                if (addresses.get(0).getSubLocality() != null) {
                    user.put("city", addresses.get(0).getSubLocality());
                }
                if (addresses.get(0).getAdminArea() != null) {
                    user.put("state", addresses.get(0).getAdminArea());
                }
                if (addresses.get(0).getCountryName() != null) {
                    user.put("country", addresses.get(0).getCountryName());
                }
                if (addresses.get(0).getPostalCode() != null) {
                    user.put("postalCode", addresses.get(0).getPostalCode());
                }
                // save the data to database
                user.saveInBackground();
            }
        }
    }
}