package com.akistd.moneybucket.ui.transaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.data.Jars;
import com.akistd.moneybucket.util.InputFilterMinMax;

import java.util.ArrayList;
import java.util.List;

public class JarsThuNhapActivityApdater extends BaseAdapter {

    public ArrayList<Jars> data = new ArrayList<>();
    private Context context;
    public List<Integer> jarsAmountList = new ArrayList<>();
    TextView totalText;
    public JarsThuNhapActivityApdater(Context _context, ArrayList<Jars> jarsList, TextView totalText){
        this.context = _context;
        this.data = jarsList;
        this.totalText = totalText;
        for (Jars jar:data) {
            jarsAmountList.add(jar.getJarAmount());
        }

    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Jars getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final JarsListViewHolder holder;

        if (convertView == null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.jarlist_thunhap_activity_row, parent,false);

            holder = new JarsListViewHolder();
            holder.mainpage_jarlist_name = convertView.findViewById(R.id.mainpage_jarlist_name);
            holder.mainpage_jarlist_img = convertView.findViewById(R.id.mainpage_jarlist_img);
            holder.addPorpotionBtn = convertView.findViewById(R.id.addPorpotionBtn);
            holder.minusPorpotionBtn = convertView.findViewById(R.id.minusPorpotionBtn);
            holder.porpotionText = convertView.findViewById(R.id.porpotionText);
            convertView.setTag(holder);
        }else{
            holder = (JarsListViewHolder) convertView.getTag();
        }

        final Jars jar = getItem(position);


        holder.mainpage_jarlist_name.setText(jar.getJarName());

        switch (jar.getJarName()) {
            case "Đầu tư" -> holder.mainpage_jarlist_img.setImageResource(R.drawable.hu1);
            case "Hưởng thụ" -> holder.mainpage_jarlist_img.setImageResource(R.drawable.hu2);
            case "Giáo dục" -> holder.mainpage_jarlist_img.setImageResource(R.drawable.hu3);
            case "Thiện tâm" -> holder.mainpage_jarlist_img.setImageResource(R.drawable.hu4);
            case "Thiết yếu" -> holder.mainpage_jarlist_img.setImageResource(R.drawable.hu5);
            case "Tiết kiệm" -> holder.mainpage_jarlist_img.setImageResource(R.drawable.hu6);
        }



        holder.addPorpotionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPorpotion(position, holder.porpotionText,1);
            }
        });

        holder.minusPorpotionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPorpotion(position, holder.porpotionText,-1);
            }
        });



        holder.addPorpotionBtn.setOnTouchListener(new View.OnTouchListener() {

            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 100);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    addPorpotion(position, holder.porpotionText,1);
                    mHandler.postDelayed(this, 100);
                }
            };

        });

        holder.minusPorpotionBtn.setOnTouchListener(new View.OnTouchListener() {

            private Handler mHandler;

            @Override public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mHandler != null) return true;
                        mHandler = new Handler();
                        mHandler.postDelayed(mAction, 100);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mHandler == null) return true;
                        mHandler.removeCallbacks(mAction);
                        mHandler = null;
                        break;
                }
                return false;
            }

            Runnable mAction = new Runnable() {
                @Override public void run() {
                    addPorpotion(position, holder.porpotionText,-1);
                    mHandler.postDelayed(this, 100);
                }
            };

        });


        holder.porpotionText.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "100")});
        holder.porpotionText.setEnabled(false);
        holder.porpotionText.setText(jarsAmountList.get(position).toString());



        return convertView;
    }



    @SuppressLint("SetTextI18n")
    private void addPorpotion(int position, EditText porpotionText, int value){
        if (value > 0){
            if (jarsAmountList.get(position) + value <= 100){
                jarsAmountList.set(position, (int) (jarsAmountList.get(position) + value));
            }
        }else {
            if (jarsAmountList.get(position) + value >= 0){
                jarsAmountList.set(position, (int) (jarsAmountList.get(position) + value));
            }
        }

        porpotionText.setText(String.valueOf(jarsAmountList.get(position)));
        int total = 0;
        for (int i : jarsAmountList) {
            total += i;
        }

        totalText.setText(total +"%");
        if (total != 100){
            totalText.setTextColor(Color.parseColor("#ff5252"));
        }else{
            totalText.setTextColor(Color.parseColor("#00d5bc"));
        }
    }
}
