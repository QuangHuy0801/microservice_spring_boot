package com.example.doan_nhom_6.Activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.doan_nhom_6.Adapter.AdminOrderFragmentAdapter;
import com.example.doan_nhom_6.Interface.OnTabLayoutChangedListener;
import com.example.doan_nhom_6.Fragment.CompletedOrderFragment;
import com.example.doan_nhom_6.Fragment.CanceledOrderFragment;
import com.example.doan_nhom_6.Fragment.DeliveringOrderFragment;
import com.example.doan_nhom_6.Fragment.PendingOrderFragment;
import com.example.doan_nhom_6.R;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

public class RQOrderFragment extends Fragment implements OnTabLayoutChangedListener {
    TabLayout tabLayout;
    ViewPager2 viewPager;
    AdminOrderFragmentAdapter adminOrderFragmentAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_r_q_order, container, false);
        anhXa(rootView);
        FragmentManager fragmentManager = getChildFragmentManager();
        OnTabLayoutChangedListener onTabLayoutChangedListener = this;
        adminOrderFragmentAdapter = new AdminOrderFragmentAdapter(fragmentManager, getLifecycle(), onTabLayoutChangedListener);
        viewPager.setAdapter(adminOrderFragmentAdapter);
        tabLayout.addTab(tabLayout.newTab().setText("Chờ Xác Nhận"));
        tabLayout.addTab(tabLayout.newTab().setText("Đang Giao"));
        tabLayout.addTab(tabLayout.newTab().setText("Đã Giao"));
        tabLayout.addTab(tabLayout.newTab().setText("Đã Hủy"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                onTabLayoutChanged(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        return rootView;
    }

    @Override
    public void onTabLayoutChanged(int position) {
        Fragment fragment = getChildFragmentManager().findFragmentByTag("f" + position);
        if (fragment instanceof PendingOrderFragment) {
            ((PendingOrderFragment) fragment).ReloadDataOnTabLayoutChaged();
        } else if (fragment instanceof DeliveringOrderFragment) {
            ((DeliveringOrderFragment) fragment).ReloadDataOnTabLayoutChaged();
        } else if (fragment instanceof CompletedOrderFragment) {
            ((CompletedOrderFragment) fragment).ReloadDataOnTabLayoutChaged();
        } else if (fragment instanceof CanceledOrderFragment) {
            ((CanceledOrderFragment) fragment).ReloadDataOnTabLayoutChaged();
        }
    }


    private void anhXa(View rootView) {
        tabLayout = rootView.findViewById(R.id.tabLayoutOrder);
        viewPager = rootView.findViewById(R.id.viewPagerOrder);
    }
}