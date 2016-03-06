package com.example.cse110mb260t14.ffs;

/**
 * Created by kongdeqian1994 on 3/5/16.
 */

import java.util.Objects;

/**
 * Created by kongdeqian1994 on 3/4/16.
 */
public class TestObject extends Object {
    private String title;
    private String description;
    TestObject(String title, String description ) {
        this.title = title;
        this.description = description;
    }
    public String get_title(){
        return this.title;
    }
    public String get_description(){
        return this.description;
    }
}
