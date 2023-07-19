package com.akistd.moneybucket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.akistd.moneybucket.data.Jars;
import com.akistd.moneybucket.data.MongoDB;
import com.akistd.moneybucket.data.Users;
import com.akistd.moneybucket.databinding.ActivityTrangChuBinding;
import com.akistd.moneybucket.ui.auth.Login;
import com.akistd.moneybucket.ui.homepage.mainpage;
import com.akistd.moneybucket.ui.transaction.TransactionsActivity;
import com.akistd.moneybucket.util.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import org.bson.types.ObjectId;

import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.Credentials;
import io.realm.mongodb.User;
import io.realm.mongodb.auth.GoogleAuthType;

public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs = null;
    Constants util = new Constants();
    Realm realm;
    ListView listView;
    TextView dumdum;
    Button reload, signoutBtn;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    String signinToken="";
    User user;
    App app;

    FloatingActionButton transactionFAB;
    final androidx.fragment.app.FragmentManager mFragmentManager = getSupportFragmentManager();
    final androidx.fragment.app.FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
    final mainpage mFragment = new mainpage();

    private ActivityTrangChuBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Khởi tạo Realm
        Realm.init(this);
        String appID = util.getAppID() ;
        app = new App(new AppConfiguration.Builder(appID)
                .appName("MoneyBucket")
                .requestTimeout(30, TimeUnit.SECONDS)
                .build());

        //Kết nối đến database
        Credentials credentials = Credentials.google(this.getIntent().getStringExtra("signinToken"), GoogleAuthType.ID_TOKEN);
        app.loginAsync(credentials, r->{
            if (r.isSuccess()) {

                Log.v("AKILOGG", "Đăng nhập thành công với tài khoản Google.");
                user = app.currentUser();
                MongoDB.getInstance().setUser(user);

                Realm.setDefaultConfiguration(MongoDB.getInstance().getDefaultDeviceSyncConfig());
                Realm.getInstanceAsync(MongoDB.getInstance().getDefaultDeviceSyncConfig(), new Realm.Callback() {
                    @Override
                    public void onSuccess(Realm realm) {
                        if(isChangingConfigurations() || isFinishing()) {
                            realm.close();
                        } else {
                            MainActivity.this.realm = realm;
                            MongoDB.getInstance().setRealm(realm);

                            onRealmLoaded(realm);
                            Log.v("AKILOGG", "Gán realm thành công!");
                        }
                    }
                });


            } else {
                Log.e("AKILOGG", "Failed to log in. Error: " + r.getError());
                Toast.makeText(this, "Đăng nhập không thành công! Vui lòng thử lại sau...", Toast.LENGTH_SHORT).show();
                signOut();
            }
        });

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //Khởi tạo chạy lần đầu
        prefs = getSharedPreferences("com.akistd.moneyBucket", MODE_PRIVATE);

        //Add controls và events cho mục không cần đến db.
        addControls();
        addEvents();

        //Lấy thông tin đăng nhập
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(util.getClientID()).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
        //Check if user is already registed.

    }


    private void configFirstRun(){
            if (prefs.getBoolean("firstrun", true)) {
                // Do first run stuff here then set 'firstrun' as false
                // using the following line to edit/commit prefs
                RegisterNewUser();
                prefs.edit().putBoolean("firstrun", false).commit();
            }

    }

    private void RegisterNewUser(){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if(realm.where(Users.class).equalTo("owner_id", user.getId()).findFirst() == null){

                    //Khởi tạo người dùng mới
                    Users users = new Users();
                    users.setId(new ObjectId());
                    users.setOwner_id(user.getId());
                    users.setUserBalance(0.0);

                    GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
                    users.setUserName(acc.getDisplayName());
                    users.setUser_email(acc.getEmail());

                    realm.copyToRealm(users);
                    MongoDB.getInstance().setMoneyUsers(users);

                    //Khởi tạo hũ


                    Jars thietyeu = new Jars(new ObjectId(),55,0.0,"Thiết yếu", user.getId());
                    Jars giaoduc = new Jars(new ObjectId(),10,0.0,"Giáo dục", user.getId());
                    Jars tietkiem = new Jars(new ObjectId(),20,0.0,"Tiết kiệm", user.getId());
                    Jars huongthu = new Jars(new ObjectId(),5,0.0,"Hưởng thụ", user.getId());
                    Jars dautu = new Jars(new ObjectId(),5,0.0,"Đầu tư", user.getId());
                    Jars thientam = new Jars(new ObjectId(),5,0.0,"Thiện tâm", user.getId());

                    realm.copyToRealm(thietyeu);
                    realm.copyToRealm(giaoduc);
                    realm.copyToRealm(tietkiem);
                    realm.copyToRealm(huongthu);
                    realm.copyToRealm(dautu);
                    realm.copyToRealm(thientam);


                }else{
                    Users users = realm.where(Users.class).equalTo("owner_id", user.getId()).findFirst();
                    MongoDB.getInstance().setMoneyUsers(users);
                }
            }
        });
    }


    //Khởi chạy khi kết nối db thành công
    protected void onRealmLoaded(Realm realm) {
        configFirstRun();
        RegisterNewUser();
        /*listView = findViewById(R.id.usersListView);
        ArrayList<Users> userList = new ArrayList<>();
        userList.addAll(MongoDB.getInstance().getAllUsersData());
        UsersListApdater apdater = new UsersListApdater(userList);
        listView.setAdapter(apdater);*/


        binding = ActivityTrangChuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.v("AKKI LOG", String.valueOf(item.getItemId()));
                if (item.getItemId() == R.id.navigation_transactions){
                    Intent intent = new Intent(MainActivity.this, TransactionsActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });

        navigationSetup();

    }

    private void navigationSetup(){


        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(this);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_transactions, R.id.navigation_profile)
                .build();



        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_trang_chu);

        NavOptions options = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setEnterAnim(R.anim.enter_from_left)
                .setExitAnim(R.anim.exit_from_right)
                .build();

        NavOptions options1 = new NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setEnterAnim(R.anim.enter_from_right)
                .setExitAnim(R.anim.exit_from_left)
                .build();
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        BottomNavigationView nav = findViewById(R.id.nav_view);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_profile){
                    Bundle bundleProfile = new Bundle();
                    bundleProfile.putString("username", acc.getDisplayName());
                    bundleProfile.putString("userId", app.currentUser().getId());
                    navController.navigate(R.id.navigation_profile,bundleProfile,options1);
                }

                if (item.getItemId() == R.id.navigation_home){
                    /*Bundle bundleHome = new Bundle();
                    bundleHome.putString("username", acc.getDisplayName());*/
                    navController.navigate(R.id.navigation_home,null, options);
                }

                if (item.getItemId() == R.id.navigation_transactions){
                    navController.navigate(R.id.navigation_transactions);
                }
                return false;
            }
        });
        transactionFAB = (FloatingActionButton) findViewById(R.id.transactionFAB);
        transactionFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.navigation_transactions);
            }
        });
        Bundle bundleHome = new Bundle();
        bundleHome.putString("username", acc.getDisplayName());
        navController.navigate(R.id.navigation_home,bundleHome);
    }

    private void addControls(){
        /*signoutBtn = findViewById(R.id.signOutBtn);
        dumdum = findViewById(R.id.dumdum);


        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(this);
        if (acc !=null){
            dumdum.setText("Tài khoản:" + acc.getDisplayName() + "\n" + acc.getEmail());
            signinToken = acc.getIdToken();
            Log.v("AKI LOGGIN", signinToken);


        }else{
            Intent login = new Intent(this, Login.class);
            startActivity(login);
        }*/

    }

    private void addEvents(){
        /*signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });*/


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
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null){
            realm.close();
        }

    }
}