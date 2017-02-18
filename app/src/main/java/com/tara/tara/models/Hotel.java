package com.tara.tara.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sreeram Ajay on 17-02-2017.
 */
@IgnoreExtraProperties
public class Hotel {
    String name;
    double ratingValue;
    int ratingCount;
    public Map<String, String> offers = new HashMap<>();

    public Hotel(){

    }
    public Hotel(String name,double ratingValue,int ratingCount,Map<String,String> offers){
        this.name = name;
        this.ratingValue = ratingValue;
        this.ratingCount = ratingCount;
        this.offers = offers;
    }
}
