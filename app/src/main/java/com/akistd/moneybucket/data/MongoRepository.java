package com.akistd.moneybucket.data;

import org.bson.types.ObjectId;

import java.util.ArrayList;

public interface MongoRepository {
    public Object getData();
    public ArrayList<Users> filterData(String name);
    public void ConfigRealm();
    public void insertUsers(Users user);
    public void updateUsers(Users user);
    public void deletePerson(ObjectId id);
}
