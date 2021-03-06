package com.example.cse110mb260t14.ffs;

import android.Manifest;
import android.app.Activity;
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
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseGeoPoint;
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
    private LoginButton login_button;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitude, longitude;
    private Geocoder geocoder;
    private List<Address> addresses;

    ViewPager pager;
    ViewPagerAdapter adapter;
    ImageView burgerIcon;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"BUY","SELL","WATCH"};
    int Numboftabs =3;
    public static Activity activity;
    public static boolean parseInitialized=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getStringExtra("istest")==null) {
            if (!parseInitialized) {
                try {
                    Parse.enableLocalDatastore(this);
                    Parse.initialize(this);
                    // TODO: possibly change context
                    ParseFacebookUtils.initialize(this);
                    parseInitialized = true;
                }
                catch (RuntimeException e) {

                }
            }

            // Facebook login setup
            FacebookSdk.sdkInitialize(getApplicationContext());
        }
        setContentView(R.layout.activity_main);
        activity = this;




        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);







        addDrawerItems();
        setupDrawer();

        burgerIcon = (ImageView)findViewById(R.id.burger_icon);
        burgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });


        geocoder = new Geocoder(this, Locale.getDefault());
        // get location
        startGrabbingLocation(locationManager, locationListener);




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


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width


        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        /* DONE TABSSS */
    }

    @Override
    public void onBackPressed()
    {
        // super.onBackPressed(); // Comment this super call to avoid calling finish()
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

            // Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            // Called when a drawer has settled in a completely closed state.
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


