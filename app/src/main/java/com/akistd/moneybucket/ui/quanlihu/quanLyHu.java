package com.akistd.moneybucket.ui.quanlihu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.data.Jars;
import com.akistd.moneybucket.data.MongoDB;
import com.akistd.moneybucket.ui.homepage.mainpage;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Objects;

public class quanLyHu extends AppCompatActivity {

    ImageButton qlh_imgbtn_back;

    EditText qlh_jars_thietyeu_edt, qlh_jars_giaoduc_edt, qlh_jars_tietkiem_edt, qlh_jars_huongthu_edt, qlh_jars_dautu_edt, qlh_jars_thientam_edt;
    ImageButton qlh_jars_thietyeu_imgbtn_plus, qlh_jars_thietyeu_imgbtn_minus, qlh_jars_giaoduc_imgbtn_plus, qlh_jars_giaoduc_imgbtn_minus, qlh_jars_tietkiem_imgbtn_plus,
            qlh_jars_tietkiem_imgbtn_minus, qlh_jars_huongthu_imgbtn_plus, qlh_jars_huongthu_imgbtn_minus, qlh_jars_dautu_imgbtn_plus, qlh_jars_dautu_imgbtn_minus,
            qlh_jars_thientam_imgbtn_plus, qlh_jars_thientam_imgbtn_minus;

    AppCompatButton btn_luuHu;

    MongoDB db;

    ArrayList<Jars> JarsAList = MongoDB.getInstance().getAllJars();
    Integer percThietYeu =  JarsAList.get(0).getJarAmount();
    Integer percGiaoDuc = JarsAList.get(1).getJarAmount();
    Integer percTietKiem =  JarsAList.get(2).getJarAmount();
    Integer percHuongThu = JarsAList.get(3).getJarAmount();
    Integer percDauTu =  JarsAList.get(4).getJarAmount();
    Integer percThienTam = JarsAList.get(5).getJarAmount();


