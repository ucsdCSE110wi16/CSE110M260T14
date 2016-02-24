package com.example.cse110mb260t14.ffs;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

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
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseQuery;
import com.parse.ParseSession;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by liujie on 16/2/22.
 */
public class LoginTest extends ActivityInstrumentationTestCase2<LoginActivity>{

    public static  final String TAG="AppTest";
    private Instrumentation mInstrument;
    private LoginButton loginButton;
    private String name, email, facebookID;
    CallbackManager callbackManager;
    static boolean parseInitialized = false;
    public final static String EXTRA_MESSAGE = "com.example.cse110mb260t14.MESSAGE";
    final List<String> permissions = Arrays.asList("public_profile", "email");

    public LoginTest()
    {
        super("yuan.activity", LoginActivity.class);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mInstrument = getInstrumentation();
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        final List<String> permissions = Arrays.asList("public_profile", "email");

        loginButton = (LoginButton)getActivity().findViewById(R.id.login_button);

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
                        Intent intent = new Intent(getActivity(), DrawerMenuActivity.class);
                        String message = user_name.toString();
                        intent.putExtra(EXTRA_MESSAGE, message);
                        getActivity().startActivity(intent);
                    } else {
                        Log.d("MyApp", "UNABLE TO FIND USER");
                    }
                }
            });


        }


    }

    public void testlogin()
    {
         //check the function of login
        ParseFacebookUtils.logInWithReadPermissionsInBackground(getActivity(), permissions, new LogInCallback() {
            @Override
            public void done(final ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                    // link user
                    if (!ParseFacebookUtils.isLinked(user)) {

                        ParseFacebookUtils.linkWithReadPermissionsInBackground(user, getActivity(), permissions, new SaveCallback() {

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
                    Intent intent = new Intent(getActivity(), DrawerMenuActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    System.out.println("Returning user info updated");
                    // set Parse User Data!
                    user.setEmail(email);
                    user.put("name", name);
                    user.put("facebookID", facebookID);
                    user.saveInBackground();
                    Log.d("MyApp", "User logged in through Facebook!");
                    Intent intent = new Intent(getActivity(), DrawerMenuActivity.class);
                    getActivity().startActivity(intent);
                }

            }
        });
       /* try
        {    Thread.sleep(10000);    Thread.sleep(10000);    Thread.sleep(10000);    Thread.sleep(10000);}
        catch (Exception e)
        {

        }*/

    }
    public void testIslogin()
    {

        //check if the user has already login in
        assertEquals(true, ParseUser.getCurrentUser().isAuthenticated());
    }


}
