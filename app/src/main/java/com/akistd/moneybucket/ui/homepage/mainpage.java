package com.akistd.moneybucket.ui.homepage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.akistd.moneybucket.ui.history.HistoryActivity;
import com.akistd.moneybucket.ui.history.TransactionHistoryAdapter;
import com.akistd.moneybucket.ui.transaction.TransactionsActivity;
import com.akistd.moneybucket.util.Constants;
import com.akistd.moneybucket.util.UtilConverter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.ArrayList;

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

    AppCompatButton mainpage_btn_historySeemore, mainpage_btn_addIncome, mainpage_btn_addOutcome,mainpage_btn_moneyFlowSeemore;

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

    }

    private void loadSoDu(){
        ArrayList<Jars> jars = MongoDB.getInstance().getAllJars();
        Double sodu= Double.valueOf(0d);
        for (Jars jar: jars) {
            sodu += jar.getJarBalance();
        }


        ArrayList<Transaction> latestIncome = MongoDB.getInstance().getAllSortedIncomeTransaction();
        Double totalIncome= Double.valueOf(0d);
        if (latestIncome.size()>0){

            for (int i=0; i<5; i++){
                totalIncome += latestIncome.get(i).getTransAmount();
            }
        }
        ArrayList<Transaction> thisMonthOutcome = MongoDB.getInstance().getThisMonthSortedOutcomeTransaction();
        Double totalOutcome= Double.valueOf(0d);
        for (Transaction tr: thisMonthOutcome) {
            totalOutcome += tr.getTransAmount() *-1;

        }

        //Công thức - toàn bộ tiền trong hũ - tiền đã tiêu trong tháng này.
        Double percentRaw = 100 - (totalOutcome/ totalIncome)*100;
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
        moneyFlowChart = view.findViewById(R.id.moneyFlow_barchart);

        moneyFlowChart.setDrawBarShadow(false);
        moneyFlowChart.getDescription().setEnabled(false);
        moneyFlowChart.setPinchZoom(false);
        moneyFlowChart.setDrawGridBackground(true);
        // empty labels so that the names are spread evenly
        String[] days = {"", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "CN", ""};
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

        ArrayList<Transaction> incomeTr = MongoDB.getInstance().getAllSortedIncomeTransaction();

        float[] valOne = {900, 0, 400, 200, 800, 700, 0}; //thu
        float[] valTwo = {1200, 500, 400, 30, 200, 600, 1200};  //chi

        ArrayList<BarEntry> barOne = new ArrayList<>();
        ArrayList<BarEntry> barTwo = new ArrayList<>();

        Double totalIncome = Double.valueOf(0d);
        ArrayList<Transaction> latestIncome= new ArrayList<>();

        /*for (int i =0; i<6; i++){
            latestIncome.add(incomeTr.get(i));
            totalIncome += latestIncome.get(i).getTransAmount();
        }


        for (int i =0; i<6; i++){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(latestIncome.get(i).getCreateAt());
            barOne.add(new BarEntry(i,valOne));

        }*/

        /*for (int i = 0; i < valOne.length; i++) {
            barOne.add(new BarEntry(i, valOne[i]));
            barTwo.add(new BarEntry(i, valTwo[i]));

        }*/

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