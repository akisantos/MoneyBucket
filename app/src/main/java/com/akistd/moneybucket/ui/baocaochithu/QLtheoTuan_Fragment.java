package com.akistd.moneybucket.ui.baocaochithu;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.data.MongoDB;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Calendar;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QLtheoTuan_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QLtheoTuan_Fragment extends Fragment {
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

    public QLtheoTuan_Fragment() {
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
    public static QLtheoTuan_Fragment newInstance(String param1, String param2) {
        QLtheoTuan_Fragment fragment = new QLtheoTuan_Fragment();
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
        btnDatePikerWeek.setOnClickListener(new View.OnClickListener() {
                        @Override
            public void onClick(View v) {
                // Get Current Date
                mYear = currentDate.get(Calendar.YEAR);
                mMonth = currentDate.get(Calendar.MONTH);
                mDay = currentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                btnDatePikerWeek.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                currentDate.set(year,monthOfYear,dayOfMonth);
                                Log.v("getdat", "Start!! "+String.valueOf(currentDate.getTime()));
                                GroupBarChart(currentDate);
                            }

                        }, mYear, mMonth, mDay);


                datePickerDialog.show();
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
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(true);
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
            barOne.add(new BarEntry(i, (float) valIncome[i]));
            barTwo.add(new BarEntry(i, (float) valOutCome[i]));

        }

        BarDataSet set1 = new BarDataSet(barOne, "barOne");
        set1.setColor(Color.GREEN);
        BarDataSet set2 = new BarDataSet(barTwo, "barTwo");
        set2.setColor(Color.RED);


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
        mChart.setData(data);
        mChart.setScaleEnabled(false);
        mChart.setVisibleXRangeMaximum(6f);
        mChart.groupBars(1f, groupSpace, barSpace);
        mChart.invalidate();

    }
}