package com.akistd.moneybucket;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.akistd.moneybucket.adapters.testing.UsersListApdater;
import com.akistd.moneybucket.data.MongoDB;
import com.akistd.moneybucket.data.Users;
import com.akistd.moneybucket.ui.auth.Login;
import com.akistd.moneybucket.util.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.auth.GoogleAuthType;
import io.realm.mongodb.sync.MutableSubscriptionSet;
import io.realm.mongodb.sync.Subscription;
import io.realm.mongodb.sync.SyncConfiguration;

public class MainActivity extends AppCompatActivity {

    Constants util = new Constants();
    Realm realm;
    ListView listView;
    TextView dumdum;
    Button reload, signoutBtn;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    String signinToken="";
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Realm.init(this);
        String appID = util.getAppID() ;
        App app = new App(new AppConfiguration.Builder(appID)
                .appName("MoneyBucket")
                .requestTimeout(30, TimeUnit.SECONDS)
                .build());

        Credentials credentials = Credentials.google(this.getIntent().getStringExtra("signinToken"), GoogleAuthType.ID_TOKEN);
        app.loginAsync(credentials, r->{
            if (r.isSuccess()) {

                Log.v("AKILOGG", "Đăng nhập thành công với tài khoản Google.");
                user = app.currentUser();
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
                Realm.getInstanceAsync(builder, new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        if(isChangingConfigurations() || isFinishing()) {
                            realm.close();
                        } else {
                            MainActivity.this.realm = realm;
                            MongoDB.getInstance().setRealm(realm);
                            onRealmLoaded(realm);
                        }
                    }
                });


            } else {
                Log.e("AKILOGG", "Failed to log in. Error: " + r.getError());
            }
        });
        //MongoDB.getInstance().Connect(this.getIntent().getStringExtra("signinToken"));

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);



    }

    protected void onRealmLoaded(Realm realm) {
        addControls();
        addEvents();

        listView = findViewById(R.id.usersListView);
        ArrayList<Users> userList = new ArrayList<>();
        userList.addAll(realm.where(Users.class).findAll());
        UsersListApdater apdater = new UsersListApdater(userList);
        listView.setAdapter(apdater);

        reload = findViewById(R.id.LoadUsersBtn);
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Users user = new Users();
                user.setUserBalance(1234.0);
                user.setOwner_id(MongoDB.getInstance().getUser().getId());
                user.setUserName("akitest");
                MongoDB.getInstance().insertUsers(user);
                apdater.notifyDataSetChanged();
            }
        });
    }

    private void addControls(){
        signoutBtn = findViewById(R.id.signOutBtn);
        dumdum = findViewById(R.id.dumdum);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(util.getClientID()).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(this);
        if (acc !=null){
            dumdum.setText("Tài khoản:" + acc.getDisplayName() + "\n" + acc.getEmail());
            signinToken = acc.getIdToken();
            Log.v("AKI LOGGIN", signinToken);


        }else{
            Intent login = new Intent(this, Login.class);
            startActivity(login);
        }
    }

    private void addEvents(){
        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }

    private void signOut() {
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(MainActivity.this.getApplicationContext(), Login.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MongoDB.getInstance().Connect(signinToken);
        realm.close();

    }
}