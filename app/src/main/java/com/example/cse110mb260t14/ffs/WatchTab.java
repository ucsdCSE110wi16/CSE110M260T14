package com.example.cse110mb260t14.ffs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by hp1 on 21-01-2015.
 */
public class WatchTab extends Fragment {


    private ParseUser currentUser = ParseUser.getCurrentUser();
    private ArrayList<String> watchList = (ArrayList)currentUser.get("WatchList");



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.watch_layout,container,false);

        final ListView listView = (ListView) v.findViewById(R.id.watch_listings);
        if (watchList != null) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Listings");
            query.whereContainedIn("objectId", watchList);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> found, ParseException e) {

                    ArrayList<ParseObject> objects = new ArrayList<ParseObject>(found);
                    ListingAdapter adapter = new ListingAdapter(getActivity(), objects);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                            // do late
                            Intent intent = new Intent(getActivity(), displayFullItem.class);
                            intent.putExtra("objectID", ((ParseObject) adapter.getItemAtPosition(position)).getObjectId());
                            startActivity(intent);

                        }
                    });
                }
            });
        }



        return v;
    }
}