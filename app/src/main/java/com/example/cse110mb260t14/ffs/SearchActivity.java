package com.example.cse110mb260t14.ffs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by kongdeqian1994 on 2/6/16.
 */
public class SearchActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void search(View button) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Listings");

        final EditText descriptionText = (EditText) findViewById(R.id.EditTextId);
        String description = descriptionText.getText().toString();
        System.out.println(description);
        query.whereEqualTo("Descriptions", description);
        query.getFirstInBackground(new GetCallback<ParseObject>() {

            public void done(ParseObject object, ParseException e) {
                if (object != null) {
                    TextView textView = (TextView) findViewById(R.id.TextViewContent);
                    String desc = object.getString("Name") + "\n" ;
                    textView.setText(desc);
                    System.out.println("Find item");
                }
                else{
                    System.out.println("Not Find");
                }
            }
        });
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

        return super.onOptionsItemSelected(item);
    }

}
