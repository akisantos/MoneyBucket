package com.akistd.moneybucket.ui.transaction;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.data.Jars;
import com.akistd.moneybucket.data.MongoDB;
import com.akistd.moneybucket.data.Transaction;
import com.akistd.moneybucket.util.UtilConverter;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThuNhapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThuNhapFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String Root_Frag = "root_fagment";
    Button btnSave, btnAllJam, btnDatePicker,saveBtn;
    ImageButton imgBtnOut;
    EditText editTotalMoney, editDescribe;
    private int mYear, mMonth, mDay;
    ArrayList<Jars> jarsList = MongoDB.getInstance().getAllJars();
    public ThuNhapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThuNhapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThuNhapFragment newInstance(String param1, String param2) {
        ThuNhapFragment fragment = new ThuNhapFragment();
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
        return inflater.inflate(R.layout.fragment_thu_nhap, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*btnSave = view.findViewById(R.id.btn);*/
        imgBtnOut = view.findViewById(R.id.imgBtnOut);
        btnAllJam = view.findViewById(R.id.btnAllJam);
        btnDatePicker = view.findViewById(R.id.btnDatePicker);
        editTotalMoney = view.findViewById(R.id.editTotalMoney);
        editDescribe = view.findViewById(R.id.editDescribe);
        saveBtn = view.findViewById(R.id.saveBtn);


        addEvent();
    }

    private void addEvent() {
        btnAllJam.setOnClickListener(v-> {
            /*FragmentTransaction fr = getFragmentManager().beginTransaction();
            fr.add(R.id.FLAllJam, new ThuNhapChild_AllJamFragment());
            fr.commit();*/
            Intent jarSettings = new Intent(getContext(), ThuNhapJarsSettingsActivity.class);
            startActivity(jarSettings, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());

        });

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        btnDatePicker.setText(UtilConverter.getInstance().vnTimeLocaleConverter(c.getTime()));

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                // Get Current Date
                /*final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                btnDatePicker.setText(String.format("%s - %s - %s", mDay, mMonth, mYear));*/

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, monthOfYear);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                                btnDatePicker.setText(UtilConverter.getInstance().vnTimeLocaleConverter(c.getTime()));

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        editTotalMoneyEvents();

        //Save btn
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewIncomeTransaction();
            }
        });
    }

    private void createNewIncomeTransaction(){

       if (dataValidator()){
           //Lấy ds jars mới nhất
           jarsList = MongoDB.getInstance().getAllJars();

           //Cập nhật giá trị cho jars và sync
           for (Jars jar: jarsList) {

               //Tính tiền chia
               Double newBalance = Double.parseDouble(String.valueOf(editTotalMoney.getText())) * jar.getJarAmount()/100;

               //Tạo transaction mới
               Transaction newIncome = new Transaction();
               newIncome.setOwner_id(MongoDB.getInstance().getUser().getId());
               newIncome.setCreateAt(Calendar.getInstance().getTime());
               newIncome.setTransAmount(newBalance);
               newIncome.setUser(MongoDB.getInstance().getMoneyUsers());
               newIncome.setJars(jar);
               newIncome.setTransNote(String.valueOf(editDescribe.getText()));
               MongoDB.getInstance().insertTransaction(newIncome);

               // Cập nhật hũ
               Jars modifyJar = new Jars(jar);
               modifyJar.setJarBalance(modifyJar.getJarBalance() + newIncome.getTransAmount());
               MongoDB.getInstance().updateJar(modifyJar);
           }

           //reload main
           getActivity().finish();
       }


    }

    private boolean dataValidator(){
        if (editTotalMoney.getText().length()>0){
            return true;
        }

        return false;
    }

    private void editTotalMoneyEvents(){
        editTotalMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}