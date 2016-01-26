package com.example.cse110mb260t14.ffs;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.parse.ParseUser;

public class ProfileActivity extends AppCompatActivity {

    TextView name, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        ParseUser currentUser = ParseUser.getCurrentUser();
        name.setText(currentUser.getString("name"));
        email.setText(currentUser.getString("email"));

    }

}
