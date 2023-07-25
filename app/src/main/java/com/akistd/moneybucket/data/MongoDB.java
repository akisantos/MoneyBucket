package com.akistd.moneybucket.data;

import android.util.Log;

import com.akistd.moneybucket.util.Constants;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.mongodb.App;
import io.realm.mongodb.User;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;

public class MongoDB implements MongoRepository{

    private static MongoDB instance = null;
    private final Constants util = new Constants();
    String appID = util.getAppID() ;
    App app;
    private Realm realm;
    private User user;
    private Users moneyUsers;

    public Users getMoneyUsers() {
        return moneyUsers;
    }

    public void setMoneyUsers(Users moneyUsers) {
        this.moneyUsers = moneyUsers;
    }

    public Realm getRealm() {
        return realm;
    }

    public void setRealm(Realm realm) {
        this.realm = realm;
    }

    public App getApp() {
        return app;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MongoDB(){

    }

    public static synchronized MongoDB getInstance(){
        if (instance == null){
            instance = new MongoDB();
        }
        return instance;
    }



    public SyncConfiguration getDefaultDeviceSyncConfig(){
        SyncConfiguration builder = new SyncConfiguration.Builder(user)
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .initialSubscriptions(new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
                    @Override
                    public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                        subscriptions.removeAll();
                        subscriptions.add(Subscription.create(realm.where(Users.class).equalTo("owner_id", user.getId())));
                        subscriptions.add(Subscription.create(realm.where(Jars.class).equalTo("owner_id", user.getId())));
                        subscriptions.add(Subscription.create(realm.where(Transaction.class).equalTo("owner_id", user.getId())));
                    }})

                .waitForInitialRemoteData(2112, TimeUnit.MILLISECONDS)
                .build();

