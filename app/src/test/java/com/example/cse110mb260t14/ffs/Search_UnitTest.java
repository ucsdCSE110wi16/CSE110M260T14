package com.example.cse110mb260t14.ffs;

import android.support.v4.app.FragmentActivity;
import android.widget.Button;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static junit.framework.Assert.assertEquals;

/**
 * Created by kongdeqian1994 on 3/4/16.
 */
/*This test only test the functionality of the search works for all the case
* where user want to search include the user search uppercase or lowercase at most 5 words
*  and also test for the invalid test case like much "   " input*/
public class Search_UnitTest {
    private BuyTab buy;
    //private FragmentActivity activity;
    private Button search;
    private String test_String1;
    private String test_String2;
    private String test_String3;
    private String test_String4;
    private String test_String5;
    private TestObject[] test_lists;
    @Before

    public void setup(){
        //Parse.initialize(this,"G6wgKXsfBYRhA5DV464SceRMkjgxEmMDh0SSs6nQ","FmJNKV6GpVbqTIHC1mkSjyuXmmTEePDH010Ieq7s");
        test_String1 = "watch";
        test_String2 = "";
        test_String3 = "phone";
        test_String4 = "macbook";
        // Object's title and description should be all lower case because we store the
        // title and description in parse in lower case
        TestObject black_watch = new TestObject("watch","color is black");
        TestObject iphone = new TestObject("iphone4s","299");
        TestObject doge = new TestObject("doge","one of my doge");
        TestObject mac = new TestObject("mac","macbook pro 2013");
        test_lists = new TestObject[20];
        test_lists[0] =(black_watch);
        test_lists[1] =(iphone);
        test_lists[2] =(doge);
        test_lists[3] =(mac);


    }

    @Test
    public void test_search_function(){
        // Parse.initialize(this);
        SearchActivity search = new SearchActivity();
        TestObject[] test1;
        TestObject[] test2;
        TestObject[] test3;
        TestObject[] test4;


        //Test for usually case
        test1= search.search(test_lists, test_String1);
        assertEquals(test1[0].get_title().contains(test_String1), true);
        test2 = search.search(test_lists, test_String2);
        assertEquals(test2[0].get_title().contains(test_String2), true);
        test3 = search.search(test_lists, test_String3);
        assertEquals(test3[0].get_title().contains(test_String3), true);
        test4 = search.search(test_lists, test_String4);
        assertEquals(test4[0].get_description().contains(test_String4), true);






    }




}