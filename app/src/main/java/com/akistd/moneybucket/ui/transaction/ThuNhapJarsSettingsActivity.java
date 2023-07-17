package com.akistd.moneybucket.ui.transaction;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class ThuNhapJarsSettingsActivity extends AppCompatActivity {

    ArrayList<Jars> data = new ArrayList<>();
    ListView jars_list_listview;
    Button saveBtn;
    TextView totalText;
    JarsThuNhapActivityApdater apdater;
    LinearLayout clickToFinish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thu_nhap_jars_settings);


        saveBtn = findViewById(R.id.saveBtn);
        totalText = findViewById(R.id.totalText);
        clickToFinish = findViewById(R.id.clickToFinish);

        jars_list_listview = findViewById(R.id.jars_list_listview);
        data = MongoDB.getInstance().getAllJars();
        apdater = new JarsThuNhapActivityApdater(getApplicationContext(), data);
        jars_list_listview.setAdapter(apdater);

        jars_list_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ThuNhapJarsSettingsActivity.this, position, Toast.LENGTH_SHORT).show();
                Integer total=0;
                for (Integer i: apdater.jarsAmountList) {
                    total +=i;
                }
                totalText.setText(total.toString() + "%");
            }
        });

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
        for (int i=0; i< data.size(); i++){
            Jars holder = data.get(i);

            Jars updateJar = new Jars(holder.getId(),holder.getJarAmount(),holder.getJarBalance(),holder.getJarName(), holder.getOwner_id());
            updateJar.setJarAmount(apdater.jarsAmountList.get(i));
            MongoDB.getInstance().updateJar(updateJar);
        }

        finish();
    }


}