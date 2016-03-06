package com.example.cse110mb260t14.ffs;

import junit.framework.TestCase;

/**
 * Created by kongdeqian1994 on 3/5/16.
 */
public class SignedInActivityTest extends TestCase {
    private SignedInActivity test;
    public void setUp(){
        test =  new SignedInActivity();
        //test.onCreate();
    }
    public void test_SignedInActivity(){
        assertEquals(test.signed_in(),false);
        assertEquals(test.show(),false);
    }
}
