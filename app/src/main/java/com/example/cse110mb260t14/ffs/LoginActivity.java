package com.example.cse110mb260t14.ffs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/*
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;*/


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseQuery;
import com.parse.ParseSession;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;


public class LoginActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private String name, email, facebookID;
    CallbackManager callbackManager;
    static boolean parseInitialized = false;
    public final static String EXTRA_MESSAGE = "com.example.cse110mb260t14.MESSAGE";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    /*
    //Material Design Testing Neccessities
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;
    //Material Design Testing Neccessities
    */

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
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    final JSONObject object,
                                    GraphResponse response) {
                                name = object.optString("name");
                                email = object.optString("email");
                                facebookID = object.optString("id");
                                // Application code
                                Log.v("LoginActivity", response.toString());
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,email,gender,cover,name");
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

        if (ParseUser.getCurrentUser() != null && ParseUser.getCurrentUser().isAuthenticated()) {

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
                        intent.putExtra("istest","1");
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
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
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
                            // set Parse User Data!
                            System.out.println("New user info updated");
                            user.setEmail(email);
                            user.put("name", name);
                            user.put("facebookID", facebookID);
                            user.saveInBackground();
                            Intent intent = new Intent(LoginActivity.this, DrawerMenuActivity.class);
                            intent.putExtra("istest","1");
                            startActivity(intent);
                        } else {
                            System.out.println("Returning user info updated");
                            // set Parse User Data!
                            user.setEmail(email);
                            user.put("name", name);
                            user.put("facebookID", facebookID);
                            user.saveInBackground();
                            Log.d("MyApp", "User logged in through Facebook!");
                            Intent intent = new Intent(LoginActivity.this, DrawerMenuActivity.class);
                            intent.putExtra("istest","1");
                            startActivity(intent);
                        }

                    }
                });
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.cse110mb260t14.ffs/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Login Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.cse110mb260t14.ffs/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}