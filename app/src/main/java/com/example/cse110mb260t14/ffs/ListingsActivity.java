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
                System.out.println(categories);
                query.whereContainsAll("Categories", categories);
                query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> found, ParseException e) {
                                System.out.println(found.size());
                                String[] listing_titles = new String[found.size()];
                                for(int i = 0; i < found.size(); i++){
                                        listing_titles[i] = (String) found.get(i).get("Title");
                                        System.out.println(listing_titles[i]);
                                }
                                ArrayAdapter adapter = new ArrayAdapter<String>(ListingsActivity.this, R.layout.category_list_item, listing_titles);
                                ListView listView = (ListView) findViewById(R.id.listings);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapter,View v, int position, long id){
                                    // do later

                                    Intent intent;
                                    System.out.println((String)adapter.getItemAtPosition(position));

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
