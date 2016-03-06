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

import java.util.List;

/**
 * Created by kongdeqian1994 on 2/6/16.
 */
public class SearchActivity extends ActionBarActivity {
    private List<ParseObject> title_description_res;
    private static int result;


    public TestObject[] search(TestObject[] somequery ,String test){
        TestObject[] result = new TestObject[20];

        String[] Split_description;
        //somequery = ParseQuery.getQuery("Listings");
        //somequery.whereEqualTo("Status", 0);
        //query.whereNotEqualTo("SellerID", ParseUser.getCurrentUser().getObjectId());
        //somequery.whereContains("Title", "");
        //somequery.whereContains("objectId", "");


        int sub_postion = 0;
        for(int i = 0; i < test.length(); i++){
            if(test.charAt(i) == ' '){
                sub_postion++;
            }
            else{
                break;
            }
        }
        test = test.substring(sub_postion);
        //System.out.println("What is :" + test);
        Split_description = test.split("\\s+");
        //List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        int word_limit = 0;
        if(Split_description.length <=5){
            word_limit = Split_description.length;
        }
        else{
            word_limit = 5;
        }
        for (int i = 0; i < word_limit; i++) {
            //ParseQuery<ParseObject> title = ParseQuery.getQuery("Listings");


            //ParseQuery<ParseObject> description = ParseQuery.getQuery("Listings");


            int count = 0;
            String lowerCase_description = new String(Split_description[i].toLowerCase());
            for(int j = 0; j < somequery.length; j++){
                if(somequery[j] == null){
                    break;
                }
                else{
                    if(somequery[j].get_title().contains(Split_description[i]) ||
                            somequery[j].get_description().contains(Split_description[i])){
                        result[count] = somequery[j];
                        count++;
                    }

                }
            }

            //System.out.println("SOME:" + Split_description[i]);


            // System.out.println("Description is : " + Split_description[i]);



        }


        /*try {
            if (somequery.count() == 0) {
                return true;
            }
        }
        catch(ParseException e){

        }*/



        return result;


}
}
