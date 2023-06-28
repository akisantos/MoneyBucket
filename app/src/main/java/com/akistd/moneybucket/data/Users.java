package com.akistd.moneybucket.data;

import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Users extends RealmObject {
    @PrimaryKey
    private ObjectId _id;
    private String owner_id ="";
    private Double user_balance;
    private String user_name;

    private String user_email;
    public Users(){

    }

    // Standard getters & setters
    public ObjectId getId() { return _id; }
    public void setId(ObjectId _id) { this._id = _id; }

    public Double getUserBalance() { return user_balance; }
    public void setUserBalance(Double user_balance) { this.user_balance = user_balance; }

    public String getUserName() { return user_name; }
    public void setUserName(String user_name) { this.user_name = user_name; }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }
}
