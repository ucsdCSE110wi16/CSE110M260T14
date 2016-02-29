package com.example.cse110mb260t14.ffs;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by GabrielMechali on 2/28/16.
 */
public class OfferObject {

    ParseUser potential_buyer;
    String offer;
    public OfferObject(String userid, String value){
        getUser(userid);
        offer = value;
    }

    private void getUser(String userid){
        ParseQuery<ParseUser> query = new ParseQuery<>("User");
        query.whereEqualTo("objectId", userid);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                potential_buyer = objects.get(0);
            }
        });
    }

    public ParseUser retrieveUser(){
        return potential_buyer;
    }
    public String retrieveValue(){
        return offer;
    }

}
