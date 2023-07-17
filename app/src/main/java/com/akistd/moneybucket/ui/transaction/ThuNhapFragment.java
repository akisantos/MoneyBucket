package com.akistd.moneybucket.ui.transaction;

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
    Button btnSave, btnAllJam, btnDatePicker;
    ImageButton imgBtnOut;
    EditText editTotalMoney, editDescribe;
    private int mYear, mMonth, mDay;
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


        addEvent();
    }

    private void addEvent() {
        btnAllJam.setOnClickListener(v-> {
            /*FragmentTransaction fr = getFragmentManager().beginTransaction();
            fr.add(R.id.FLAllJam, new ThuNhapChild_AllJamFragment());
            fr.commit();*/
            Intent jarSettings = new Intent(getContext(), ThuNhapJarsSettingsActivity.class);
            startActivity(jarSettings);
        });

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

        editTotalMoneyEvents();
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