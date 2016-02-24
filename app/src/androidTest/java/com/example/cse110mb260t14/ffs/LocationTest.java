package com.example.cse110mb260t14.ffs;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.test.AndroidTestCase;
import android.util.Log;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by liujie on 16/2/22.
 */
public class LocationTest extends AndroidTestCase {

    public static  final String TAG="AppTest";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitude, longitude;
    private Geocoder geocoder;
    private List<Address> addresses;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Parse.enableLocalDatastore(getContext());
        Parse.initialize(getContext());
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        // get location
        startGrabbingLocation(locationManager, locationListener);
    }

    public void testlocation()
    {
        //check if the latitude and longitude are null
        ParseGeoPoint geoLoc = (ParseGeoPoint) ParseUser.getCurrentUser().get("location");
        double latitude = geoLoc.getLatitude();
        double longitude = geoLoc.getLongitude();
        Log.d(TAG, "testlocation: "+latitude+"--"+longitude);
        assertEquals(true,latitude==0?false:true);
    }
    public void testlocationIsNull()
    {
        //check if the address is null
        Log.d(TAG, "testlocationIsNull: "+ParseUser.getCurrentUser().getString("address"));
        assertEquals(true,ParseUser.getCurrentUser().getString("address")==null?false:true);
    }
    public void testuplocation()
    {
        //check the if changing location is successful
        updateLocation(ParseUser.getCurrentUser());

    }
    public void startGrabbingLocation(LocationManager locationManager, LocationListener locationListener) {
        // start the location manager for retrieving GPS coordinates
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
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
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            Toast.makeText(getContext(), "Getting Location From Network Provider", Toast.LENGTH_SHORT).show();
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
                assertEquals(true,latitude==0?false:true);
            }
        }
    }
}
