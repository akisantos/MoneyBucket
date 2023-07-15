package com.akistd.moneybucket.ui.transaction;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.akistd.moneybucket.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThuNhapChild_AllJamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThuNhapChild_AllJamFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageButton btnMinusTY, btnMinusGD, btnMinusTK, btnMinusHT, btnMinusDT, btnMinusTT;
    ImageButton btnPlusTY, btnPlusGD, btnPlusTK, btnPlusHT, btnPlusDT, btnPlusTT;
    EditText editPercentTY, editPercentGD, editPercentTK, editPercentHT,editPercentDT, editPercentTT, editTotalPercent;
    Button btnConfirm;



    public ThuNhapChild_AllJamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThuNhapChild_AllJamFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThuNhapChild_AllJamFragment newInstance(String param1, String param2) {
        ThuNhapChild_AllJamFragment fragment = new ThuNhapChild_AllJamFragment();
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
        return inflater.inflate(R.layout.fragment_thu_nhap_child__all_jam, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //add control
        btnMinusTY = view.findViewById(R.id.btnMinusTY);
        btnMinusGD = view.findViewById(R.id.btnMinusGD);
        btnMinusTK = view.findViewById(R.id.btnMinusTK);
        btnMinusHT = view.findViewById(R.id.btnMinusHT);
        btnMinusDT = view.findViewById(R.id.btnMinusDT);
        btnMinusTT = view.findViewById(R.id.btnMinusTT);

        btnPlusTY = view.findViewById(R.id.btnPlusTY);
        btnPlusGD = view.findViewById(R.id.btnPlusGD);
        btnPlusTK = view.findViewById(R.id.btnPlusTK);
        btnPlusHT = view.findViewById(R.id.btnPlusHT);
        btnPlusDT = view.findViewById(R.id.btnPlusDT);
        btnPlusTT = view.findViewById(R.id.btnPlusTT);

        editPercentTY = view.findViewById(R.id.editPercentTY);
        editPercentGD = view.findViewById(R.id.editPercentGD);
        editPercentTK = view.findViewById(R.id.editPercentTK);
        editPercentHT = view.findViewById(R.id.editPercentHT);
        editPercentDT = view.findViewById(R.id.editPercentDT);
        editPercentTT = view.findViewById(R.id.editPercentTT);
        editTotalPercent = view.findViewById(R.id.editTotalPercent);

        btnConfirm = view.findViewById(R.id.btnConfirm);
        //
        addEvent();
        countTotalPercentage();
    }

    void addEvent() {
        btnConfirm.setOnClickListener(v -> {
            Integer totalPercent = countTotalPercentage();

            if(totalPercent != 100) {
                Toast.makeText(getActivity(),"Total percentage must be 100%",Toast.LENGTH_SHORT).show();
            }
            else {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.FL, new ThuNhapFragment());
                fr.commit();
            }
        });

        editPercentTY.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                countTotalPercentage();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editPercentGD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                countTotalPercentage();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editPercentTK.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                countTotalPercentage();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editPercentHT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                countTotalPercentage();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editPercentDT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                countTotalPercentage();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editPercentTT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                countTotalPercentage();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnMinusTY.setOnClickListener(v -> {
            editPercentTY.setText(String.valueOf(Integer.parseInt(String.valueOf(editPercentTY.getText())) - 5));
        });
        btnMinusGD.setOnClickListener(v -> {
            editPercentGD.setText(String.valueOf(Integer.parseInt(String.valueOf(editPercentGD.getText())) - 5));
        });
        btnMinusTK.setOnClickListener(v -> {
            editPercentTK.setText(String.valueOf(Integer.parseInt(String.valueOf(editPercentTK.getText())) - 5));
        });
        btnMinusHT.setOnClickListener(v -> {
            editPercentHT.setText(String.valueOf(Integer.parseInt(String.valueOf(editPercentHT.getText())) - 5));
        });
        btnMinusDT.setOnClickListener(v -> {
            editPercentDT.setText(String.valueOf(Integer.parseInt(String.valueOf(editPercentDT.getText())) - 5));
        });
        btnMinusTT.setOnClickListener(v -> {
            editPercentTT.setText(String.valueOf(Integer.parseInt(String.valueOf(editPercentTT.getText())) - 5));
        });

        btnPlusTY.setOnClickListener(v -> {
            editPercentTY.setText(String.valueOf(Integer.parseInt(String.valueOf(editPercentTY.getText())) + 5));
        });
        btnPlusGD.setOnClickListener(v -> {
            editPercentGD.setText(String.valueOf(Integer.parseInt(String.valueOf(editPercentGD.getText())) + 5));
        });
        btnPlusTK.setOnClickListener(v -> {
            editPercentTK.setText(String.valueOf(Integer.parseInt(String.valueOf(editPercentTK.getText())) + 5));
        });
        btnPlusHT.setOnClickListener(v -> {
            editPercentHT.setText(String.valueOf(Integer.parseInt(String.valueOf(editPercentHT.getText())) + 5));
        });
        btnPlusDT.setOnClickListener(v -> {
            editPercentDT.setText(String.valueOf(Integer.parseInt(String.valueOf(editPercentDT.getText())) + 5));
        });
        btnPlusTT.setOnClickListener(v -> {
            editPercentTT.setText(String.valueOf(Integer.parseInt(String.valueOf(editPercentTT.getText())) + 5));
        });
    }

    Integer countTotalPercentage() {
        Integer totalPercent = Integer.parseInt(String.valueOf(editPercentTY.getText())) + Integer.parseInt(String.valueOf(editPercentGD.getText()))
                + Integer.parseInt(String.valueOf(editPercentTK.getText())) + Integer.parseInt(String.valueOf(editPercentHT.getText()))
                + Integer.parseInt(String.valueOf(editPercentDT.getText())) + Integer.parseInt(String.valueOf(editPercentTT.getText()));

        editTotalPercent.setText(String.valueOf(totalPercent));
        return totalPercent;
    }

}