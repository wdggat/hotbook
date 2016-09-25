package com.iread.bean;

/**
 * Created by liu on 16/9/19.
 */
public enum Species {
    AMAZON;

    public static Species getFromName(String name) {
        if(name.equals("amazon")) {
            return Species.AMAZON;
        }
        return null;
    }
}
