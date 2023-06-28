package com.akistd.moneybucket.adapters.testing;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.akistd.moneybucket.R;
import com.akistd.moneybucket.data.MongoDB;
import com.akistd.moneybucket.data.Users;

import java.util.ArrayList;

public class UsersListApdater extends BaseAdapter {
    ArrayList<Users> usersList ;

    public UsersListApdater(ArrayList<Users> usersList) {
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
        Users users = (Users) getItem(position);
        ((TextView) viewProduct.findViewById(R.id.userIdTV)).setText(users.getOwner_id().toString());
        ((TextView) viewProduct.findViewById(R.id.userNameTV)).setText(String.format("usrname : %s", users.getUserName()));
        ((TextView) viewProduct.findViewById(R.id.userBalanceTV)).setText(users.getUserBalance().toString());


        return viewProduct;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        usersList = MongoDB.getInstance().getData();
    }
}
