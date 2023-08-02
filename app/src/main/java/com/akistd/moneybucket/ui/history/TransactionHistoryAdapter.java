package com.akistd.moneybucket.ui.history;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.data.Jars;
import com.akistd.moneybucket.data.Transaction;
import com.akistd.moneybucket.util.UtilConverter;

import java.util.ArrayList;

public class TransactionHistoryAdapter extends BaseAdapter {
    Context context;
    int resource;
    ArrayList<Transaction> dataList = new ArrayList<>();
    ArrayList<Jars> jarList = new ArrayList<>();
    TransactionListviewHolder holder;

    public TransactionHistoryAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Transaction> objects, ArrayList<Jars> jarList) {
        this.context = context;
        this.resource = resource;
        this.dataList = objects;
        this.jarList = jarList;

    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Transaction getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.transaction_history_row, parent,false);

            holder = new TransactionListviewHolder();
            holder.imgThumb = convertView.findViewById(R.id.imgThumb);
            holder.transactionValueTxt = convertView.findViewById(R.id.transactionValueTxt);
            holder.jarName = convertView.findViewById(R.id.jarName);
            holder.txtDate = convertView.findViewById(R.id.txtDate);
            convertView.setTag(holder);
        }else{
            holder = (TransactionListviewHolder) convertView.getTag();
        }

        final Transaction transaction = getItem(position);
        final Jars jars = jarList.get(jarList.indexOf(transaction.getJars()));


        holder.jarName.setText(jars.getJarName());
        String[] dateTrim = UtilConverter.getInstance().vnTimeLocaleConverter(transaction.getCreateAt()).split(",");
        String[] monthTrim = dateTrim[1].split(" ");
        holder.txtDate.setText(String.format("%s, ngày %s",dateTrim[0],monthTrim[1]));
        if (transaction.getTransAmount() >0){

            holder.transactionValueTxt.setText("+" + UtilConverter.getInstance().vndCurrencyConverter(transaction.getTransAmount()));
            holder.transactionValueTxt.setTextColor(Color.parseColor("#2ecc71"));
        }else {

            holder.transactionValueTxt.setText(UtilConverter.getInstance().vndCurrencyConverter(transaction.getTransAmount()));
            holder.transactionValueTxt.setTextColor(Color.parseColor("#e74c3c"));
        }

        switch (jars.getJarName()) {
            case "Đầu tư" -> holder.imgThumb.setImageResource(R.drawable.hu1);
            case "Hưởng thụ" -> holder.imgThumb.setImageResource(R.drawable.hu2);
            case "Giáo dục" -> holder.imgThumb.setImageResource(R.drawable.hu3);
            case "Thiện tâm" -> holder.imgThumb.setImageResource(R.drawable.hu4);
            case "Thiết yếu" -> holder.imgThumb.setImageResource(R.drawable.hu5);
            case "Tiết kiệm" -> holder.imgThumb.setImageResource(R.drawable.hu6);
        }


        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setTitle("Thông tin giao dịch");

                String date = UtilConverter.getInstance().vnTimeHourLocaleConverter(transaction.getCreateAt());
                String note = transaction.getTransNote();
                if (note.isEmpty()) note = "Trống";
                String value = UtilConverter.getInstance().vndCurrencyConverter(transaction.getTransAmount());

                builder1.setMessage(String.format("Thuộc hũ: %s \nNgày thực hiện: %s \nGiá trị: %s \nNội dung: %s ",transaction.getJars().getJarName(),date,value,note));

                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Đóng",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });


                AlertDialog alert11 = builder1.create();
                alert11.show();
                return false;
            }
        });

        return convertView;
    }
}
