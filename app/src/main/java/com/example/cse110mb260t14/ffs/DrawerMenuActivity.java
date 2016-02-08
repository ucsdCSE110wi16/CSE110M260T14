package com.example.cse110mb260t14.ffs;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DrawerMenuActivity extends ActionBarActivity {

    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private LoginButton login_button;
    private Button postButton;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitude, longitude;
    private Geocoder geocoder;
    private List<Address> addresses;


    TextView usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();


        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);


        addDrawerItems();
        setupDrawer();

        usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        usernameTextView.setText(message);

        TextView name_view = (TextView) findViewById(R.id.name_view);
        TextView location_view = (TextView) findViewById(R.id.location_view);

        if (ParseUser.getCurrentUser().getString("name") != null) {
            name_view.setText(ParseUser.getCurrentUser().getString("name"));
        }

        geocoder = new Geocoder(this, Locale.getDefault());
        // get location
        startGrabbingLocation(locationManager, locationListener);

        location_view.setText(ParseUser.getCurrentUser().getString("address") + ", " + ParseUser.getCurrentUser().getString("city"));

        final List<String> permissions = Arrays.asList("public_profile", "email");

        login_button = (LoginButton) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent intent = new Intent(DrawerMenuActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        postButton = (Button) findViewById(R.id.sell_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawerMenuActivity.this, PostItemActivity.class);
                startActivity(intent);
            }
        });

        // setup onclicklistener to update location
        Button location_button = (Button) findViewById(R.id.location_button);
        location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateLocation(ParseUser.getCurrentUser());
                ((TextView) findViewById(R.id.location_view)).setText(ParseUser.getCurrentUser().getString("address")
                        + ", " + ParseUser.getCurrentUser().getString("city"));
            }
        });
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Listings");
                    query.whereContains("Title", "");
                    query.whereContains("objectId", "");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> found, ParseException e) {
                            System.out.println(found.size());
                            String[] listing_titles = new String[found.size()];
                            final String[] listing_ids = new String[found.size()];
                            for (int i = 0; i < found.size(); i++) {
                                listing_titles[i] = (String) found.get(i).get("Title");
                                listing_ids[i] = found.get(i).getObjectId();
                                System.out.println("Adding " + listing_ids[i] + " to listing ids array");
                                System.out.println(listing_titles[i]);
                            }
                            ArrayAdapter adapter = new ArrayAdapter<String>(DrawerMenuActivity.this, R.layout.main_list_item, listing_titles);
                            ListView listView = (ListView) findViewById(R.id.main_listings);
                            listView.setAdapter(adapter);
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                                    // do later

                                    Intent intent = new Intent(DrawerMenuActivity.this, displayFullItem.class);
                                    System.out.println((String) adapter.getItemAtPosition(position));
                                    intent.putExtra("objectID", listing_ids[position]);
                                    System.out.println("LISTING ID IS " + listing_ids[position]);
                                    startActivity(intent);

                                }
                            });
                        }
                    });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void addDrawerItems() {
        String[] osArray = {"Profile", "Categories", "Transaction History", "Logout"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent Profile = new Intent(DrawerMenuActivity.this, ProfileActivity.class);
                        startActivity(Profile);
                        break;
                    case 1:
                        Intent Categories = new Intent(DrawerMenuActivity.this, CategoriesActivity.class);
                        startActivity(Categories);
                        break;
                    case 2:
                        Intent TransactionHistory = new Intent(DrawerMenuActivity.this, TransactionHistoryActivity.class);
                        startActivity(TransactionHistory);
                        break;
                    case 3:
                        login_button.performClick();
                        break;
                }
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state.*/
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state.*/
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            Toast.makeText(DrawerMenuActivity.this, "Getting Location From Network Provider", Toast.LENGTH_SHORT).show();
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


