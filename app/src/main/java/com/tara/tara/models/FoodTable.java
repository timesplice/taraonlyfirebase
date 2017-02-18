package com.tara.tara.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Sreeram Ajay on 17-02-2017.
 */
@IgnoreExtraProperties
public class FoodTable {
    String tableName;
    int noOfSeats;

    public FoodTable(){

    }
    public FoodTable(String tableName,int noOfSeats){
        this.tableName = tableName;
        this.noOfSeats = noOfSeats;
    }
}

