package com.akistd.moneybucket.ui.transaction;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TransactionTabLayoutAdapter extends FragmentPagerAdapter {
    private Context context;
    int totalTabs;

    public TransactionTabLayoutAdapter(Context _context, FragmentManager fm, int totalTabs){
        super(fm);
        context = _context;
        this.totalTabs = totalTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ThuNhapFragment fragment = new ThuNhapFragment();
                return fragment;
            case 1:
                ChiTieuFragment fragmentchitieu = new ChiTieuFragment();
                return fragmentchitieu;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
