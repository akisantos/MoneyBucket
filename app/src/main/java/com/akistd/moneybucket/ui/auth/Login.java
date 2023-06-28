package com.akistd.moneybucket.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.akistd.moneybucket.MainActivity;
import com.akistd.moneybucket.R;
import com.akistd.moneybucket.util.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class Login extends AppCompatActivity {

    Constants util = new Constants();
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    SignInButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Setup google auth
        loginButton = findViewById(R.id.google_button);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(util.getClientID()).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

    }

    private void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);

                goToHomeScreen();
            } catch (ApiException e) {
                Toast.makeText(getApplicationContext(), "Có gì đó sai sai!", Toast.LENGTH_SHORT).show();
                Log.v("AKi LOGIN EXCEPTION", e.getMessage().toString());
            }
        }
    }

    private void goToHomeScreen() {
        Intent homeIntent = new Intent(Login.this.getApplicationContext(), MainActivity.class);
        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(this);
        homeIntent.putExtra("signinToken", acc.getIdToken().toString());
        startActivity(homeIntent);

    }
}