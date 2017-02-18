package com.tara.tara.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Sreeram Ajay on 17-02-2017.
 */
@IgnoreExtraProperties
public class Comment {
    String userName;
    String comment;
    String userId;
    public Comment(){

    }
    public Comment(String userName,String comment,String userId){
        this.userName = userName;
        this.comment = comment;
        this.userId = userId;
    }
}
