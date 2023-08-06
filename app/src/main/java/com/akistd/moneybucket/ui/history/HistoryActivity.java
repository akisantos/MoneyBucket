package com.akistd.moneybucket.ui.history;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.data.Jars;
import com.akistd.moneybucket.data.MongoDB;
import com.akistd.moneybucket.data.Transaction;
import com.akistd.moneybucket.ui.transaction.JarsChiTieuFragSpinnerAdapter;
import com.whiteelephant.monthpicker.MonthPickerDialog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

public class HistoryActivity extends AppCompatActivity {


    ImageButton backButton;
    Button btnDatePicker;
    Spinner btnAllJam;
    ListView transactionListView;

    JarsChiTieuFragSpinnerAdapter spinnerAdapter;
    TransactionHistoryAdapter listViewAdapter;
    ArrayList<Jars> jarsList = MongoDB.getInstance().getAllJars();
    ArrayList<Transaction> transactionsList = new ArrayList<>();
    private int mYear, mMonth, selection = 0;
    Calendar currentDate = Calendar.getInstance();

    TextView transactionListView_emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        addControls();
        addEvents();

    }

    private void addControls() {

        backButton = (ImageButton) findViewById(R.id.imgBtnOut);
        btnDatePicker = findViewById(R.id.btnDatePicker);
        transactionListView = findViewById(R.id.transactionListView);
        btnAllJam = findViewById(R.id.btnAllJam);

        transactionListView_emptyView = findViewById(R.id.transactionListView_emptyView);
    }

    private void addEvents(){


        jarListsSpinnerEvents();
        transactionHistoryListViewHandler();
        currentDate.setTimeZone(TimeZone.getTimeZone("UTC"));
        currentDate.set(Calendar.DAY_OF_MONTH,currentDate.getActualMinimum(Calendar.DAY_OF_MONTH));
        mYear = currentDate.get(Calendar.YEAR);
        mMonth = currentDate.get(Calendar.MONTH);
        btnDatePicker.setText(String.format("Tháng %s/%s", mMonth+1, mYear));

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(HistoryActivity.this, new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {
                        btnDatePicker.setText(String.format("Tháng %s/%s", selectedMonth +1, selectedYear));
                        updateTransactionDataOnSpinerChanged(selection);
                    }
                }, mYear,mMonth+1);

                builder.setActivatedMonth(mMonth)
                        .setMinYear(1990)
                        .setActivatedYear(mYear)
                        .setTitle("Chọn tháng năm")
                        .setOnMonthChangedListener(new MonthPickerDialog.OnMonthChangedListener() {
                            @Override
                            public void onMonthChanged(int selectedMonth) {
                                mMonth = selectedMonth;
                                currentDate.set(mYear,mMonth,0);

                            }
                        })
                        .setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
                            @Override
                            public void onYearChanged(int year) {
                                mYear = year;
                                currentDate.set(mYear,mMonth,0);
                            }
                        })
                        .build().show();

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void updateTransactionDataOnSpinerChanged(int position){
        ArrayList<Transaction> dataList = loadTransactionHistoryOnDatechange(currentDate);
        ArrayList<Transaction> dataList2 = new ArrayList<>();
        if (dataList.size() > 0){
            for (Transaction t: dataList) {
                if (position == 0) {
                    dataList2.add(t);
                }else {
                    if (Objects.equals(jarsList.get(position).getId(), t.getJars().getId())) dataList2.add(t);
                }
            }
        }

        updateListViewData(dataList2);

    }

    private void updateListViewData(ArrayList<Transaction> transactionsList){
        listViewAdapter.dataList = transactionsList;
        listViewAdapter.notifyDataSetChanged();
    }

    private ArrayList<Transaction> loadTransactionHistoryOnDatechange(Calendar time){
        return MongoDB.getInstance().getThisMonthSortedTransaction(time);
    }

    private void jarListsSpinnerEvents(){
        Jars allJars = new Jars();
        allJars.setJarName("Tất cả hũ");
        allJars.setJarBalance(0.0);
        jarsList.add(0,allJars);
        spinnerAdapter = new JarsChiTieuFragSpinnerAdapter(this, jarsList);
        spinnerAdapter.setDropDownViewResource(R.layout.jarlist_chitieu_frag_dropdown);
        btnAllJam.setAdapter(spinnerAdapter);

        btnAllJam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateTransactionDataOnSpinerChanged(position);
                selection = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    private void transactionHistoryListViewHandler(){
        listViewAdapter = new TransactionHistoryAdapter(this,R.layout.transaction_history_row,transactionsList, jarsList);
        transactionListView.setAdapter(listViewAdapter);
        transactionListView.setEmptyView(transactionListView_emptyView);
    }
}