    ArrayList<Integer> jarAmountList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_hu);

        for (Jars jar:JarsAList) {
            if (Objects.equals(jar.getJarName(), "Thiết yếu")){
                jarAmountList.add(0, jar.getJarAmount());
            }

            if (Objects.equals(jar.getJarName(), "Giáo dục")){
                jarAmountList.add(1, jar.getJarAmount());
            }

            if (Objects.equals(jar.getJarName(), "Tiết kiệm")){
                jarAmountList.add(2, jar.getJarAmount());
            }

            if (Objects.equals(jar.getJarName(), "Hưởng thụ")){
                jarAmountList.add(3, jar.getJarAmount());
            }

            if (Objects.equals(jar.getJarName(), "Đầu tư")){
                jarAmountList.add(4, jar.getJarAmount());
            }

            if (Objects.equals(jar.getJarName(), "Thiện tâm")){
                jarAmountList.add(5, jar.getJarAmount());
            }

        }

        addControls();
        addEvents();
    }


    public void addControls() {
        //EditText
        qlh_jars_thietyeu_edt = (EditText) findViewById(R.id.qlh_jars_thietyeu_edt);
        qlh_jars_giaoduc_edt = (EditText) findViewById(R.id.qlh_jars_giaoduc_edt);
        qlh_jars_tietkiem_edt = (EditText) findViewById(R.id.qlh_jars_tietkiem_edt);
        qlh_jars_huongthu_edt = (EditText) findViewById(R.id.qlh_jars_huongthu_edt);
        qlh_jars_dautu_edt = (EditText) findViewById(R.id.qlh_jars_dautu_edt);
        qlh_jars_thientam_edt = (EditText) findViewById(R.id.qlh_jars_thientam_edt);
        //ImageButton
        qlh_jars_thietyeu_imgbtn_plus = (ImageButton) findViewById(R.id.qlh_jars_thietyeu_imgbtn_plus);
        qlh_jars_thietyeu_imgbtn_minus = (ImageButton) findViewById(R.id.qlh_jars_thietyeu_imgbtn_minus);

        qlh_jars_giaoduc_imgbtn_plus = (ImageButton) findViewById(R.id.qlh_jars_giaoduc_imgbtn_plus);
        qlh_jars_giaoduc_imgbtn_minus = (ImageButton) findViewById(R.id.qlh_jars_giaoduc_imgbtn_minus);

        qlh_jars_tietkiem_imgbtn_plus = (ImageButton) findViewById(R.id.qlh_jars_tietkiem_imgbtn_plus);
        qlh_jars_tietkiem_imgbtn_minus = (ImageButton) findViewById(R.id.qlh_jars_tietkiem_imgbtn_minus);

        qlh_jars_huongthu_imgbtn_plus = (ImageButton) findViewById(R.id.qlh_jars_huongthu_imgbtn_plus);
        qlh_jars_huongthu_imgbtn_minus = (ImageButton) findViewById(R.id.qlh_jars_huongthu_imgbtn_minus);

        qlh_jars_dautu_imgbtn_plus = (ImageButton) findViewById(R.id.qlh_jars_dautu_imgbtn_plus);
        qlh_jars_dautu_imgbtn_minus = (ImageButton) findViewById(R.id.qlh_jars_dautu_imgbtn_minus);

        qlh_jars_thientam_imgbtn_plus = (ImageButton) findViewById(R.id.qlh_jars_thientam_imgbtn_plus);
        qlh_jars_thientam_imgbtn_minus = (ImageButton) findViewById(R.id.qlh_jars_thientam_imgbtn_minus);
        qlh_imgbtn_back = (ImageButton) findViewById(R.id.qlh_imgbtn_back);

        btn_luuHu = findViewById(R.id.btn_luuHu);
    }


    @SuppressLint("ClickableViewAccessibility")
    public void addEvents(){
        //Load pie chart
        PieChart pieChart = findViewById(R.id.pieChart_tyLeHu);
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) percThietYeu,"Thiết yếu"));
        entries.add(new PieEntry((float) percGiaoDuc,"Giáo dục"));
        entries.add(new PieEntry((float) percTietKiem,"Tiết kiệm"));
        entries.add(new PieEntry((float) percHuongThu,"Hưởng thụ"));
        entries.add(new PieEntry((float) percDauTu,"Đầu tư"));
        entries.add(new PieEntry((float) percThienTam,"Thiện tâm"));
        PieDataSet pieDataSet  = new PieDataSet(entries,"");
        pieDataSet.setValueTextSize(0f);
        pieDataSet.setColors(Color.parseColor("#0094FF"),Color.parseColor("#00FFE0"),Color.parseColor("#E84DE2"),Color.parseColor("#FFD615"),Color.parseColor("#FF1F5A"),Color.parseColor("#14FF00"),Color.parseColor("#14FF00"));
        PieData pieData  = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(100);
        pieChart.invalidate();
        
        //=======================================================================
        qlh_imgbtn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //=======================================================================
        btn_luuHu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateJar();
            }
        });

        qlh_jars_thietyeu_edt.setText(String.valueOf(percThietYeu) +"%");
        qlh_jars_giaoduc_edt.setText(String.valueOf(percGiaoDuc) +"%");
        qlh_jars_tietkiem_edt.setText(String.valueOf(percTietKiem) +"%");
        qlh_jars_huongthu_edt.setText(String.valueOf(percHuongThu) +"%");
        qlh_jars_dautu_edt.setText(String.valueOf(percDauTu) +"%");
        qlh_jars_thientam_edt.setText(String.valueOf(percThienTam) +"%");
        //Thiet yeu ===============================================================  0
        qlh_jars_thietyeu_imgbtn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jarAmountList.set(0,jarAmountList.get(0)+5);
                qlh_jars_thietyeu_edt.setText(jarAmountList.get(0) + "%");

            }
        });

        qlh_jars_thietyeu_imgbtn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jarAmountList.set(0,jarAmountList.get(0)-5);
                qlh_jars_thietyeu_edt.setText(jarAmountList.get(0) + "%");

            }
        });

        qlh_jars_thietyeu_imgbtn_plus.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN -> {
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 100);
                    }
                    case MotionEvent.ACTION_UP -> {
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                    }
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    addPorpotion(0 ,qlh_jars_thietyeu_edt,1);
                    mHandler.postDelayed(this, 100);
                }
            };
        });

        qlh_jars_thietyeu_imgbtn_minus.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN -> {
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 100);
                    }
                    case MotionEvent.ACTION_UP -> {
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                    }
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    addPorpotion(0 ,qlh_jars_thietyeu_edt,-1);
                    mHandler.postDelayed(this, 100);
                }
            };
        });

        //Giao duc ===============================================================  1
        qlh_jars_giaoduc_imgbtn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jarAmountList.set(1,jarAmountList.get(1)+5);
                qlh_jars_giaoduc_edt.setText(jarAmountList.get(1) + "%");

            }
        });

        qlh_jars_giaoduc_imgbtn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jarAmountList.set(1,jarAmountList.get(1)-5);
                qlh_jars_giaoduc_edt.setText(jarAmountList.get(1) + "%");

            }
        });

        qlh_jars_giaoduc_imgbtn_plus.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN -> {
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 100);
                    }
                    case MotionEvent.ACTION_UP -> {
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                    }
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    addPorpotion(1 ,qlh_jars_giaoduc_edt,1);
                    mHandler.postDelayed(this, 100);
                }
            };
        });

        qlh_jars_giaoduc_imgbtn_minus.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN -> {
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 100);
                    }
                    case MotionEvent.ACTION_UP -> {
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                    }
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    addPorpotion(1 ,qlh_jars_giaoduc_edt,-1);
                    mHandler.postDelayed(this, 100);
                }
            };
        });

        //Tiet kiem=========================================================================  2
        qlh_jars_tietkiem_imgbtn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jarAmountList.set(2,jarAmountList.get(2)+5);
                qlh_jars_tietkiem_edt.setText(jarAmountList.get(2) + "%");

            }
        });

        qlh_jars_tietkiem_imgbtn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jarAmountList.set(2,jarAmountList.get(2)-5);
                qlh_jars_tietkiem_edt.setText(jarAmountList.get(2) + "%");

            }
        });

        qlh_jars_tietkiem_imgbtn_plus.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN -> {
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 100);
                    }
                    case MotionEvent.ACTION_UP -> {
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                    }
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    addPorpotion(2 ,qlh_jars_tietkiem_edt,1);
                    mHandler.postDelayed(this, 100);
                }
            };
        });

        qlh_jars_tietkiem_imgbtn_minus.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN -> {
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 100);
                    }
                    case MotionEvent.ACTION_UP -> {
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                    }
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    addPorpotion(2 ,qlh_jars_tietkiem_edt,-1);
                    mHandler.postDelayed(this, 100);
                }
            };
        });

        //Huong thu ================================= 3
        qlh_jars_huongthu_imgbtn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jarAmountList.set(3,jarAmountList.get(3)+5);
                qlh_jars_huongthu_edt.setText(jarAmountList.get(3) + "%");

            }
        });

        qlh_jars_huongthu_imgbtn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jarAmountList.set(3,jarAmountList.get(3)-5);
                qlh_jars_huongthu_edt.setText(jarAmountList.get(3) + "%");

            }
        });

        qlh_jars_huongthu_imgbtn_plus.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN -> {
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 100);
                    }
                    case MotionEvent.ACTION_UP -> {
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                    }
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    addPorpotion(3 ,qlh_jars_huongthu_edt,1);
                    mHandler.postDelayed(this, 100);
                }
            };
        });

        qlh_jars_huongthu_imgbtn_minus.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN -> {
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 100);
                    }
                    case MotionEvent.ACTION_UP -> {
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                    }
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    addPorpotion(3 ,qlh_jars_huongthu_edt,-1);
                    mHandler.postDelayed(this, 100);
                }
            };
        });




        //Dau tu =================================  4
        qlh_jars_dautu_imgbtn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jarAmountList.set(4,jarAmountList.get(4)+5);
                qlh_jars_dautu_edt.setText(jarAmountList.get(4) + "%");

            }
        });

        qlh_jars_dautu_imgbtn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jarAmountList.set(4,jarAmountList.get(4)-5);
                qlh_jars_dautu_edt.setText(jarAmountList.get(4) + "%");

            }
        });

        qlh_jars_dautu_imgbtn_plus.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN -> {
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 100);
                    }
                    case MotionEvent.ACTION_UP -> {
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                    }
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    addPorpotion(4 ,qlh_jars_dautu_edt,1);
                    mHandler.postDelayed(this, 100);
                }
            };
        });

        qlh_jars_dautu_imgbtn_minus.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN -> {
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 100);
                    }
                    case MotionEvent.ACTION_UP -> {
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                    }
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    addPorpotion(4 ,qlh_jars_dautu_edt,-1);
                    mHandler.postDelayed(this, 100);
                }
            };
        });


        //Thien tam =================================   5
        qlh_jars_thientam_imgbtn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jarAmountList.set(5,jarAmountList.get(5)+5);
                qlh_jars_thientam_edt.setText(jarAmountList.get(5) + "%");

            }
        });

        qlh_jars_thientam_imgbtn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jarAmountList.set(5,jarAmountList.get(5)-5);
                qlh_jars_thientam_edt.setText(jarAmountList.get(5) + "%");

            }
        });

        qlh_jars_thientam_imgbtn_plus.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN -> {
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 100);
                    }
                    case MotionEvent.ACTION_UP -> {
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                    }
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    addPorpotion(5 ,qlh_jars_thientam_edt,1);
                    mHandler.postDelayed(this, 100);
                }
            };
        });

        qlh_jars_thientam_imgbtn_minus.setOnTouchListener(new View.OnTouchListener() {
            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN -> {
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 100);
                    }
                    case MotionEvent.ACTION_UP -> {
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                    }
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    addPorpotion(5 ,qlh_jars_thientam_edt,-1);
                    mHandler.postDelayed(this, 100);
                }
            };
        });


    }

    @SuppressLint("SetTextI18n")
    private void addPorpotion(int position, EditText porpotionText, int value){
        if (value > 0){
            if (jarAmountList.get(position) + value <= 100){
                jarAmountList.set(position, (int) (jarAmountList.get(position) + value));
            }
        }else {
            if (jarAmountList.get(position) + value >= 0){
                jarAmountList.set(position, (int) (jarAmountList.get(position) + value));
            }
        }

        porpotionText.setText(String.valueOf(jarAmountList.get(position)));
        int total = 0;
        for (int i : jarAmountList) {
            total += i;
        }

    }


    public void updateJar(){
        Integer f =0;
        for (Integer v: jarAmountList) {
            f+=v;
        }
        if (f==100){
            for (int i=0; i< JarsAList.size(); i++){
                Jars holder = JarsAList.get(i);
                Jars updateJar = new Jars(holder.getId(),holder.getJarAmount(),holder.getJarBalance(),holder.getJarName(), holder.getOwner_id());
                updateJar.setJarAmount(jarAmountList.get(i));
                MongoDB.getInstance().updateJar(updateJar);

            }
            finish();
        }else {
            Toast.makeText(getApplicationContext(), "Chia tỉ lệ phải cộng lại = 100 chứ? Bạn có vấn đề à!?" + f.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}