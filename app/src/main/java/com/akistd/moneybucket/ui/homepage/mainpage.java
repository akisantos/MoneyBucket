package com.akistd.moneybucket.ui.homepage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.data.Jars;
import com.akistd.moneybucket.data.MongoDB;
import com.akistd.moneybucket.data.Transaction;
import com.akistd.moneybucket.ui.baocaochithu.BaoCaoActivity;
import com.akistd.moneybucket.ui.baocaochithu.CustomChartView;
import com.akistd.moneybucket.ui.history.HistoryActivity;
import com.akistd.moneybucket.ui.history.TransactionHistoryAdapter;
import com.akistd.moneybucket.ui.quanlihu.quanLyHu;
import com.akistd.moneybucket.ui.transaction.TransactionsActivity;
import com.akistd.moneybucket.util.ChartCurrencyFormatter;
import com.akistd.moneybucket.util.Constants;
import com.akistd.moneybucket.util.CustomPercentFormatter;
import com.akistd.moneybucket.util.UtilConverter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link mainpage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mainpage extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public mainpage() {
        // Required empty public constructor
    }

    AppCompatButton mainpage_btn_historySeemore, mainpage_btn_addIncome, mainpage_btn_addOutcome,mainpage_btn_moneyFlowSeemore, mainpage_btn_SeemorejarDistribution;

    TextView mainpage_welcomeText,mainpage_currentBalanceText,main_balance_process_numb;
    ProgressBar main_balance_processBar;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    ListView jars_list_listview, mainpage_listview_history;
    Cjarlist_listAdapter cjarlistListAdapter;
    ArrayList<Jars> jarsList;
    BarChart moneyFlowChart;
    Constants util = new Constants();
    TextView emptyView, jars_list_listview_emptyView;
    View view;
    Calendar currentDate = Calendar.getInstance();
    ArrayList<Transaction> data;
    TransactionHistoryAdapter listViewAdapter;

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

        //Lấy thông tin đăng nhập
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(util.getClientID()).requestEmail().build();
        gsc = GoogleSignIn.getClient(getActivity(),gso);
        jarsList = MongoDB.getInstance().getAllJars();
    }


    String username = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mainpage, container, false);
        addControls(view);
        addEvents();

        dispMoneyFlowBarchart();
        loadPiechart_mainpage();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (cjarlistListAdapter != null){
            cjarlistListAdapter.notifyDataSetChanged();
            listViewAdapter.notifyDataSetChanged();

        }

        loadSoDu();
        loadHistory();
        dispMoneyFlowBarchart();
        loadPiechart_mainpage();
    }

    private void addControls(View view){


        //HeaderCard
        mainpage_welcomeText = (TextView) view.findViewById(R.id.mainpage_welcomeText);
        mainpage_currentBalanceText = (TextView) view.findViewById(R.id.mainpage_currentBalanceText);
        main_balance_processBar = view.findViewById(R.id.main_balance_processBar);
        main_balance_process_numb = view.findViewById(R.id.main_balance_process_numb);
        mainpage_btn_moneyFlowSeemore = view.findViewById(R.id.mainpage_btn_moneyFlowSeemore);

        //Jars List (nếu đou chạm thì comment lại cho đúng nhe)
        jars_list_listview = (ListView) view.findViewById(R.id.jars_list_listview);
        jars_list_listview_emptyView = view.findViewById(R.id.jars_list_listview_emptyView);


        //Income,outcome btn
        mainpage_btn_addIncome = (AppCompatButton) view.findViewById(R.id.mainpage_btn_addIncome);
        mainpage_btn_addOutcome = (AppCompatButton) view.findViewById(R.id.mainpage_btn_addOutcome);

        //History
        mainpage_btn_historySeemore = (AppCompatButton) view.findViewById(R.id.mainpage_btn_historySeemore);
        mainpage_listview_history = view.findViewById(R.id.mainpage_listview_history);
        emptyView = (TextView)view.findViewById(R.id.emptyView);

        //co cau hu
        mainpage_btn_SeemorejarDistribution = (AppCompatButton) view.findViewById(R.id.mainpage_btn_SeemorejarDistribution);
    }

    private void addEvents(){

        //headerCard
        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(getContext());
        mainpage_welcomeText.setText("Xin chào, "+ acc.getDisplayName() + "!");
        mainpage_currentBalanceText.setText("Đang cập nhật...");
        loadSoDu();

        //JarsList

        cjarlistListAdapter = new Cjarlist_listAdapter(getContext(), R.layout.jarlist_layout_mainpage, jarsList);
        jars_list_listview.setAdapter(cjarlistListAdapter);
        jars_list_listview.setEmptyView(jars_list_listview_emptyView);


        //history events
        mainpage_btn_historySeemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(view.getContext(), HistoryActivity.class);
                view.getContext().startActivity(historyIntent);
            }
        });

        data = MongoDB.getInstance().getThisMonthSortedTransactionByNumber(5);
        jarsList = MongoDB.getInstance().getAllJars();
        listViewAdapter = new TransactionHistoryAdapter(getContext(),R.layout.transaction_history_row,data, jarsList);
        mainpage_listview_history.setEmptyView(emptyView);
        mainpage_listview_history.setAdapter(listViewAdapter);



        //Income, outcome events
        mainpage_btn_addIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transactionIntent = new Intent(view.getContext(), TransactionsActivity.class);
                transactionIntent.putExtra("tabIndex",0);
                view.getContext().startActivity(transactionIntent);
            }
        });

        mainpage_btn_addOutcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transactionIntent = new Intent(view.getContext(), TransactionsActivity.class);
                transactionIntent.putExtra("tabIndex",1);
                view.getContext().startActivity(transactionIntent);
            }
        });

        mainpage_btn_moneyFlowSeemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BaoCaoActivity.class);
                startActivity(intent);
            }
        });

        mainpage_btn_SeemorejarDistribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), quanLyHu.class);
                startActivity(intent);
            }
        });

    }



    private void loadSoDu(){
        ArrayList<Jars> jars = MongoDB.getInstance().getAllJars();
        Double sodu= Double.valueOf(0d);
        for (Jars jar: jars) {
            sodu += jar.getJarBalance();
        }


        ArrayList<Transaction> latestIncome = MongoDB.getInstance().getAllSortedIncomeTransaction();
        Date incomeTime = new Date();
        if (latestIncome.size()>0){
            for (int i=0; i<6; i++){
                incomeTime = latestIncome.get(i).getCreateAt();
            }
        }
        ArrayList<Transaction> thisMonthOutcome = MongoDB.getInstance().getThisMonthSortedOutcomeTransaction();
        Double totalOutcome= Double.valueOf(0d);

        for (Transaction tr: thisMonthOutcome) {
            if (tr.getCreateAt().after(incomeTime)) {
                totalOutcome += tr.getTransAmount() *-1;

            }
        }

        Log.v("Akii log THU", UtilConverter.getInstance().vndCurrencyConverter(sodu+totalOutcome));
        Log.v("AKKI LOGG CHI", UtilConverter.getInstance().vndCurrencyConverter(totalOutcome));
        //Từ thời điểm nạp lần nhất đến hiện tại đã dùng hết bao nhiêu tiền.
        Double percentRaw = 100 - (totalOutcome/ (sodu + totalOutcome))*100;
        if (percentRaw<0 || percentRaw.isInfinite() || percentRaw.isNaN()){
            main_balance_process_numb.setText("0%");
            main_balance_processBar.setProgress(0);
        }else {
            main_balance_process_numb.setText(String.format("%.0f",percentRaw) + "%");
            main_balance_processBar.setProgress(Integer.parseInt(String.format("%.0f",percentRaw)));
        }

        mainpage_currentBalanceText.setText(UtilConverter.getInstance().vndCurrencyConverter(sodu));


    }

    private void loadHistory(){
        data = MongoDB.getInstance().getFiveSortedTransactionByNumber();
        listViewAdapter = new TransactionHistoryAdapter(getContext(),R.layout.transaction_history_row,data, jarsList);
        mainpage_listview_history.setEmptyView(emptyView);
        mainpage_listview_history.setAdapter(listViewAdapter);
    }


    public void dispMoneyFlowBarchart(){
        currentDate.setTimeZone(TimeZone.getTimeZone("UTC"));
        currentDate.set(Calendar.DAY_OF_MONTH,currentDate.getActualMinimum(Calendar.DAY_OF_MONTH));
        ArrayList<Double> getValueOutCome = MongoDB.getInstance().getDataOutComeInWeek(currentDate);
        ArrayList<Double> getValueInCome = MongoDB.getInstance().getDataInComeInWeek(currentDate);
        moneyFlowChart = (BarChart) view.findViewById(R.id.moneyFlow_barchart);
        moneyFlowChart.setDrawBarShadow(false);
        moneyFlowChart.getDescription().setEnabled(false);
        moneyFlowChart.setPinchZoom(true);
        moneyFlowChart.setDrawGridBackground(true);
        moneyFlowChart.setTouchEnabled(true);
        // empty labels so that the names are spread evenly
        String[] labels = {"", "Tuần1", "Tuần2", "Tuần3", "Tuần4", ""};
        XAxis xAxis = moneyFlowChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(true);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.WHITE);
        xAxis.setAxisMinimum(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        YAxis leftAxis = moneyFlowChart.getAxisLeft();
        leftAxis.setAxisMinimum(0);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(2);
        leftAxis.setLabelCount(8, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        moneyFlowChart.getAxisRight().setEnabled(false);
        moneyFlowChart.getLegend().setEnabled(false);
        double[] valIncome = new double[getValueInCome.size()];
        double[] valOutCome =  new double[getValueOutCome.size()];
        ArrayList<BarEntry> barOne = new ArrayList<>();
        ArrayList<BarEntry> barTwo = new ArrayList<>();
            for (int i = 0; i < getValueInCome.size(); i++) {
                double cal1 = getValueInCome.get(i);
                double cal2 = getValueOutCome.get(i);
                valIncome[i] = cal1;
                valOutCome[i] = cal2;
            }
            for (int i = 0; i < valIncome.length; i++) {
                barOne.add(new BarEntry(i, (float) valIncome[i]));
                barTwo.add(new BarEntry(i, (float) valOutCome[i]));
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
        data.setValueFormatter(new CustomPercentFormatter());
        for (int i = 0; i< leftAxis.mEntries.length; i++){
            leftAxis.mEntries[i] = Math.round(leftAxis.mEntries[i]);
        }
        leftAxis.setValueFormatter(new ChartCurrencyFormatter());
        moneyFlowChart.setData(data);

        moneyFlowChart.setScaleEnabled(false);
        moneyFlowChart.setDrawValueAboveBar(false);
        moneyFlowChart.setVisibleXRangeMaximum(6f);
        moneyFlowChart.setHighlightFullBarEnabled(true);
        moneyFlowChart.groupBars(1f, groupSpace, barSpace);
        moneyFlowChart.animateXY(1000, 1000);
        moneyFlowChart.getLegend().setEnabled(false);
        moneyFlowChart.setHighlightPerTapEnabled(true);
        moneyFlowChart.setTouchEnabled(true);
        CustomChartView mv = new CustomChartView (getContext(), R.layout.chart_detail_text);
        moneyFlowChart.setMarkerView(mv);
        moneyFlowChart.getXAxis().setYOffset(20f);
        moneyFlowChart.setExtraBottomOffset(30f);
        moneyFlowChart.invalidate();
    }

    private void loadPiechart_mainpage(){
        ArrayList<Jars> JarsAList = MongoDB.getInstance().getAllJars();
        Integer percThietYeu =  JarsAList.get(0).getJarAmount();
        Integer percGiaoDuc = JarsAList.get(1).getJarAmount();
        Integer percTietKiem =  JarsAList.get(2).getJarAmount();
        Integer percHuongThu = JarsAList.get(3).getJarAmount();
        Integer percDauTu =  JarsAList.get(4).getJarAmount();
        Integer percThienTam = JarsAList.get(5).getJarAmount();

        PieChart pieChart = view.findViewById(R.id.jarsDistributeChart);
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry((float) percThietYeu,"Thiết yếu"));
        entries.add(new PieEntry((float) percGiaoDuc,"Giáo dục"));
        entries.add(new PieEntry((float) percTietKiem,"Tiết kiệm"));
        entries.add(new PieEntry((float) percHuongThu,"Hưởng thụ"));
        entries.add(new PieEntry((float) percDauTu,"Đầu tư"));
        entries.add(new PieEntry((float) percThienTam,"Thiện tâm"));
        PieDataSet pieDataSet  = new PieDataSet(entries,"Tỉ lệ phân bổ");
        pieDataSet.setValueTextSize(12f);
        pieDataSet.setColors(Color.parseColor("#0094FF"),
                Color.parseColor("#00FFE0"),
                Color.parseColor("#E84DE2"),
                Color.parseColor("#FFD615"),
                Color.parseColor("#FF1F5A"),
                Color.parseColor("#14FF00"),
                Color.parseColor("#14FF00"));




        PieData pieData  = new PieData(pieDataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextColor(Color.parseColor("#000000"));
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(100);

        pieChart.spin( 500,0,-360f, Easing.EasingOption.EaseInBounce);
        pieChart.setDrawEntryLabels(false);
        pieChart.setDrawCenterText(true);
        pieChart.setCenterTextSize(20f);
        pieChart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
        pieChart.setCenterTextColor(Color.parseColor("#222222"));
        pieChart.setCenterText("100%");
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(50);
        pieChart.setTouchEnabled(false);



        pieChart.invalidate();
    }
}