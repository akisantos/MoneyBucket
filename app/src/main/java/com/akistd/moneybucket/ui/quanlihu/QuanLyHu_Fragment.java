package com.akistd.moneybucket.ui.quanlihu;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.akistd.moneybucket.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuanLyHu_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuanLyHu_Fragment extends Fragment {
    View view;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public QuanLyHu_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuanLyHu_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static QuanLyHu_Fragment newInstance(String param1, String param2) {
        QuanLyHu_Fragment fragment = new QuanLyHu_Fragment();
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
        view = inflater.inflate(R.layout.fragment_quan_ly_hu_,container,false);
        PieChart pieChart = view.findViewById(R.id.pieChart_tyLeHu);
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(55,"Thiết yếu"));
        entries.add(new PieEntry(10,"Giáo dục"));
        entries.add(new PieEntry(20,"Tiết kiệm"));
        entries.add(new PieEntry(5,"Hưởng thụ"));
        entries.add(new PieEntry(5,"Đầu tư"));
        entries.add(new PieEntry(5,"Thiện tâm"));
        PieDataSet pieDataSet  = new PieDataSet(entries,"");
        pieDataSet.setValueTextSize(0f);
        pieDataSet.setColors(Color.parseColor("#0094FF"),Color.parseColor("#00FFE0"),Color.parseColor("#E84DE2"),Color.parseColor("#FFD615"),Color.parseColor("#FF1F5A"),Color.parseColor("#14FF00"),Color.parseColor("#14FF00"));
        PieData pieData  = new PieData(pieDataSet);
        pieChart.setData(pieData);

        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(100);
        pieChart.invalidate();
        return view;
    }
}