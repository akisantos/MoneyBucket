package com.akistd.moneybucket.ui.transaction;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.data.Jars;
import com.akistd.moneybucket.data.MongoDB;
import com.akistd.moneybucket.data.Transaction;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChiTieuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChiTieuFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Button saveBtn, btnDatePicker;
    Spinner btnAllJam;
    ImageButton imgBtnOut;
    EditText editTotalMoney, editDescribe;
    private int mYear, mMonth, mDay;
    JarsChiTieuFragSpinnerAdapter adapter;
    ArrayList<Jars> jarsList = MongoDB.getInstance().getAllJars();
    public ChiTieuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChiTieuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChiTieuFragment newInstance(String param1, String param2) {
        ChiTieuFragment fragment = new ChiTieuFragment();
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



        return inflater.inflate(R.layout.fragment_chi_tieu, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgBtnOut = view.findViewById(R.id.imgBtnOut);
        btnAllJam = view.findViewById(R.id.btnAllJam);
        btnDatePicker = view.findViewById(R.id.btnDatePicker);
        editTotalMoney = view.findViewById(R.id.editTotalMoney);
        editDescribe = view.findViewById(R.id.editDescribe);
        saveBtn = view.findViewById(R.id.saveBtn);

        addEvent();
    }

    private void addEvent() {

        jarListsSpinnerEvents();

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);



                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                btnDatePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEvents();
            }
        });


    }

    private void saveEvents(){
        createNewIncomeTransaction();
    }

    private void createNewIncomeTransaction(){

        if (dataValidator()){
            //Lấy jar được chọn
            Jars selectedJars = (Jars) btnAllJam.getSelectedItem();

            //Lóc cóc tính tiền!
            Double newBalance = Double.parseDouble(String.valueOf(editTotalMoney.getText()));

           if (selectedJars.getJarBalance() - newBalance >=0){
               //Tạo transaction mới
               Transaction newIncome = new Transaction();
               newIncome.setOwner_id(MongoDB.getInstance().getUser().getId());
               newIncome.setCreateAt(Calendar.getInstance().getTime());
               newIncome.setTransAmount(-newBalance);
               newIncome.setUser(MongoDB.getInstance().getMoneyUsers());
               newIncome.setJars(selectedJars);
               newIncome.setTransNote(String.valueOf(editDescribe.getText()));
               MongoDB.getInstance().insertTransaction(newIncome);

               // Cập nhật hũ
               Jars modifyJar = new Jars(selectedJars);
               modifyJar.setJarBalance(modifyJar.getJarBalance() + newIncome.getTransAmount());
               MongoDB.getInstance().updateJar(modifyJar);

               //reload main
               getActivity().finish();
           }else {
               Toast.makeText(getContext(), "You'r totally ran out of money! Beggar!", Toast.LENGTH_SHORT).show();
           }
        }


    }

    private boolean dataValidator(){
        if (editTotalMoney.getText().length()>0){
            return true;
        }

        return false;
    }

    private void jarListsSpinnerEvents(){
        adapter = new JarsChiTieuFragSpinnerAdapter(getContext(), jarsList);
        adapter.setDropDownViewResource(R.layout.jarlist_chitieu_frag_dropdown);
        btnAllJam.setAdapter(adapter);


    }
}