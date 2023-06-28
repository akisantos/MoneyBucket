package com.akistd.moneybucket.data;

import org.bson.types.Decimal128;
import org.bson.types.ObjectId;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Jars extends RealmObject {
    @PrimaryKey
    @Required
    private ObjectId _id;

    @Required
    private Double jar_amount;

    @Required
    private Decimal128 jar_balance;

    @Required
    private String jar_name;

    // Standard getters & setters
    public ObjectId getId() { return _id; }
    public void setId(ObjectId _id) { this._id = _id; }

    public Double getJarAmount() { return jar_amount; }
    public void setJarAmount(Double jar_amount) { this.jar_amount = jar_amount; }

    public Decimal128 getJarBalance() { return jar_balance; }
    public void setJarBalance(Decimal128 jar_balance) { this.jar_balance = jar_balance; }

    public String getJarName() { return jar_name; }
    public void setJarName(String jar_name) { this.jar_name = jar_name; }
}

