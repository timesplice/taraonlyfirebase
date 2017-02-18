package com.tara.tara.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Sreeram Ajay on 18-02-2017.
 */

/**
 * Created by hotel and sent to user as a response from hotel for HotelOrder Request
 */
@IgnoreExtraProperties
public class UserOrder {
    public String hotel;
    String table;
    String order;
    Double waitingTime;
    String chefReply;
    Double bill;
    Long timestamp=-1L;
    Boolean payment=false;

    public UserOrder() {
    }

    public UserOrder(String hotel, String table,String order, Double waitingTime, String chefReply, Double bill) {
        this.hotel = hotel;
        this.table = table;
        this.order = order;
        this.waitingTime = waitingTime;
        this.chefReply = chefReply;
        this.bill = bill;

    }
}
