package com.akistd.moneybucket.data;

import org.bson.types.ObjectId;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Transaction extends RealmObject {
    @PrimaryKey
    @Required
    private ObjectId _id;
    @Required
    private Date create_at;
    @Required
    private Double trans_amount;
    private String trans_note;
    public String getOwner_id() {
        return owner_id;
    }
    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }
    private String owner_id;
    private Users user;
    private Jars jars;
    public Transaction() {
    }
    public Transaction(ObjectId _id, Date create_at, Double trans_amount, String trans_note, String owner_id, Users user, Jars jars) {
        this._id = _id;
        this.create_at = create_at;
        this.trans_amount = trans_amount;
        this.trans_note = trans_note;
        this.owner_id = owner_id;
        this.user = user;
        this.jars = jars;
    }
    // Standard getters & setters
    public ObjectId getId() { return _id; }
    public void setId(ObjectId _id) { this._id = _id; }
    public Date getCreateAt() { return create_at; }
    public void setCreateAt(Date create_at) { this.create_at = create_at; }
    public Double getTransAmount() { return trans_amount; }
    public void setTransAmount(Double trans_amount) { this.trans_amount = trans_amount; }
    public String getTransNote() { return trans_note; }
    public void setTransNote(String trans_note) { this.trans_note = trans_note; }
    public Jars getJars() {
        return jars;
    }
    public void setJars(Jars jars) {
        this.jars = jars;
    }
    public Users getUser() {
        return user;
    }
    public void setUser(Users user) {
        this.user = user;
    }
}

