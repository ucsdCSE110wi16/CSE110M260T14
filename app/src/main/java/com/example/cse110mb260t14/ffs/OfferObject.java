package com.example.cse110mb260t14.ffs;

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
    String test_user;
    public OfferObject(){};
    public OfferObject(String userid, String value){
        getUser(userid);
        offer = value;
    }

    private void getUser(String userid) {

        ParseQuery<ParseUser> queryUser = ParseUser.getQuery();
        queryUser.whereEqualTo("objectId", userid);
        try {
            List<ParseUser> found = queryUser.find();
            potential_buyer = found.get(0);
        }
        catch (ParseException e){

        }

        /*
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                potential_buyer = objects.get(0);
            }
        });*/
    }

    public ParseUser retrieveUser(){
        return potential_buyer;
    }
    public String retrieveValue(){
        return offer;
    }
    public void make_offer(String userid, String value){
        test_user = userid;
        offer = value;

    }


}
