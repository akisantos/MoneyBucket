package com.akistd.moneybucket.adapters.testing;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.data.Jars;

import java.util.ArrayList;

public class JarsListApdater extends BaseAdapter {
    ArrayList<Jars> usersList ;

    public JarsListApdater(ArrayList<Jars> usersList) {
        this.usersList = usersList;
    }

    @Override
    public int getCount() {
        return usersList.size();
    }

    @Override
    public Object getItem(int position) {
        return usersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewProduct;
        if (convertView == null) {
            viewProduct = View.inflate(parent.getContext(), R.layout.testing_simple_listdata_row, null);
        } else viewProduct = convertView;

        //Bind sữ liệu phần tử vào View
        Jars users = (Jars) getItem(position);
        ((TextView) viewProduct.findViewById(R.id.userIdTV)).setText(users.getOwner_id().toString());
        ((TextView) viewProduct.findViewById(R.id.userNameTV)).setText(String.format("Tên hũ : %s", users.getJarName()));
        ((TextView) viewProduct.findViewById(R.id.userBalanceTV)).setText("% hũ:" + users.getJarAmount().toString());


        return viewProduct;
    }

    /*@Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        usersList = MongoDB.getInstance().getData();
    }*/
}
