package com.akistd.moneybucket.ui.baocaochithu;

import java.util.ArrayList;

public class ItemHu_bcct {
    int img;
    String nameJar;

    public ItemHu_bcct(int img, String nameJar) {
        this.img = img;
        this.nameJar = nameJar;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getNameJar() {
        return nameJar;
    }

    public void setNameJar(String nameJar) {
        this.nameJar = nameJar;
    }
    public static ArrayList<ItemHu_bcct> initSex(int[] img1, String[] sex2){
        ArrayList<ItemHu_bcct> itemHu_bccts = new ArrayList<>();
        for(int i = 0;i<sex2.length;i++){
            ItemHu_bcct item = new ItemHu_bcct(img1[i],sex2[i]);
            itemHu_bccts.add(item);
        }
        return itemHu_bccts;
    }
}
