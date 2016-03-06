package com.example.cse110mb260t14.ffs;

import junit.framework.TestCase;

/**
 * Created by kongdeqian1994 on 3/5/16.
 */
public class OfferObjectTest extends TestCase {
    private OfferObject test1;
    private OfferObject test2;
    private OfferObject test3;

    public void setUp(){
        test1 = new OfferObject();
        test2 = new OfferObject();
        test3 = new OfferObject();
    }
    public void testOffer(){
        test1.make_offer("ID1", "2");
        String offer1 = test1.retrieveValue();
        assertEquals("2", offer1);
        test2.make_offer("ID2", "3");
        String offer2 = test1.retrieveValue();
        assertEquals("2", offer2);
        test3.make_offer("ID3", "3");
        String offer3 = test3.retrieveValue();
        assertEquals("3", offer3);

    }


}
