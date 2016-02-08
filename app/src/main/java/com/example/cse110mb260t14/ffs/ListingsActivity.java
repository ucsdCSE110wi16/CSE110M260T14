package com.example.cse110mb260t14.ffs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Created by George on 2/6/2016.
 */
public class ListingsActivity extends AppCompatActivity {
        public static ArrayList<String> categories = new ArrayList<String>();
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_listings);


                ParseQuery<ParseObject> query = ParseQuery.getQuery("Listings");
                query.whereContainsAll("Categories", categories);
                query.whereContains("Title", "");
                query.whereContains("objectId", "");
                query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> found, ParseException e) {
                                System.out.println(found.size());
                                final String[] listing_titles = new String[found.size()];
                                final String[] listing_ids = new String[found.size()];
                                for (int i = 0; i < found.size(); i++) {
                                        listing_titles[i] = (String) found.get(i).get("Title");
                                        listing_ids[i] = found.get(i).getObjectId();
                                        System.out.println("Adding " + listing_ids[i] + " to listing ids array");
                                        System.out.println(listing_titles[i]);
                                }


                                ArrayAdapter adapter = new ArrayAdapter<String>(ListingsActivity.this, R.layout.main_list_item, R.id.item_row_title, listing_titles);
                                ListView listView = (ListView) findViewById(R.id.listings);
                                listView.setAdapter(adapter);



                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                                                // do later

                                                System.out.println("CLICKED ON ITEM  " + listing_titles[position]);

                                                Intent intent = new Intent(ListingsActivity.this, displayFullItem.class);
                                                System.out.println((String) adapter.getItemAtPosition(position));
                                                intent.putExtra("objectID", listing_ids[position]);
                                                System.out.println("LISTING ID IS " + listing_ids[position]);
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
