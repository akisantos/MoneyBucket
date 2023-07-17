package com.akistd.moneybucket.ui.homepage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.data.Jars;

import java.util.ArrayList;

public class Cjarlist_listAdapter extends BaseAdapter {
    Context context;
    ArrayList<Jars> arrayList;
    int layout;


    public Cjarlist_listAdapter(@NonNull Context context, int resource, ArrayList<Jars> objects) {
        this.arrayList = objects;
        this.context = context;
        this.layout = resource;
        Log.v("AKI LOGG", String.valueOf(arrayList.size()));
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Jars getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.jarlist_layout_mainpage, null);
        }
        Jars jar = getItem(position);

        ImageView img = convertView.findViewById(R.id.mainpage_jarlist_img);

        if (jar.getJarName().equals("Đầu tư")){
            img.setImageResource(R.drawable.hu1);
        } else if (jar.getJarName().equals("Hưởng thụ")) {
            img.setImageResource(R.drawable.hu2);
        }else if (jar.getJarName().equals("Giáo dục")) {
            img.setImageResource(R.drawable.hu3);
        }else if (jar.getJarName().equals("Thiện tâm")) {
            img.setImageResource(R.drawable.hu4);
        }else if (jar.getJarName().equals("Thiết yếu")) {
            img.setImageResource(R.drawable.hu5);
        }else if (jar.getJarName().equals("Tiết kiệm")) {
            img.setImageResource(R.drawable.hu6);
        }
        TextView info = convertView.findViewById(R.id.mainpage_jarlist_info);
        info.setText(jar.getJarName());
        TextView amount = convertView.findViewById(R.id.mainpage_jarlist_amount);
        amount.setText(String.format("%.0f",jar.getJarBalance())+ " VND");

        return convertView;
    }
}
