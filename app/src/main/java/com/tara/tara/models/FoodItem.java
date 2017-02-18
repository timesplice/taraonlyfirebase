package com.tara.tara.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sreeram Ajay on 17-02-2017.
 */
@IgnoreExtraProperties
public class FoodItem {
    String name;
    String shortDesc;
    String desc;
    double price;
    String category;
    int inStockCount;
    String imageUrl;
    public int starCount = 0;
    public int avgStars=0;
    /*star rating range from 0 to 10*/
    public Map<String, Integer> stars = new HashMap<>();

    public FoodItem(){

    }
    public FoodItem(String name,String shortDesc,String desc,double price,String category,String imageUrl){
        this.name =name;
        this.shortDesc = shortDesc;
        this.desc = desc;
        this.price= price;
        this.category = category;
        this.imageUrl = imageUrl;
        inStockCount=-1;
    }
}
