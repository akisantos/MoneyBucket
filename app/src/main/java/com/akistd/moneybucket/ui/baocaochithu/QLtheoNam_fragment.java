package com.akistd.moneybucket.ui.baocaochithu;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.data.Jars;
import com.akistd.moneybucket.data.MongoDB;
import com.akistd.moneybucket.data.Transaction;
import com.akistd.moneybucket.ui.transaction.JarsChiTieuFragSpinnerAdapter;
import com.akistd.moneybucket.util.ChartCurrencyFormatter;
import com.akistd.moneybucket.util.UtilConverter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QLtheoNam_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QLtheoNam_fragment extends Fragment {
    BarChart mChart2;
    Button btnDatePikerWeek;
    TextView tv_soDu,tv_tieuHao;
    View view;
    JarsChiTieuFragSpinnerAdapter spinnerAdapter;
    Spinner spinnerhu;
    private int mYear, mMonth, mDay,selection=0;
    ArrayList<Jars> jarsList = MongoDB.getInstance().getAllJars();

    Calendar currentDate = Calendar.getInstance();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QLtheoNam_fragment() {
        // Required empty public constructor
    }


    public static QLtheoNam_fragment newInstance() {
        QLtheoNam_fragment fragment = new QLtheoNam_fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    double[] IncomeMonth={};
    double[] OutcomeMonth={};
    private  void getDataBaseOnJarWeek(int position) {
        ArrayList<Double> getValueOutCome = MongoDB.getInstance().getDataOutComeInMonth(currentDate);
        ArrayList<Double> getValueInCome = MongoDB.getInstance().getDataInComeInMonth(currentDate);

        ArrayList<Double> incomeInMonthList = MongoDB.getInstance().getDataOfJarInComeInMonth2(currentDate,position);
        ArrayList<Double> outcomeInMonthList = MongoDB.getInstance().getDataOfJarOutComeInMonth2(currentDate,position);

        ArrayList<Transaction> allInComeYearTransaction = MongoDB.getInstance().getAllIncomeTransactionInYear(currentDate);
        ArrayList<Transaction> allOutComeYearTransaction = MongoDB.getInstance().getAllOutcomeTransactionInYear(currentDate);

        IncomeMonth = new double[incomeInMonthList.size()];
        for (int i = 0; i < incomeInMonthList.size(); i++) {
            double cal1 = incomeInMonthList.get(i);
            IncomeMonth[i] = cal1;
        }
        for (int i = 0; i < IncomeMonth.length; i++) {
            Log.v("dataaaaaaaIn", "!! " + IncomeMonth[i]);
        }

        OutcomeMonth = new double[outcomeInMonthList.size()];
        for (int i = 0; i < outcomeInMonthList.size(); i++) {
            double cal2 = outcomeInMonthList.get(i);
            OutcomeMonth[i] = cal2;
        }
        for (int i = 0; i < OutcomeMonth.length; i++) {
            Log.v("dataaaaaaaOut", "!! " + OutcomeMonth[i]);
        }

        GroupBarChart(currentDate,position,getValueInCome, getValueOutCome,incomeInMonthList, outcomeInMonthList);


        loadSoDu(position, allInComeYearTransaction, allOutComeYearTransaction,incomeInMonthList, outcomeInMonthList);
    }

    private void jarListsSpinnerEvents(){
        Jars allJars = new Jars();
        allJars.setJarName("Tất cả hũ");
        allJars.setJarBalance(0.0);
        jarsList.add(0,allJars);
        spinnerAdapter = new JarsChiTieuFragSpinnerAdapter(getContext(), jarsList);
        spinnerAdapter.setDropDownViewResource(R.layout.jarlist_chitieu_frag_dropdown);
        spinnerhu.setAdapter(spinnerAdapter);

        spinnerhu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selection = position;
                getDataBaseOnJarWeek(selection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bao_cao_chi_thu_,container,false);
        btnDatePikerWeek = view.findViewById(R.id.btnDatePikerWeek);
        spinnerhu = (Spinner)view.findViewById(R.id.spinnerhu);
        tv_soDu = (TextView) view.findViewById(R.id.tv_soDu);
        tv_tieuHao = (TextView) view.findViewById(R.id.tv_tieuHao);

        //Event
        jarListsSpinnerEvents();
        currentDate.setTimeZone(TimeZone.getTimeZone("UTC"));
        currentDate.set(Calendar.DAY_OF_MONTH,currentDate.getActualMinimum(Calendar.DAY_OF_MONTH));
        mYear = currentDate.get(Calendar.YEAR);
        mMonth = currentDate.get(Calendar.MONTH);
        btnDatePikerWeek.setText(String.format("Tháng %s/%s", mMonth+1, mYear));
        getDataBaseOnJarWeek(selection);
        btnDatePikerWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date

                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {
                        btnDatePikerWeek.setText(String.format("Tháng %s/%s", selectedMonth +1, selectedYear));
                        getDataBaseOnJarWeek(selection);

                    }
                }, mYear,mMonth);

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
        return view;
    }
    private void loadSoDu(int position,ArrayList<Transaction> transactionsSoDutong, ArrayList<Transaction> transactionsTieuHaotong,ArrayList<Double> transactionsSoDu,ArrayList<Double> transactionsTieuHao ){
        Double sodu;
        Double tieuhao= Double.valueOf(0d);
        if(position == 0){
             sodu= Double.valueOf(0d);
            for (Transaction tr: transactionsSoDutong) {
                sodu += tr.getTransAmount();
            }

            for (Transaction tr: transactionsTieuHaotong) {
                tieuhao += tr.getTransAmount()*-1;
            }

        }else{
            sodu= Double.valueOf(0d);
            tieuhao= Double.valueOf(0d);
            for (Double tr: transactionsSoDu) {
                sodu += tr.doubleValue();
            }
            for (Double tr: transactionsTieuHao) {
                tieuhao += tr.doubleValue()*-1;
            }
        }
        tv_soDu.setText(UtilConverter.getInstance().vndCurrencyConverter(sodu));
        tv_tieuHao.setText(UtilConverter.getInstance().vndCurrencyConverter(tieuhao));
    }
    public void GroupBarChart(Calendar time, int position,ArrayList<Double> getValueInCome,ArrayList<Double> getValueOutCome,ArrayList<Double> getValueInComeBaseOnJar,  ArrayList<Double> getValueIOutComeBaseOnJar){

        mChart2 = (BarChart) view.findViewById(R.id.mChart);
        mChart2.setDrawBarShadow(false);
        mChart2.getDescription().setEnabled(false);
        mChart2.setPinchZoom(true);
        mChart2.setDrawGridBackground(true);
        mChart2.setTouchEnabled(true);
        // empty labels so that the names are spread evenly
        String[] labels = {"", "Th1", "Th2", "Th3", "Th4", "Th5", "Th6", "Th7", "Th8", "Th9", "Th10", "Th11", "Th12", ""};
        XAxis xAxis = mChart2.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setAxisMinimum(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));



        YAxis leftAxis = mChart2.getAxisLeft();
        leftAxis.setAxisMinimum(0);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(2);
        leftAxis.setLabelCount(8, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        mChart2.getAxisRight().setEnabled(false);
        mChart2.getLegend().setEnabled(false);


        double[] valIncome = new double[getValueInCome.size()];
        double[] valOutCome =  new double[getValueOutCome.size()];
        ArrayList<BarEntry> barOne = new ArrayList<>();
        ArrayList<BarEntry> barTwo = new ArrayList<>();
        if(selection == 0){
            for (int i = 0; i < getValueInCome.size(); i++) {
                double cal1 = getValueInCome.get(i);
                double cal2 = getValueOutCome.get(i);
                valIncome[i] = cal1;
                valOutCome[i] = cal2;
            }
            for (int i = 1; i < valIncome.length; i++) {

                barOne.add(new BarEntry(i, (float) valIncome[i]));
                barTwo.add(new BarEntry(i, (float) valOutCome[i]));
            }
        }else {
            for (int i = 1; i < getValueInCome.size(); i++) {
                double cal1 = getValueInComeBaseOnJar.get(i);
                double cal2 = getValueIOutComeBaseOnJar.get(i);
                IncomeMonth[i] = cal1;
                OutcomeMonth[i] = cal2;
            }
            for (int i = 0; i < IncomeMonth.length; i++) {
                barOne.add(new BarEntry(i, (float) IncomeMonth[i]));
                barTwo.add(new BarEntry(i, (float) OutcomeMonth[i]));

            }
        }

        if (barOne.size()>0){
            BarDataSet set1 = new BarDataSet(barOne, "barOne");
            set1.setColor(Color.parseColor("#2ecc71"));
            set1.setDrawValues(true);
            set1.setValueFormatter(new ChartCurrencyFormatter());
            BarDataSet set2 = new BarDataSet(barTwo, "barTwo");
            set2.setColor(Color.parseColor("#e74c3c"));
            set2.setDrawValues(true);
            set2.setValueFormatter(new ChartCurrencyFormatter());

            set1.setHighlightEnabled(true);
            set2.setHighlightEnabled(true);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            dataSets.add(set2);

            BarData data = new BarData(dataSets);
            float groupSpace = 0.16f;
            float barSpace = 0.02f;
            float barWidth = 0.4f;
            // (barSpace + barWidth) * 2 + groupSpace = 1
            data.setBarWidth(barWidth);
            data.setValueTextColor(Color.parseColor("#FFFFFFFF"));
            data.setValueTextSize(10);
            // so that the entire chart is shown when scrolled from right to left
            xAxis.setAxisMaximum(labels.length - 1.1f);
            for (int i = 0; i< leftAxis.mEntries.length; i++){
                leftAxis.mEntries[i] = Math.round(leftAxis.mEntries[i]);
            }
            leftAxis.setValueFormatter(new ChartCurrencyFormatter());

            mChart2.setData(data);
            mChart2.setScaleEnabled(false);
            mChart2.setDrawValueAboveBar(false);
            mChart2.setVisibleXRangeMaximum(6f);
            mChart2.setHighlightFullBarEnabled(true);
            mChart2.groupBars(1f, groupSpace, barSpace);
            mChart2.animateXY(1000, 1000);
            mChart2.getLegend().setEnabled(false);
            mChart2.setHighlightPerTapEnabled(true);
            mChart2.setTouchEnabled(true);
            CustomChartView mv = new CustomChartView (getContext(), R.layout.chart_detail_text);
            mChart2.setMarkerView(mv);

            mChart2.invalidate();
        }

        Log.v("AKKIII", String.valueOf(barOne.size()));
    }
}