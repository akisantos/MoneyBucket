package com.akistd.moneybucket.ui.homepage;

import com.akistd.moneybucket.R;

import java.util.ArrayList;

public class CJarlist {
    static String info;
    static String amount;
    static int id;
    public CJarlist(int id, String info, String amount) {
        this.id = id;
        this.info = info;
        this.amount = amount;
    }

    public static int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public static String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public static ArrayList jarlist(){
        ArrayList<CJarlist> arrayList = new ArrayList<>();
        arrayList.add(new CJarlist(R.drawable.too, "Thiết yếu", "300,000 VND"));
        arrayList.add(new CJarlist(R.drawable.too, "Giáo dục", "300,000 VND"));
        arrayList.add(new CJarlist(R.drawable.too, "Tiết kiệm", "300,000 VND"));
        arrayList.add(new CJarlist(R.drawable.too, "Hưởng thụ", "300,000 VND"));
        arrayList.add(new CJarlist(R.drawable.too, "Đầu tư", "300,000 VND"));
        arrayList.add(new CJarlist(R.drawable.too, "Thiện tâm", "300,000 VND"));
        return arrayList;
    }

}
