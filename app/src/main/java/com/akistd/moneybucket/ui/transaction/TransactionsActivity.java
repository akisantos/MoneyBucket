package com.akistd.moneybucket.ui.transaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.akistd.moneybucket.R;
import com.google.android.material.tabs.TabLayout;

public class TransactionsActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageButton backButton;
    Button saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);


        addControls();
        addEvents();
        openTab(this.getIntent().getIntExtra("tabIndex",0));

    }

    private void addControls() {

        backButton = (ImageButton) findViewById(R.id.imgBtnOut);

        tabLayoutSetup();
    }

    private void addEvents(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(TransactionsActivity.this, MainActivity.class);
                startActivity(intent);*/
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void tabLayoutSetup(){
        tabLayout= (TabLayout) findViewById(R.id.tabLayout);
        viewPager=(ViewPager)findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Thu nhập"));
        tabLayout.addTab(tabLayout.newTab().setText("Chi tiêu"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final TransactionTabLayoutAdapter adapter = new TransactionTabLayoutAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }

    private void openTab(int index){
        TabLayout.Tab tab = tabLayout.getTabAt(index);
        tab.select();
    }
}