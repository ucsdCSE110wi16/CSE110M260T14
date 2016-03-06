package com.example.cse110mb260t14.ffs;

import com.parse.ParseObject;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by kongdeqian1994 on 3/5/16.
 */
public class WatchTabTest extends TestCase {
    private ConfirmItemListing test;
    public void setUp(){test = new ConfirmItemListing();}
    public void testWatch_list(){
        ArrayList<String> testbuffer =  new ArrayList<String>();
        ParseObject item = new ParseObject("Listings");
        testbuffer = test.make_watchList(testbuffer,item,"id1", "55", "TeSt_TiTlE", "TeSt_DeScRiPtIoN",
                new String[]{"cat1", "cat2"}, "testID123");
        testbuffer = test.make_watchList(testbuffer,item,"id2", "55", "TeSt_TiTlE", "TeSt_DeScRiPtIoN",
                new String[]{"cat1", "cat2"}, "testID123");
        testbuffer = test.make_watchList(testbuffer,item,"id3", "55", "TeSt_TiTlE", "TeSt_DeScRiPtIoN",
                new String[]{"cat1", "cat2"}, "testID123");
        assertNotNull(testbuffer);
        assertEquals(testbuffer.get(0), "id1");
        assertEquals(testbuffer.get(1), "id2");
        assertEquals(testbuffer.get(2), "id3");


    }

}
