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
 * Use the {@link QLtheoThang_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QLtheoThang_Fragment extends Fragment {
    BarChart mChart;
    View view;
    Button btnDatePikerWeek;
    private int mYear, mMonth, mDay;

    Calendar currentDate = Calendar.getInstance();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QLtheoThang_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QLtheoTuan_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QLtheoThang_Fragment newInstance(String param1, String param2) {
        QLtheoThang_Fragment fragment = new QLtheoThang_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        //Control
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
        ArrayList<Double> getValueOutCome = MongoDB.getInstance().getDataOutComeInWeek(currentDate);
        ArrayList<Double> getValueInCome = MongoDB.getInstance().getDataInComeInWeek(currentDate);
        mChart = (BarChart) view.findViewById(R.id.mChart);
        mChart.setDrawBarShadow(false);
        mChart.getDescription().setEnabled(false);
        mChart.setPinchZoom(true);
        mChart.setDrawGridBackground(true);
        mChart.setTouchEnabled(true);
        // empty labels so that the names are spread evenly
        String[] labels = {"", "Tuần1", "Tuần2", "Tuần3", "Tuần4", ""};
        XAxis xAxis = mChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setAxisMinimum(1f);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(0);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(2);
        leftAxis.setLabelCount(8, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);

        double[] valIncome = new double[getValueInCome.size()];
        double[] valOutCome =  new double[getValueOutCome.size()];
        for (int i = 0; i < getValueInCome.size(); i++) {
            double cal1 = getValueInCome.get(i);
            double cal2 = getValueOutCome.get(i);
            valIncome[i] = cal1;
            valOutCome[i] = cal2;
        }

        ArrayList<BarEntry> barOne = new ArrayList<>();
        ArrayList<BarEntry> barTwo = new ArrayList<>();
        ;
        for (int i = 0; i < valIncome.length; i++) {
            if (valOutCome[i] !=0) {
                barOne.add(new BarEntry(i, (float) valIncome[i]));
                barTwo.add(new BarEntry(i, (float) valOutCome[i]));
            }

        }

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
        // so that the entire chart is shown when scrolled from right to left
        xAxis.setAxisMaximum(labels.length - 1.1f);

        data.setValueTextColor(Color.parseColor("#FFFFFFFF"));
        data.setValueTextSize(10);

        for (int i = 0; i< leftAxis.mEntries.length; i++){
            leftAxis.mEntries[i] = Math.round(leftAxis.mEntries[i]);
        }
        leftAxis.setValueFormatter(new ChartCurrencyFormatter());
        mChart.setData(data);

        mChart.setScaleEnabled(false);
        mChart.setDrawValueAboveBar(false);
        mChart.setVisibleXRangeMaximum(6f);
        mChart.setHighlightFullBarEnabled(true);
        mChart.groupBars(1f, groupSpace, barSpace);
        mChart.animateXY(1000, 1000);
        mChart.getLegend().setEnabled(false);
        mChart.setHighlightPerTapEnabled(true);
        mChart.setTouchEnabled(true);
        CustomChartView mv = new CustomChartView (getContext(), R.layout.chart_detail_text);
        mChart.setMarkerView(mv);

        mChart.invalidate();

    }
}