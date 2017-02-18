package com.tara.tara.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Sreeram Ajay on 17-02-2017.
 */
@IgnoreExtraProperties
public class User {
    String name;
    String email;
    String facebookId;
    public User(){

    }
    public User(String name,String email,String facebookId){
        this.name = name;
        this.email = email;
        this.facebookId = facebookId;
    }
}
