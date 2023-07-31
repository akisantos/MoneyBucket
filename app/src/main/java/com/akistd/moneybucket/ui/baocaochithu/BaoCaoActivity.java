package com.akistd.moneybucket.ui.baocaochithu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.data.MongoDB;
import com.akistd.moneybucket.data.Transaction;
import com.akistd.moneybucket.util.UtilConverter;

import java.util.ArrayList;
import java.util.Calendar;

public class BaoCaoActivity extends AppCompatActivity {
    Button btn_tuan,btn_thang;
    ArrayList<ItemHu_bcct> itemHu_bccts;
    AdapterHu_bcct adapterHu_bcct;
    Spinner spinnerhu;
    ImageButton backButton;
    TextView tv_soDu,tv_tieuHao;
    Button btnDatePickerReport;
    private int mYear, mMonth, mDay;
    Calendar currentDate = Calendar.getInstance();;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_cao);
        addControl();
        addEnvent();
        loaiHu();

    }
    private void addControl(){
        btn_tuan = (Button) findViewById(R.id.btn_tuan);
        btn_thang = (Button) findViewById(R.id.btn_thang);
        tv_tieuHao = (TextView) findViewById(R.id.tv_tieuHao);
        tv_soDu = (TextView) findViewById(R.id.tv_soDu);
//        btnDatePickerReport = (Button) findViewById(R.id.btnDatePickerReport);
        spinnerhu = (Spinner)findViewById(R.id.spinnerhu);
        backButton = (ImageButton) findViewById(R.id.imgBtnOut);
    }
    private void addEnvent(){
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
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        btnDatePickerReport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Get Current Date
//                mYear = currentDate.get(Calendar.YEAR);
//                mMonth = currentDate.get(Calendar.MONTH);
//                mDay = currentDate.get(Calendar.DAY_OF_MONTH);
//
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(BaoCaoActivity.this,
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//
//                                btnDatePickerReport.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
//                                currentDate.set(year,monthOfYear,dayOfMonth);
//                                Log.v("getdat", "Start!! "+String.valueOf(currentDate.getTime()));
//                                Value(currentDate);
//                                getDataOutComeInWeek(currentDate);
//                                getDataInComeInWeek(currentDate);
//                            }
//
//                        }, mYear, mMonth, mDay);
//
//
//                datePickerDialog.show();
//            }
//        });
    }
//    private void loadSoDu(){
//        ArrayList<Jars> jars = MongoDB.getInstance().getAllJars();
//        Double sodu= Double.valueOf(0d);
//        for (Jars jar: jars) {
//            sodu += jar.getJarBalance();
//        }
//
//
//        ArrayList<Transaction> latestIncome = MongoDB.getInstance().getAllSortedIncomeTransaction();
//        Double totalIncome= Double.valueOf(0d);
//        if (latestIncome.size()>0){
//
//            for (int i=0; i<5; i++){
//                totalIncome += latestIncome.get(i).getTransAmount();
//            }
//        }
//        ArrayList<Transaction> thisMonthOutcome = MongoDB.getInstance().getThisMonthSortedOutcomeTransaction();
//        Double totalOutcome= Double.valueOf(0d);
//        for (Transaction tr: thisMonthOutcome) {
//            totalOutcome += tr.getTransAmount() *-1;
//
//        }
//
//        //Công thức - toàn bộ tiền trong hũ - tiền đã tiêu trong tháng này.
////        Double percentRaw = 100 - (totalOutcome/ totalIncome)*100;
////        if (percentRaw<0 || percentRaw.isInfinite() || percentRaw.isNaN()){
////            main_balance_process_numb.setText("0%");
////            main_balance_processBar.setProgress(0);
////        }else {
////            main_balance_process_numb.setText(String.format("%.0f",percentRaw) + "%");
////            main_balance_processBar.setProgress(Integer.parseInt(String.format("%.0f",percentRaw)));
////        }
//
//        tv_soDu.setText(UtilConverter.getInstance().vndCurrencyConverter(sodu));
//
//
//    }
//    private void loadTieuHao(){
//        ArrayList<Transaction> thisMonthOutcome = MongoDB.getInstance().getThisMonthSortedOutcomeTransaction();
//        Double totalOutcome= Double.valueOf(0d);
//        for (Transaction tr: thisMonthOutcome) {
//            totalOutcome += tr.getTransAmount() *-1;
//        }
//
//        tv_tieuHao.setText(UtilConverter.getInstance().vndCurrencyConverter(totalOutcome));
//    }
    private void getDataOutComeInWeek(Calendar time){
        ArrayList<Transaction> thisWeekOutcome = MongoDB.getInstance().getWeekSortedOutcomeTransaction(time);
        Double totalOutcome= Double.valueOf(0d);
        for (Transaction tr: thisWeekOutcome) {
            totalOutcome += tr.getTransAmount() *-1;
        }
        Log.v("week outcome", "Start!! "+String.valueOf(time.getTime()));
        tv_tieuHao.setText(UtilConverter.getInstance().vndCurrencyConverter(totalOutcome));
    }
    private void Value(Calendar time){

    }

    private void getDataInComeInWeek(Calendar time){
        ArrayList<Transaction> thisWeekOutcome = MongoDB.getInstance().getWeekSortedIncomeTransaction(time);
        Double totalIncome= Double.valueOf(0d);

        for (Transaction tr: thisWeekOutcome) {
            totalIncome += tr.getTransAmount();
            Log.v("week outcome", "TOTALL IC!! "+String.valueOf(totalIncome));
        }

        tv_soDu.setText(UtilConverter.getInstance().vndCurrencyConverter(totalIncome));
    }

    public void onResume() {
        super.onResume();

    }

    private void loaiHu(){
        int[] img =new int[]{R.drawable.hu2,R.drawable.hu5,R.drawable.hu1,R.drawable.hu6,R.drawable.hu2,R.drawable.hu4,R.drawable.hu3,};
        String[] namejar = new String[]{"Tất cả","Thiết yếu","Giáo dục","Tiết kiệm","Hưởng thụ","Đầu tư","Thiện tâm"};

        itemHu_bccts= ItemHu_bcct.initSex(img,namejar);
        adapterHu_bcct =new AdapterHu_bcct(this.getLayoutInflater(),itemHu_bccts,R.layout.baocaochitieu_jar_row);
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