package com.example.cse110mb260t14.ffs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by George on 2/6/2016.
 */
public class ListingsActivity extends AppCompatActivity {
        private Button search_button;
        private ParseQuery<ParseObject> query;
        String description;
        String[] Split_description;
        public static ArrayList<String> categories = new ArrayList<String>();
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_listings);


                query = ParseQuery.getQuery("Listings");
                query.whereContainsAll("Categories", categories);
                query.whereEqualTo("Status", 0);
                query.whereNotEqualTo("SellerID", ParseUser.getCurrentUser().getObjectId());
                query.whereContains("Title", "");
                query.whereContains("objectId", "");
                search_button = (Button) findViewById(R.id.SearchList);
                search_button.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View a) {

                                search_button.setTextColor(Color.BLACK);
                                EditText descriptionText = (EditText) findViewById(R.id.EditTextIdList);
                                description = descriptionText.getText().toString();
                                int sub_postion = 0;
                                for (int i = 0; i < description.length(); i++) {
                                        if (description.charAt(i) == ' ') {
                                                sub_postion++;
                                        } else {
                                                break;
                                        }
                                }
                                description = description.substring(sub_postion);
                                System.out.println("What is :" + description);
                                Split_description = description.split("\\s+");
                                List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
                                int word_limit = 0;
                                if (Split_description.length <= 5) {
                                        word_limit = Split_description.length;
                                } else {
                                        word_limit = 5;
                                }
                                for (int i = 0; i < word_limit; i++) {
                                        ParseQuery<ParseObject> title = ParseQuery.getQuery("Listings");


                                        ParseQuery<ParseObject> description = ParseQuery.getQuery("Listings");


                                        String lowerCase_description = new String(Split_description[i].toLowerCase());

                                        //System.out.println("SOME:" + Split_description[i]);
                                        title.whereContains("objectId", "");
                                        title.whereContains("Title_lower", lowerCase_description);

                                        description.whereContains("objectId", "");
                                        description.whereContains("Description_lower", lowerCase_description);

                                        // System.out.println("Description is : " + Split_description[i]);

                                        queries.add(title);

                                        queries.add(description);

                                }

                                System.out.println("Finish Finding");
                                query = ParseQuery.or(queries);
                                query.whereNotEqualTo("SellerID", ParseUser.getCurrentUser().getObjectId());
                                query.whereEqualTo("Status", 0);
                                query.whereContainsAll("Categories", categories);
                                query.findInBackground(new FindCallback<ParseObject>() {
                                        public void done(List<ParseObject> found, ParseException e) {
                                                search_button.setTextColor(Color.GREEN);
                                                ArrayList<ParseObject> objects = new ArrayList<ParseObject>(found);
                                                ListingAdapter adapter = new ListingAdapter(ListingsActivity.this, objects);
                                                ListView listView = (ListView) findViewById(R.id.listings);
                                                listView.setAdapter(adapter);


                                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                                                                // do later

                                                                System.out.println("CLICKED ON ITEM  " + ((ParseObject) adapter.getItemAtPosition(position)).getString("Title"));

                                                                Intent intent = new Intent(ListingsActivity.this, displayFullItem.class);
                                                                intent.putExtra("objectID", ((ParseObject) adapter.getItemAtPosition(position)).getObjectId());
                                                                System.out.println("LISTING ID IS " + ((ParseObject) adapter.getItemAtPosition(position)).getObjectId());
                                                                startActivity(intent);

                                                        }
                                                });
                                        }
                                });
                                search_button.setTextColor(Color.BLACK);

                        }
                });

                query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> found, ParseException e) {
                                /*
                                System.out.println(found.size());
                                final String[] listing_titles = new String[found.size()];
                                final String[] listing_ids = new String[found.size()];
                                for (int i = 0; i < found.size(); i++) {
                                        listing_titles[i] = (String) found.get(i).get("Title");
                                        listing_ids[i] = found.get(i).getObjectId();
                                        System.out.println("Adding " + listing_ids[i] + " to listing ids array");
                                        System.out.println(listing_titles[i]);
                                }
                                */

                                ArrayList<ParseObject> objects = new ArrayList<ParseObject>(found);
                                ListingAdapter adapter = new ListingAdapter(ListingsActivity.this, objects);
                                ListView listView = (ListView) findViewById(R.id.listings);
                                listView.setAdapter(adapter);


                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                                                // do later

                                                System.out.println("CLICKED ON ITEM  " + ((ParseObject) adapter.getItemAtPosition(position)).getString("Title"));

                                                Intent intent = new Intent(ListingsActivity.this, displayFullItem.class);
                                                intent.putExtra("objectID", ((ParseObject) adapter.getItemAtPosition(position)).getObjectId());
                                                System.out.println("LISTING ID IS " + ((ParseObject) adapter.getItemAtPosition(position)).getObjectId());
                                                startActivity(intent);

                                        }
                                });
                        }
                });





        }
        public static void addCategory(String pCategory) {
               categories.add(pCategory);
        }
        public static void resetCategories() {
                categories.clear();
        }
        public static void addCategories(ArrayList<String> pCategories) {
                categories.addAll(pCategories);
        }


}
