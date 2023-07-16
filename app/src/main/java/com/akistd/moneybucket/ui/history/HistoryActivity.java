package com.akistd.moneybucket.ui.history;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.TrangChu_Activity;

public class HistoryActivity extends AppCompatActivity {


    ImageButton backButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        addControls();
        addEvents();
    }

    private void addControls() {

        backButton = (ImageButton) findViewById(R.id.imgBtnOut);

    }

    private void addEvents(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, TrangChu_Activity.class);
                startActivity(intent);
            }
        });

    }
}