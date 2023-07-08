package com.akistd.moneybucket.ui.baocaochithu;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.akistd.moneybucket.R;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class BaoCaoActivity extends AppCompatActivity {
    Button btn_tuan,btn_thang;
    ArrayList<ItemHu_bcct> itemHu_bccts;
    AdapterHu_bcct adapterHu_bcct;
    Spinner spinnerhu;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_bao_cao);
        btn_tuan = (Button) findViewById(R.id.btn_tuan);
        btn_thang = (Button) findViewById(R.id.btn_thang);
        spinnerhu = (Spinner)findViewById(R.id.spinnerhu);
        btn_tuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new QLtheoTuan_Fragment());
            }
        });
        btn_thang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new QLtheoThang_fragment());
            }
        });
        gioitinh();


    }
    private void gioitinh(){
        int[] img =new int[]{R.drawable.hu2,R.drawable.hu5,R.drawable.hu1,R.drawable.hu6,R.drawable.hu2,R.drawable.hu4,R.drawable.hu3,};
        String[] namejar = new String[]{"Tất cả","Thiết yếu","Giáo dục","Tiết kiệm","Hưỡng thụ","Đầu tư","Thiện tâm"};

        itemHu_bccts= ItemHu_bcct.initSex(img,namejar);
        adapterHu_bcct =new AdapterHu_bcct(this.getLayoutInflater(),itemHu_bccts,R.layout.item_hu_bcct);
        spinnerhu.setAdapter(adapterHu_bcct);
        spinnerhu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(),item_gioitinhs.get(position).getSex(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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