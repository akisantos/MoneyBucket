package com.akistd.moneybucket.data;

import android.util.Log;

import com.akistd.moneybucket.util.Constants;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.auth.GoogleAuthType;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;

public class MongoDB implements MongoRepository{

    private static MongoDB instance = null;
    private final Constants util = new Constants();

    String appID = util.getAppID() ;
    App app = new App(new AppConfiguration.Builder(appID)
            .appName("MoneyBucket")
            .requestTimeout(30, TimeUnit.SECONDS)
            .build());
    private Realm realm;
    private final ThreadLocal<Realm> localRealms = new ThreadLocal<>();
    private User user = app.currentUser();


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

    public MongoDB(){

    }

    public synchronized void Connect(String uid){
        Credentials credentials = Credentials.google(uid, GoogleAuthType.ID_TOKEN);
        app.loginAsync(credentials, r->{
            if (r.isSuccess()) {

                Log.v("AKILOGG", "Đăng nhập thành công với tài khoản Google.");
                user = app.currentUser();

            } else {
                Log.e("AKILOGG", "Failed to log in. Error: " + r.getError());
            }
        });
        /*try {
            wait();
            app.loginAsync(credentials, r->{
                if (r.isSuccess()) {

                    Log.v("AKILOGG", "Đăng nhập thành công với tài khoản Google.");
                    user = app.currentUser();
                    setDefaultDeviceSyncConfig();
                    notify();
                    ConfigRealm();


                } else {
                    Log.e("AKILOGG", "Failed to log in. Error: " + r.getError());
                }
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/

    }

    public static synchronized MongoDB getInstance(){
        if (instance == null){
            instance = new MongoDB();
        }
        return instance;
    }

    @Override
    public synchronized void ConfigRealm() {

        if (user != null){
            Log.v("AKILOGG", "Successfully init realm!.");
            Log.v("AKI LOGGG", Realm.getDefaultConfiguration().toString());
            Realm.getInstanceAsync(Realm.getDefaultConfiguration(), new Realm.Callback() {
                @Override
                public void onSuccess(Realm realm) {
                    Log.v("AKI LOGG", "Đã mở Realm thành công");
                    MongoDB.this.realm = realm;
                    localRealms.set(realm);

                }
            });

        }
    }

    public SyncConfiguration setDefaultDeviceSyncConfig(){
        SyncConfiguration builder = new SyncConfiguration.Builder(user)
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .initialSubscriptions(new SyncConfiguration.InitialFlexibleSyncSubscriptions() {
                    @Override
                    public void configure(Realm realm, MutableSubscriptionSet subscriptions) {
                        subscriptions.removeAll();
                        subscriptions.add(Subscription.create(realm.where(Users.class).equalTo("owner_id", user.getId())));
                    }})
                .waitForInitialRemoteData(2112, TimeUnit.MILLISECONDS)
                .build();

        Realm.setDefaultConfiguration(builder);

        return builder;
    }

    public Realm openLocalInstance() {
        checkDefaultConfiguration();
        Realm realm = Realm.getDefaultInstance(); // <-- maybe this should be a parameter
        Realm localRealm = localRealms.get();
        if(localRealm == null || localRealm.isClosed()) {
            localRealms.set(realm);
        }
        return realm;
    }

    /**
     * Returns the local Realm instance without adding to the reference count.
     *
     * @return the local Realm instance
     * @throws IllegalStateException when no Realm is open
     */
    public Realm getLocalInstance() {
        Realm realm = localRealms.get();
        if(realm == null || realm.isClosed()) {
            throw new IllegalStateException(
                    "No open Realms were found on this thread.");
        }
        return realm;
    }

    /**
     * Closes local Realm instance, decrementing the reference count.
     *
     * @throws IllegalStateException if there is no open Realm.
     */
    public void closeLocalInstance() {
        checkDefaultConfiguration();
        Realm realm = localRealms.get();
        if(realm == null || realm.isClosed()) {
            throw new IllegalStateException(
                    "Cannot close a Realm that is not open.");
        }
        realm.close();
        // noinspection ConstantConditions
        if(Realm.getLocalInstanceCount(Realm.getDefaultConfiguration()) <= 0) {
            localRealms.set(null);
        }
    }



    private void checkDefaultConfiguration() {
        if(Realm.getDefaultConfiguration() == null) {
            throw new IllegalStateException("No default configuration is set.");
        }
    }




    @Override
    public ArrayList<Users>getData() {
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

    @Override
    public ArrayList<Users> filterData(String name) {
        return null;
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
    public void deletePerson(ObjectId id) {
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

}
