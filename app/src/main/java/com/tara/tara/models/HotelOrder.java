package com.tara.tara.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sreeram Ajay on 18-02-2017.
 */

/**
 * Created by user by making order and sent to Hotel
 */
@IgnoreExtraProperties
public class HotelOrder {
    public String user;
    public String hotel;
    public String table;
    public String order;
    Map<String,Integer> orderedItems=new HashMap<String,Integer>();
    Boolean payment=false;
    Boolean delivered=false;
    Long timestamp=-1L;

    public HotelOrder(){

    }
    public HotelOrder(String user, String hotel, String table,String order,Map<String,Integer> orderedItems) {
        this.user = user;
        this.hotel = hotel;
        this.table = table;
        this.order = order;
        this.orderedItems=orderedItems;
    }

}
