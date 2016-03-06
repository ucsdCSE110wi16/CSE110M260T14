package com.example.cse110mb260t14.ffs;

import junit.framework.TestCase;

/**
 * Created by George on 3/5/2016.
 */
public class displayFullItemTest extends TestCase {
    displayFullItem test;
    public void setUp() {
       test = new displayFullItem();
    }

    public void testMakeFinalCatString() {
        String categoriesString = "[ cat1, null, null ]";
        String finalCatString = test.makefinalCatString(categoriesString);

        assertEquals("cat1", finalCatString);

        categoriesString = "[ cat1, cat2, null ]";
        finalCatString = test.makefinalCatString(categoriesString);

        assertEquals("cat1 and cat2", finalCatString);

        categoriesString = "[ cat1, cat2, cat3 ]";
        finalCatString = test.makefinalCatString(categoriesString);

        assertEquals("cat1, cat2 and cat3", finalCatString);
    }
}
