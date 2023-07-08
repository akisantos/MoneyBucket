package com.akistd.moneybucket.ui.homepage;

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

import java.util.ArrayList;

public class Cjarlist_listAdapter extends ArrayAdapter {
    Context context;
    ArrayList<CJarlist> arrayList;
    int layout;


    public Cjarlist_listAdapter(@NonNull Context context, int resource, ArrayList<CJarlist> objects) {
        super(context, resource);
        this.arrayList = objects;
        this.context = context;
        this.layout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CJarlist jarlist = arrayList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, null);
        }

        ImageView img = convertView.findViewById(R.id.mainpage_jarlist_img);
        img.setImageResource(CJarlist.getId());
        TextView info = convertView.findViewById(R.id.mainpage_jarlist_info);
        info.setText(CJarlist.getInfo());
        TextView amount = convertView.findViewById(R.id.mainpage_jarlist_amount);
        amount.setText(CJarlist.getAmount());


        return convertView;
    }
}
