package com.akistd.moneybucket.data;

import org.bson.types.ObjectId;

import java.util.ArrayList;

public interface MongoRepository {


    public void ConfigRealm();
    public Object getAllUsersData();
    public ArrayList<Users> filterData(String name);
    public void insertUsers(Users user);
    public void updateUsers(Users user);
    public void deleteUsers(ObjectId id);

    public Object getAllTransaction();
    public void insertTransaction(Transaction transaction);
    public void updateTransaction(Transaction transaction);
    public void deleteTransaction(ObjectId id);

    public Object getAllJars();
    public void insertJar(Jars jars);
    public void updateJar(Jars jars);
    public void deleteJar(ObjectId id);
}
