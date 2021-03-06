package com.example.cse110mb260t14.ffs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FullPageBuyer extends AppCompatActivity {

    ParseUser potential_buyer;
    ParseUser curr = ParseUser.getCurrentUser();
    String offerValue, userid, email, phoneNum, Name, listingId;
    ParseObject listing;
    int offerIndex=0;

    private TextView NameTV, EmailTV, NumberTV, OfferTV, MessageTV, OfferStatus;
    private Button AcceptButton, DeclineButton;

    RelativeLayout buttonLayout;

    ArrayList<String> user_offers, value_offers, status_offers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_page_buyer);

        getIntents();
    }


    private void getIntents(){

        userid = getIntent().getStringExtra("userid");
        offerValue = getIntent().getStringExtra("OfferValue");
        listingId = getIntent().getStringExtra("objectId");
        offerIndex = getIntent().getIntExtra("offerIndex", 0);

        if(userid!=null){
            ParseQuery<ParseUser> q = ParseUser.getQuery();
            q.whereEqualTo("objectId", userid);
            try {
                List<ParseUser> list =  q.find();
                potential_buyer = list.get(0);
                email = potential_buyer.getString("email");
                phoneNum = potential_buyer.getString("PhoneNumber");
                Name = potential_buyer.getString("name");
            }
            catch (ParseException e){
                finish();
            }
        }

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Listings");
        query.whereEqualTo("objectId", listingId);
        try {
            List<ParseObject> objects = query.find();
            listing = objects.get(0);
        }
        catch (ParseException e){}
        setGUI();

    }




    private void setGUI() {
        NameTV = (TextView) findViewById(R.id.buyer_name);
        EmailTV = (TextView) findViewById(R.id.buyer_email);
        NumberTV = (TextView) findViewById(R.id.buyer_phone);
        MessageTV = (TextView) findViewById(R.id.buyer_offer);
        OfferTV = (TextView) findViewById(R.id.offer_value);
        user_offers = (ArrayList)listing.get("offer_buyer_id");
        value_offers = (ArrayList)listing.get("offer_value");
        status_offers = (ArrayList) listing.get("offerStatus");

        NameTV.setText(Name);
        EmailTV.setText(email);
        NumberTV.setText(phoneNum);
        MessageTV.setText(Name + " offered $" + offerValue + " for your item " + listing.getString("Title"));
        OfferTV.setText("$" + offerValue);


        buttonLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        OfferStatus = (TextView)findViewById(R.id.offer_status);
        AcceptButton = (Button) findViewById(R.id.accept_button);
        DeclineButton = (Button) findViewById(R.id.decline_button);

        if (status_offers!=null && status_offers.size() > offerIndex && status_offers.get(offerIndex).equals("1")) {
            AcceptOffer();
        }
        else if(status_offers!=null && status_offers.size() > offerIndex && status_offers.get(offerIndex).equals("0")){
            declineOffer();
        }
        else {


            AcceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(FullPageBuyer.this);
                    builder.setTitle("Are you sure you want to accept this offer?");
                    builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setValue("1", status_offers);
                            AcceptOffer();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();

                }
            });

            DeclineButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(FullPageBuyer.this);
                    builder.setTitle("Are you sure you want to decline this offer?");
                    builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setValue("0", status_offers);
                            declineOffer();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });

        }


        EmailTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Regarding item: " + listing.getString("Title"));
                intent.putExtra(Intent.EXTRA_TEXT, "Hi " + Name + "\nI'm contacting you regarding my listing on FFS app");
                startActivity(Intent.createChooser(intent, ""));
            }
        });
    }

    private void setValue(String val, ArrayList<String> st_of) {
        if (status_offers == null) {
            status_offers = new ArrayList<String>();
            for (int i = 0; i < user_offers.size(); i++) {
                status_offers.add("-1");
            }
        }
        status_offers.set(offerIndex, val);
        listing.put("offerStatus", status_offers);
        listing.saveInBackground();
    }

    private void AcceptOffer(){
        AcceptButton.setVisibility(View.GONE);
        DeclineButton.setVisibility(View.GONE);
        OfferStatus.setVisibility(View.VISIBLE);
        OfferStatus.setText("Offer has already been accepted!");
        OfferStatus.setTextColor(Color.GREEN);
    }

    private void declineOffer(){
        AcceptButton.setVisibility(View.GONE);
        DeclineButton.setVisibility(View.GONE);
        OfferStatus.setVisibility(View.VISIBLE);
        OfferStatus.setText("Offer has already been declined!!");
        OfferStatus.setTextColor(Color.RED);
    }
}
