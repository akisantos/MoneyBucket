package com.akistd.moneybucket.ui.baocaochithu;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.data.MongoDB;
import com.akistd.moneybucket.util.ChartCurrencyFormatter;
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
    View view;
    private int mYear, mMonth, mDay;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bao_cao_chi_thu_,container,false);
        btnDatePikerWeek = view.findViewById(R.id.btnDatePikerWeek);
        //Event

        currentDate.setTimeZone(TimeZone.getTimeZone("UTC"));
        currentDate.set(Calendar.DAY_OF_MONTH,currentDate.getActualMinimum(Calendar.DAY_OF_MONTH));
        mYear = currentDate.get(Calendar.YEAR);
        mMonth = currentDate.get(Calendar.MONTH);
        btnDatePikerWeek.setText(String.format("Tháng %s/%s", mMonth+1, mYear));
        GroupBarChart(currentDate);
        btnDatePikerWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date

                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {
                        btnDatePikerWeek.setText(String.format("Tháng %s/%s", selectedMonth +1, selectedYear));
                        GroupBarChart(currentDate);
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
    public void GroupBarChart(Calendar time){
        ArrayList<Double> getValueOutCome = MongoDB.getInstance().getDataOutComeInMonth(time);
        ArrayList<Double> getValueInCome = MongoDB.getInstance().getDataInComeInMonth(time);
        mChart2 = (BarChart) view.findViewById(R.id.mChart);
        mChart2.setDrawBarShadow(false);
        mChart2.getDescription().setEnabled(false);
        mChart2.setPinchZoom(false);
        mChart2.setDrawGridBackground(true);
        // empty labels so that the names are spread evenly
        String[] labels = {"", "T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12", ""};
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


        double[] valIncome = new double[getValueOutCome.size()];
        double[] valOutCome =  new double[getValueOutCome.size()];
        for (int i = 0; i < getValueOutCome.size()-1; i++) {
            double cal1 = getValueInCome.get(i+1);
            double cal2 = getValueOutCome.get(i+1);
            valIncome[i] = cal1;
            valOutCome[i] = cal2;
        }
        ArrayList<BarEntry> barOne = new ArrayList<>();
        ArrayList<BarEntry> barTwo = new ArrayList<>();

        for (int i = 0; i < valOutCome.length; i++) {
            barOne.add(new BarEntry((float) i, (float) valIncome[i]));
            barTwo.add(new BarEntry((float) i, (float) valOutCome[i]));

        }

        BarDataSet set1 = new BarDataSet(barOne, "barOne");
        set1.setColor(Color.parseColor("#2ecc71"));
        BarDataSet set2 = new BarDataSet(barTwo, "barTwo");
        set2.setColor(Color.parseColor("#e74c3c"));


        set1.setHighlightEnabled(false);
        set2.setHighlightEnabled(false);

        set1.setDrawValues(false);
        set2.setDrawValues(false);

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);

        BarData data = new BarData(dataSets);
        float groupSpace = 0.4f;
        float barSpace = 0f;
        float barWidth = 0.3f;
        // (barSpace + barWidth) * 2 + groupSpace = 1
        data.setBarWidth(barWidth);
        // so that the entire chart is shown when scrolled from right to left
        xAxis.setAxisMaximum(labels.length - 1.1f);
        for (int i = 0; i< leftAxis.mEntries.length; i++){
            leftAxis.mEntries[i] = Math.round(leftAxis.mEntries[i]);
        }
        leftAxis.setValueFormatter(new ChartCurrencyFormatter());

        mChart2.setData(data);
        mChart2.setScaleEnabled(false);
        mChart2.setVisibleXRangeMaximum(6f);
        mChart2.groupBars(1f, groupSpace, barSpace);
        mChart2.invalidate();

    }
}