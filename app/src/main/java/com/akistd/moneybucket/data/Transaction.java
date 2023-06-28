package com.akistd.moneybucket.data.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

import java.util.Date;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;

public class Transaction extends RealmObject {
    @PrimaryKey
    @Required
    private ObjectId _id;

    @Required
    private Date create_at;

    @Required
    private ObjectId jar_id;

    @Required
    private Decimal128 trans_amount;

    private String trans_note;

    @Required
    private ObjectId user_id;

    // Standard getters & setters
    public ObjectId getId() { return _id; }
    public void setId(ObjectId _id) { this._id = _id; }

    public Date getCreateAt() { return create_at; }
    public void setCreateAt(Date create_at) { this.create_at = create_at; }

    public ObjectId getJarId() { return jar_id; }
    public void setJarId(ObjectId jar_id) { this.jar_id = jar_id; }

    public Decimal128 getTransAmount() { return trans_amount; }
    public void setTransAmount(Decimal128 trans_amount) { this.trans_amount = trans_amount; }

    public String getTransNote() { return trans_note; }
    public void setTransNote(String trans_note) { this.trans_note = trans_note; }

    public ObjectId getUserId() { return user_id; }
    public void setUserId(ObjectId user_id) { this.user_id = user_id; }
}

