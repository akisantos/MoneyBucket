package com.akistd.moneybucket.adapters.testing;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.data.Jars;
import com.akistd.moneybucket.data.MongoDB;

import java.util.ArrayList;

public class JarsInfo extends AppCompatActivity {


    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jars_info);



        listView = findViewById(R.id.usersListView);
        ArrayList<Jars> userList = new ArrayList<>();

        String owner_id = this.getIntent().getStringExtra("owner_id");
        userList.addAll(MongoDB.getInstance().getRealm().where(Jars.class).equalTo("owner_id",owner_id).findAll());
        JarsListApdater apdater = new JarsListApdater(userList);
        listView.setAdapter(apdater);
    }
}