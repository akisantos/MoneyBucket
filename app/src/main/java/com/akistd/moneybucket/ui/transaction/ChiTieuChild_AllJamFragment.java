package com.akistd.moneybucket.ui.transaction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.akistd.moneybucket.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChiTieuChild_AllJamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChiTieuChild_AllJamFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button btnConfirm;
    TextView txtSoDuTY, txtSoDuGD, txtSoDuTK, txtSoDuHT, txtSoDuDT, txtSoDuTT;

    public ChiTieuChild_AllJamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChiTieuChild_AllJamFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChiTieuChild_AllJamFragment newInstance(String param1, String param2) {
        ChiTieuChild_AllJamFragment fragment = new ChiTieuChild_AllJamFragment();
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
        return inflater.inflate(R.layout.fragment_chi_tieu_child__all_jam, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnConfirm = view.findViewById(R.id.btnConfirm);

        txtSoDuTY = view.findViewById(R.id.txtSoDuTY);
        txtSoDuGD = view.findViewById(R.id.txtSoDuGD);
        txtSoDuTK = view.findViewById(R.id.txtSoDuTK);
        txtSoDuHT = view.findViewById(R.id.txtSoDuHT);
        txtSoDuDT = view.findViewById(R.id.txtSoDuDT);
        txtSoDuTT = view.findViewById(R.id.txtSoDuTT);

        addEvent();
    }

    void addEvent() {

    }
}