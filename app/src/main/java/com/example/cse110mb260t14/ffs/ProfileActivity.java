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
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.ProfilePictureView;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    TextView name, email;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitude, longitude;
    private Geocoder geocoder;
    private List<Address> addresses;

    TextView location_view;
    Button location_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        ParseUser currentUser = ParseUser.getCurrentUser();
        name.setText(currentUser.getString("name"));
        email.setText(currentUser.getString("email"));
        location_view = (TextView) findViewById(R.id.location_view);

        ProfilePictureView profilePictureView = (ProfilePictureView) findViewById(R.id.profile_photo);
        profilePictureView.setProfileId(ParseUser.getCurrentUser().getString("facebookID"));
        profilePictureView.setPresetSize(ProfilePictureView.LARGE);







        // Get location

        geocoder = new Geocoder(this, Locale.getDefault());
        // get location
        startGrabbingLocation(locationManager, locationListener);

        location_view.setText(ParseUser.getCurrentUser().getString("address") + ", " + ParseUser.getCurrentUser().getString("city"));

        location_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });


        // setup onclicklistener to update location
        location_button = (Button) findViewById(R.id.location_button);
        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLocation(ParseUser.getCurrentUser());
                ((TextView) findViewById(R.id.location_view)).setText(ParseUser.getCurrentUser().getString("address")
                        + ", " + ParseUser.getCurrentUser().getString("city"));

                Toast.makeText(ProfileActivity.this, "Location Updated", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void startGrabbingLocation(LocationManager locationManager, LocationListener locationListener) {
        // start the location manager for retrieving GPS coordinates
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            Toast.makeText(ProfileActivity.this, "Getting Location From Network Provider", Toast.LENGTH_SHORT).show();
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
