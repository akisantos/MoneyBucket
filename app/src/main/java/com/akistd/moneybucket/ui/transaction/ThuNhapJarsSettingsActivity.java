package com.akistd.moneybucket.ui.transaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.data.Jars;
import com.akistd.moneybucket.data.MongoDB;

import java.util.ArrayList;
import java.util.List;

public class ThuNhapJarsSettingsActivity extends AppCompatActivity {

    ArrayList<Jars> data = new ArrayList<>();
    private List<Integer> jarsAmountList = new ArrayList<>();
    ListView jars_list_listview;
    Button saveBtn;
    TextView totalText;
    JarsThuNhapActivityApdater apdater;
    LinearLayout clickToFinish;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thu_nhap_jars_settings);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        saveBtn = findViewById(R.id.saveBtn);
        totalText = findViewById(R.id.totalText);
        clickToFinish = findViewById(R.id.clickToFinish);

        jars_list_listview = findViewById(R.id.jars_list_listview);
        data = MongoDB.getInstance().getAllJars();
        apdater = new JarsThuNhapActivityApdater(getApplicationContext(), data,totalText);
        jars_list_listview.setAdapter(apdater);


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateJarAmount();

            }
        });

        Integer total=0;
        for (Integer i: apdater.jarsAmountList) {
            total +=i;
        }
        totalText.setText(total.toString() + "%");

        clickToFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void updateJarAmount(){
        data = apdater.data;
        jarsAmountList = apdater.jarsAmountList;

        Integer f =0;
        for (Integer v: jarsAmountList) {
            f+=v;
        }


        if (f==100){
            for (int i=0; i< data.size(); i++){
                Jars holder = data.get(i);
                Jars updateJar = new Jars(holder.getId(),holder.getJarAmount(),holder.getJarBalance(),holder.getJarName(), holder.getOwner_id());
                updateJar.setJarAmount(apdater.jarsAmountList.get(i));
                MongoDB.getInstance().updateJar(updateJar);
            }
            finish();
        }else {
            Toast.makeText(getApplicationContext(), "Chia tỉ lệ phải cộng lại = 100 chứ? Bạn có vấn đề à!?" + f.toString(), Toast.LENGTH_SHORT).show();
        }

    }


}