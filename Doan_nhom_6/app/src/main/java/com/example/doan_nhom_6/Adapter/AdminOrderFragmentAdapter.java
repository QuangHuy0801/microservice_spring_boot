package com.example.doan_nhom_6.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.doan_nhom_6.Fragment.CanceledOrderFragment;
import com.example.doan_nhom_6.Fragment.CompletedOrderFragment;
import com.example.doan_nhom_6.Fragment.DeliveringOrderFragment;
import com.example.doan_nhom_6.Fragment.PendingOrderFragment;
import com.example.doan_nhom_6.Interface.OnTabLayoutChangedListener;

public class AdminOrderFragmentAdapter extends FragmentStateAdapter{
    final OnTabLayoutChangedListener mListener;
    public AdminOrderFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, OnTabLayoutChangedListener listener) {
        super(fragmentManager, lifecycle);
        mListener = listener;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0)
            return new PendingOrderFragment();
        else if (position == 1) {
            return new DeliveringOrderFragment();
        }
        else if (position == 2)
            return new CompletedOrderFragment();
        else
            return new CanceledOrderFragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }

}
