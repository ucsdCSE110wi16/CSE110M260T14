package com.example.cse110mb260t14.ffs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by GabrielMechali on 2/21/16.
 */

public class OfferListingAdapter extends ArrayAdapter<ParseObject> {
    public OfferListingAdapter(Context context, ArrayList<ParseObject> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ParseObject object = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_listing_with_offers, parent, false);
        }

        //String[] allOfferUsers = object.get("offer_buyer_id");
        ArrayList<String> allOfferUsers = (ArrayList)object.get("offer_buyer_id");
        ArrayList<String> allOfferValues = (ArrayList)object.get("offer_buyer_value");

        ArrayList<OfferObject> listOffersObjects = new ArrayList<OfferObject>();
        if(allOfferUsers==null){
            allOfferUsers = new ArrayList<>();
        }
        if(allOfferValues==null){
            allOfferValues = new ArrayList<>();
        }

        if(allOfferUsers.size()>0 && allOfferValues.size()>0 && allOfferUsers.size()==allOfferValues.size()) {

            for (int i = 0; i < allOfferUsers.size() && i < allOfferValues.size(); i++) {
                OfferObject obj = new OfferObject(allOfferUsers.get(i), allOfferValues.get(i));
                listOffersObjects.add(obj);
            }
        }
        OfferListViewAdapter adapter = new OfferListViewAdapter(getContext(), listOffersObjects);

        // Lookup view for data population
        TextView title = (TextView) convertView.findViewById(R.id.item_row_title);
        TextView price = (TextView) convertView.findViewById(R.id.item_row_price);
        TextView description = (TextView) convertView.findViewById(R.id.item_row_description);
        ListView offersList = (ListView) convertView.findViewById(R.id.list_view_all_offers);
        offersList.setAdapter(adapter);
        offersList.setVisibility(View.VISIBLE);
        // Populate the data into the template view using the data object
        title.setText(object.getString("Title"));
        price.setText("$" + object.getString("Price"));
        description.setText(object.getString("Description"));

        // Return the completed view to render on screen
        return convertView;
    }
}
