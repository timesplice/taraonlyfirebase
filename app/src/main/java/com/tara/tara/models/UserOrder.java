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
    String user;
    String order;
    Double waitingTime=0.0;
    String chefReply;
    Double bill=0.0;
    Long timeStamp=-1L;
    Boolean delivered = false;
    Boolean payment=false;

    public UserOrder() {
    }

    public UserOrder(String hotel, String table,String user,String order) {
        this.hotel = hotel;
        this.table = table;
        this.user = user;
        this.order = order;

    }
}
