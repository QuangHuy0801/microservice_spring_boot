package com.example.doan_nhom_6.Adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.doan_nhom_6.Activity.AdminFragment;
import com.example.doan_nhom_6.Activity.IventoryFragment;
import com.example.doan_nhom_6.Activity.RQOrderFragment;
import com.example.doan_nhom_6.Activity.StatisticFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter{

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new StatisticFragment();
            case 1:
                return new IventoryFragment();
            case 2:
                return new RQOrderFragment();
            case 3:
                return new AdminFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}