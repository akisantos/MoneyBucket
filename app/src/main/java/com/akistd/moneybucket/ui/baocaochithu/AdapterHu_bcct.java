package com.akistd.moneybucket.ui.baocaochithu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.akistd.moneybucket.R;

import java.util.ArrayList;

public class AdapterHu_bcct extends BaseAdapter {
    LayoutInflater inflater;
    ArrayList<ItemHu_bcct> itemHu_bccts = new ArrayList<>();
    int layout;

    public AdapterHu_bcct(LayoutInflater inflater, ArrayList<ItemHu_bcct> itemHu_bccts, int layout) {
        this.inflater = inflater;
        this.itemHu_bccts = itemHu_bccts;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return itemHu_bccts.size();
    }

    @Override
    public Object getItem(int position) {
        return itemHu_bccts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemHu_bcct item =itemHu_bccts.get(position);
        View rowView = inflater.inflate(layout,null,true);
        TextView namejar = (TextView) rowView.findViewById(R.id.tv_namejar);
        namejar.setText(item.getNameJar());
        ImageView img = (ImageView) rowView.findViewById(R.id.img_jar);
        img.setImageResource(item.getImg());
        return rowView;
    }
}
