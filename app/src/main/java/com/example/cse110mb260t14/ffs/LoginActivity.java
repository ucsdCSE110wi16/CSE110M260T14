package com.example.cse110mb260t14.ffs;

import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseQuery;
import com.parse.ParseSession;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private String name, email;
    private Bitmap photo;
    CallbackManager callbackManager;
    static boolean parseInitialized = false;
    public final static String EXTRA_MESSAGE = "com.example.cse110mb260t14.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // [Optional] Power your app with Local Datastore. For more info, go to
        // https://parse.com/docs/android/guide#local-datastore
        if (!parseInitialized) {
            Parse.enableLocalDatastore(this);
            Parse.initialize(this);
            // TODO: possibly change context
            ParseFacebookUtils.initialize(this);
            parseInitialized = true;
        }

        // Facebook login setup
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        final List<String> permissions = Arrays.asList("public_profile", "email");

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_location"));

        callbackManager = CallbackManager.Factory.create();

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("SUCCESS");
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    final JSONObject object,
                                    GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                }
                                else {
                                    name = object.optString("name");
                                    email = object.optString("email");
                                    JSONObject data = response.getJSONObject();
                                    if (data.has("picture")) {
                                            URL profilePicURL = null;
                                            // needs to be done in AsyncTask
                                            new Thread() {
                                                @Override
                                                public void run() {
                                                    URL profilePicURL = null;
                                                    try {
                                                        profilePicURL = new URL("http://www.aeroservice-va.it/download/liveries/repnew/repnewfs9/A321-I-ASDG-L.jpg");
                                                    } catch (MalformedURLException e) {
                                                        e.printStackTrace();
                                                    }
                                                    try {
                                                        InputStream inputStream = (InputStream) profilePicURL.getContent();
                                                        photo = BitmapFactory.decodeStream(inputStream);
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            }.start();
                                    }
                                }
                                // Application code
                                Log.v("LoginActivity", response.toString());
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday, picture");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Log.v("LoginActivity", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.v("LoginActivity", exception.getCause().toString());
            }
        });

        if (ParseUser.getCurrentUser() != null &&  ParseUser.getCurrentUser().isAuthenticated()) {

            final String user_name = ParseUser.getCurrentUser().getUsername();


            ParseQuery<ParseSession> query = ParseQuery.getQuery("UserMaster");
            query.whereEqualTo("user", user_name);
            query.findInBackground(new FindCallback<ParseSession>() {
                public void done(List<ParseSession> objects, ParseException e) {
                    if (e == null) {
                        Log.d("MY APP", "FOUND THE USER was logged in. The user has has object id: " + user_name);
                        Intent intent = new Intent(LoginActivity.this, DrawerMenuActivity.class);
                        String message = user_name.toString();
                        intent.putExtra(EXTRA_MESSAGE, message);
                        startActivity(intent);
                    } else {
                        Log.d("MyApp", "UNABLE TO FIND USER");
                    }
                }
            });


        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, permissions, new LogInCallback() {
                    @Override
                    public void done(final ParseUser user, ParseException err) {
                        if (user == null) {
                            System.out.println("1st");
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
                            System.out.println("2nd");
                            Log.d("MyApp", "User signed up and logged in through Facebook!");
                            // link user
                            if (!ParseFacebookUtils.isLinked(user)) {
                                ParseFacebookUtils.linkWithReadPermissionsInBackground(user, LoginActivity.this, permissions, new SaveCallback() {

                                    @Override
                                    public void done(ParseException ex) {
                                        if (ParseFacebookUtils.isLinked(user)) {
                                            Log.d("MyApp", "Woohoo, user logged in with Facebook!");
                                        }
                                    }
                                });
                            }
                            Intent intent = new Intent(LoginActivity.this, DrawerMenuActivity.class);
                            startActivity(intent);
                        } else {
                            // set Parse User Data!
                            user.setEmail(email);
                            user.put("name", name);
                            user.put("photo", photo);
                            user.saveInBackground();
                            Log.d("MyApp", "User logged in through Facebook!");
                            Intent intent = new Intent(LoginActivity.this, DrawerMenuActivity.class);
                            startActivity(intent);
                        }

                    }
                });
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }



    /*
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.splash, container, false);

        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");

        // Other app specific specialization

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                System.out.println("SUCCESS");
            }

            @Override
            public void onCancel() {
                System.out.println("CANCELED");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("FAILED");
            }
        });
        return view;
    }
    */
}
