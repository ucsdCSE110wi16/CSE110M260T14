package com.example.cse110mb260t14.ffs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by GabrielMechali on 2/28/16.
 */
public class OfferListViewAdapter extends ArrayAdapter<OfferObject> {
    public OfferListViewAdapter(Context context, ArrayList<OfferObject> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        OfferObject object = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.offer_listing_user, parent, false);
        }
        // Lookup view for data population
        TextView Name = (TextView) convertView.findViewById(R.id.potential_buyer_name);
        TextView offer = (TextView) convertView.findViewById(R.id.item_row_offer);
        TextView PhoneNumber = (TextView) convertView.findViewById(R.id.phone_number);
        TextView EmailAddress = (TextView) convertView.findViewById(R.id.email_address);

        // Populate the data into the template view using the data object


        ParseUser objUser = object.retrieveUser();
        Name.setText(objUser.getString("name"));
        offer.setText("$" + object.retrieveValue());
        PhoneNumber.setText(objUser.getString("PhoneNumber"));
        EmailAddress.setText(objUser.getString("email"));

        // Return the completed view to render on screen
        return convertView;
    }
}
