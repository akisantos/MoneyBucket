package com.akistd.moneybucket.ui.transaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.data.Jars;

import java.util.ArrayList;

public class JarsChiTieuFragSpinnerAdapter extends ArrayAdapter<Jars> {


    public ArrayList<Jars> data = new ArrayList<>();
    private Context context;

    private TextView jarBalanceText, mainpage_jarlist_name;
    private ImageView mainpage_jarlist_img;

    public JarsChiTieuFragSpinnerAdapter(@NonNull Context context, @NonNull ArrayList<Jars> objects) {
        super(context,0 ,objects);
        this.context = context;
        this.data = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
    View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    @SuppressLint("SetTextI18n")
    private View initView(int position, View convertView, ViewGroup parent)
    {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.jarlist_chitieu_frag_row, parent, false);

        }

        final Jars jar = data.get(position);

        jarBalanceText = convertView.findViewById(R.id.jarBalanceText);
        mainpage_jarlist_name = convertView.findViewById(R.id.mainpage_jarlist_name);
        mainpage_jarlist_img = convertView.findViewById(R.id.mainpage_jarlist_img);

        switch (jar.getJarName()) {
            case "Đầu tư" -> mainpage_jarlist_img.setImageResource(R.drawable.hu1);
            case "Hưởng thụ" -> mainpage_jarlist_img.setImageResource(R.drawable.hu2);
            case "Giáo dục" -> mainpage_jarlist_img.setImageResource(R.drawable.hu3);
            case "Thiện tâm" -> mainpage_jarlist_img.setImageResource(R.drawable.hu4);
            case "Thiết yếu" -> mainpage_jarlist_img.setImageResource(R.drawable.hu5);
            case "Tiết kiệm" -> mainpage_jarlist_img.setImageResource(R.drawable.hu6);
        }

        mainpage_jarlist_name.setText(jar.getJarName());
        jarBalanceText.setText(String.format("%.0f",jar.getJarBalance()) + "VND");
        return convertView;
    }


}