        Realm.setDefaultConfiguration(builder);
        return builder;
    }




    @Override
    public ArrayList<Users>getAllUsersData() {
        ArrayList<Users> data = new ArrayList<>();
        if (realm == null){
            ConfigRealm();
        }
        realm.executeTransaction(r ->{
            RealmResults<Users> dataList = r.where(Users.class).findAll();
            for (Users u: dataList
                 ) {
                data.add(u);
            }
        });
        return data;
    }


    //DATA SINGLETON
    @Override
    public ArrayList<Users> filterData(String name) {
        return null;
    }

    @Override
    public void ConfigRealm() {
        SyncConfiguration builder = new SyncConfiguration.Builder(user)
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .initialSubscriptions(new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
                    @Override
                    public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                        subscriptions.removeAll();
                        subscriptions.add(Subscription.create(realm.where(Users.class).equalTo("owner_id", user.getId())));
                        subscriptions.add(Subscription.create(realm.where(Jars.class).equalTo("owner_id", user.getId())));
                        subscriptions.add(Subscription.create(realm.where(Transaction.class).equalTo("owner_id", user.getId())));
                    }})

                .waitForInitialRemoteData(2112, TimeUnit.MILLISECONDS)
                .build();

        Realm.setDefaultConfiguration(builder);
    }

    @Override
    public void insertUsers(Users user) {
        if (user != null){
            realm.executeTransaction( r ->{
                try {
                    user.setId(new ObjectId());
                    r.copyToRealm(user);
                }catch (Exception e){
                    Log.v("AKI EXCEPTION", e.getMessage().toString());
                }
            });
        }
    }

    @Override
    public void updateUsers(Users givenuser) {
        if (user != null){
            realm.executeTransaction( r ->{
                try {

                    Users queriedUsers = r.where(Users.class).equalTo("_id == $0", givenuser.getId()).findFirst();
                    if (queriedUsers!= null) {
                        queriedUsers.setUserName(givenuser.getUserName());
                        queriedUsers.setUserBalance(givenuser.getUserBalance());
                    }
                }catch (Exception e){
                    Log.v("AKI EXCEPTION", e.getMessage().toString());
                }
            });
        }
    }

    @Override
    public void deleteUsers(ObjectId id) {
        if (user != null){
            realm.executeTransaction( r ->{
                try {
                    Users u = r.where(Users.class).equalTo("_id==$0", id).findFirst();
                    u.deleteFromRealm();
                }catch (Exception e){
                    Log.v("AKI EXCEPTION", e.getMessage().toString());
                }
            });
        }
    }

    @Override
    public  ArrayList<Transaction> getAllTransaction() {
        ArrayList<Transaction> jars = new ArrayList<>();
        realm.executeTransaction(r ->{
            try {
                Transaction[] jarsList = r.where(Transaction.class).findAll().toArray(new Transaction[0]);
                for (Transaction jar: jarsList) {
                    jars.add(jar);
                }
            }catch (Exception e){
                Log.v("AKI EXCEPTION", e.getMessage().toString());
            }
        });
        return jars;
    }

    public ArrayList<Transaction> getAllSortedTransaction(){
        ArrayList<Transaction> dataList = new ArrayList<>();
        realm.executeTransaction(r->{
            try {
                Transaction[] trans = realm.where(Transaction.class).sort("create_at", Sort.DESCENDING).findAll().toArray(new Transaction[0]);
                dataList.addAll(Arrays.asList(trans));

            }catch (Exception e){
                Log.v("AKI EXCEPTION", e.getMessage().toString());
            }
        });

        return dataList;
    }

    public ArrayList<Transaction> getAllSortedIncomeTransaction(){
        ArrayList<Transaction> dataList = new ArrayList<>();
        realm.executeTransaction(r->{
            try {
                Transaction[] trans = realm.where(Transaction.class).sort("create_at", Sort.DESCENDING).greaterThan("trans_amount",0).findAll().toArray(new Transaction[0]);
                dataList.addAll(Arrays.asList(trans));

            }catch (Exception e){
                Log.v("AKI EXCEPTION", e.getMessage().toString());
            }
        });

        return dataList;
    }

    public ArrayList<Transaction> getThisMonthSortedOutcomeTransaction(){
        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        calendarStart.set(Calendar.HOUR, 0);
        calendarStart.set(Calendar.MINUTE, 0);
        calendarStart.set(Calendar.SECOND, 0);

        //Log.v("AKKI LOG", "Start!! "+String.valueOf(calendarStart.getTime()));
        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(Calendar.DAY_OF_MONTH,calendarEnd.getActualMaximum(Calendar.DATE));
        calendarEnd.set(Calendar.HOUR, 12);
        calendarEnd.set(Calendar.MINUTE, 0);
        calendarEnd.set(Calendar.SECOND, 0);

        //Log.v("AKKI LOG", "END!! "+String.valueOf(calendarEnd.getTime()));
        ArrayList<Transaction> dataList = new ArrayList<>();
        realm.executeTransaction(r->{
            try {
                Transaction[] trans = realm.where(Transaction.class).greaterThanOrEqualTo("create_at", calendarStart.getTime())
                        .lessThan("create_at",calendarEnd.getTime()).sort("create_at", Sort.DESCENDING).lessThan("trans_amount",0).findAll().toArray(new Transaction[0]);
                dataList.addAll(Arrays.asList(trans));

                for (Transaction tr: dataList) {
                    Log.v("DATA IN MONTH LOG", tr.getTransAmount().toString());
                }
            }catch (Exception e){
                Log.v("AKI EXCEPTION", e.getMessage().toString());
            }
        });

        return dataList;
    }

    public ArrayList<Transaction> getAllSortedOutcomeTransaction(){
        ArrayList<Transaction> dataList = new ArrayList<>();
        realm.executeTransaction(r->{
            try {
                Transaction[] trans = realm.where(Transaction.class).sort("create_at", Sort.DESCENDING).lessThan("trans_amount",0).findAll().toArray(new Transaction[0]);
                dataList.addAll(Arrays.asList(trans));

            }catch (Exception e){
                Log.v("AKI EXCEPTION", e.getMessage().toString());
            }
        });

        return dataList;
    }

    public ArrayList<Transaction> checkTransactionIsExists(Transaction t){
        ArrayList<Transaction> dataList = new ArrayList<>();
        if (user != null){
            realm.executeTransaction( r ->{
                try {
                    Transaction trans = r.where(Transaction.class).equalTo("_id",t.getId()).findFirst();
                    dataList.add(trans);
                }catch (Exception e){
                    Log.v("AKI EXCEPTION", e.getMessage().toString());
                }
            });
        }
        return  dataList;
    }

    @Override
    public void insertTransaction(Transaction transaction) {
        if (user != null){
            realm.executeTransaction( r ->{
                try {
                    transaction.setId(new ObjectId());
                    transaction.setOwner_id(user.getId());
                    r.copyToRealm(transaction);
                }catch (Exception e){
                    Log.v("AKI EXCEPTION", e.getMessage().toString());
                }
            });
        }
    }

    @Override
    public void updateTransaction(Transaction transaction) {
        realm.executeTransaction( r ->{
            try {

                Transaction queriedTrans = r.where(Transaction.class).equalTo("_id == $0", transaction.getId()).findFirst();
                if (queriedTrans!= null) {
                    queriedTrans = transaction;
                    realm.copyToRealmOrUpdate(queriedTrans);
                }
            }catch (Exception e){
                Log.v("AKI EXCEPTION", e.getMessage().toString());
            }
        });
    }

    @Override
    public void deleteTransaction(ObjectId id) {
        realm.executeTransaction( r ->{
            try {
                Transaction u = r.where(Transaction.class).equalTo("_id", id).findFirst();
                u.deleteFromRealm();
            }catch (Exception e){
                Log.v("AKI EXCEPTION", e.getMessage().toString());
            }
        });
    }

    @Override
    public ArrayList<Jars> getAllJars() {
        ArrayList<Jars> jars = new ArrayList<>();
        realm.executeTransaction(r ->{
            try {
                Jars[] jarsList = r.where(Jars.class).findAll().toArray(new Jars[0]);
                for (Jars jar: jarsList) {
                    jars.add(jar);
                }
            }catch (Exception e){
                Log.v("AKI EXCEPTION", e.getMessage().toString());
            }
        });
        return jars;
    }

    @Override
    public void insertJar(Jars jars) {
        if (user != null){
            realm.executeTransaction( r ->{
                try {
                    jars.setId(new ObjectId());
                    jars.setOwner_id(user.getId());
                    r.copyToRealm(jars);
                }catch (Exception e){
                    Log.v("AKI EXCEPTION", e.getMessage().toString());
                }
            });
        }
    }

    @Override
    public void updateJar(Jars jars) {
        realm.executeTransaction( r ->{
            try {

                Jars queriedTrans = r.where(Jars.class).equalTo("_id", jars.getId()).findFirst();
                if (queriedTrans!= null) {
                    queriedTrans = jars;
                    realm.copyToRealmOrUpdate(queriedTrans);
                }
            }catch (Exception e){
                Log.v("AKI EXCEPTION", e.getMessage().toString());
            }
        });
    }

    @Override
    public void deleteJar(ObjectId id) {
        realm.executeTransaction( r ->{
            try {
                Jars u = r.where(Jars.class).equalTo("_id", id).findFirst();
                u.deleteFromRealm();
            }catch (Exception e){
                Log.v("AKI EXCEPTION", e.getMessage().toString());
            }
        });
    }

}
