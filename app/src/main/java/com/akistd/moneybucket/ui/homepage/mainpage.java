package com.akistd.moneybucket.ui.homepage;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.akistd.moneybucket.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mainpage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mainpage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public mainpage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mainpage.
     */
    // TODO: Rename and change types and number of parameters
    public static mainpage newInstance(String param1, String param2) {
        mainpage fragment = new mainpage();
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
    ListView jars_list_listview;
    ArrayList<CJarlist> arrayList;
    Cjarlist_listAdapter cjarlistListAdapter;

    BarChart moneyFlowChart;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mainpage, container, false);


        //-----------------------------------------------------------------------------------
        jars_list_listview = (ListView) view.findViewById(R.id.jars_list_listview);
        arrayList = CJarlist.jarlist();
        cjarlistListAdapter = new Cjarlist_listAdapter(getActivity(), R.layout.jarlist_layout_mainpage, arrayList);
        jars_list_listview.setAdapter(cjarlistListAdapter);

        //-------------------------------------------------------------------------------------
        dispMoneyFlowBarchart();

        return view;
    }

    public void dispMoneyFlowBarchart(){
        moneyFlowChart = view.findViewById(R.id.moneyFlow_barchart);

        moneyFlowChart.setDrawBarShadow(false);
        moneyFlowChart.getDescription().setEnabled(false);
        moneyFlowChart.setPinchZoom(false);
        moneyFlowChart.setDrawGridBackground(true);
        // empty labels so that the names are spread evenly
        String[] days = {"", "Mon", "Tues", "Weds", "Thu", "Fri", "Sat", "Sun", ""};
        XAxis xAxis = moneyFlowChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.BLACK);
        xAxis.setAxisMinimum(1f);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(days));
        YAxis leftAxis = moneyFlowChart.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.BLACK);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(2);
        leftAxis.setLabelCount(8, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        //mô tả description 2 cột
        moneyFlowChart.getAxisRight().setEnabled(true);
        moneyFlowChart.getLegend().setEnabled(true);

        float[] valOne = {900, 0, 400, 200, 800, 700, 0}; //thu
        float[] valTwo = {1200, 500, 400, 30, 200, 600, 1200};  //chi

        ArrayList<BarEntry> barOne = new ArrayList<>();
        ArrayList<BarEntry> barTwo = new ArrayList<>();
        ;
        for (int i = 0; i < valOne.length; i++) {
            barOne.add(new BarEntry(i, valOne[i]));
            barTwo.add(new BarEntry(i, valTwo[i]));

        }

        BarDataSet set1 = new BarDataSet(barOne, "Thu");
        set1.setColor(Color.GREEN);
        BarDataSet set2 = new BarDataSet(barTwo, "Chi");
        set2.setColor(Color.RED);


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
        xAxis.setAxisMaximum(days.length - 1.1f);
        moneyFlowChart.setData(data);
        moneyFlowChart.setScaleEnabled(false);
        moneyFlowChart.setVisibleXRangeMaximum(6f);
        moneyFlowChart.groupBars(1f, groupSpace, barSpace);
        moneyFlowChart.invalidate();

    }
}