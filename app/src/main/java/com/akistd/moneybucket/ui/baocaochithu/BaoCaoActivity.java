package com.akistd.moneybucket.ui.baocaochithu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.akistd.moneybucket.R;

import java.util.Calendar;

public class BaoCaoActivity extends AppCompatActivity {
    Button btn_tuan,btn_thang;

    ImageButton backButton;

    Button btnDatePickerReport;
    private int mYear, mMonth, mDay, selection = 0;
    Calendar currentDate = Calendar.getInstance();;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_cao);
        addControl();
        addEnvent();

        loadFragment(new QLtheoThang_Fragment());
        btn_tuan.setBackground(getDrawable(R.drawable.button_themhu));
        btn_thang.setBackground(getDrawable(R.drawable.transparent_bg));

    }
    private void addControl(){
        btn_tuan = (Button) findViewById(R.id.btn_tuan);
        btn_thang = (Button) findViewById(R.id.btn_thang);

//        btnDatePickerReport = (Button) findViewById(R.id.btnDatePickerReport);

        backButton = (ImageButton) findViewById(R.id.imgBtnOut);
    }
    private void addEnvent(){
        btn_tuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFragment(new QLtheoThang_Fragment());
                btn_tuan.setBackground(getDrawable(R.drawable.button_themhu));
                btn_thang.setBackground(getDrawable(R.drawable.transparent_bg));
            }
        });
        btn_thang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new QLtheoNam_fragment());
                btn_thang.setBackground(getDrawable(R.drawable.button_themhu));
                btn_tuan.setBackground(getDrawable(R.drawable.transparent_bg));
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    private void Value(Calendar time){

    }



    public void onResume() {
        super.onResume();

    }



    public void loadFragment(Fragment fragment) {
// create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction ft = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        ft.replace(R.id.frg_bcct, fragment);
        ft.commit(); // save the changes
    }
}