package com.example.doan_nhom_6.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.doan_nhom_6.Adapter.StatisticAdapter;
import com.example.doan_nhom_6.Adapter.ViewPagerAdapter;
import com.example.doan_nhom_6.Model.Statistic;
import com.example.doan_nhom_6.Model.User;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Somethings.ObjectSharedPreferences;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainDashboardActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    ViewPager viewPager;
    TextView tvHiNameAdmin;
//    ImageView ivAvatarAdmin;
    User Admin;
    ViewPagerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);
        setControl();
        setEvent();
    }

    private void setEvent() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.itstatis).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.itinventory).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.itRQOrder).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.itIFAdmin).setChecked(true);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.itstatis) {
                    viewPager.setCurrentItem(0);
                }
                else if(item.getItemId()==R.id.itinventory) {
                    viewPager.setCurrentItem(1);
                }
                else if(item.getItemId()==R.id.itRQOrder) {
                    viewPager.setCurrentItem(2);
                }
                else if(item.getItemId()==R.id.itIFAdmin) {
                    viewPager.setCurrentItem(3);
                }
                return true;
            }
        });
    }

    private void setControl() {
        tvHiNameAdmin = findViewById(R.id.tvHiNameAdmin);
//        ivAvatarAdmin = findViewById(R.id.ivAvatarAdmin);
        bottomNavigationView =findViewById(R.id.bottom_navigation);
        viewPager =findViewById(R.id.viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadUserInfor();
    }

    private void LoadUserInfor() {
        Admin = ObjectSharedPreferences.getSavedObjectFromPreference(MainDashboardActivity.this, "Admin", "MODE_PRIVATE", User.class);
        tvHiNameAdmin.setText("Hi "+ Admin.getUser_Name());
//        try {
//            Glide.with(getApplicationContext()).load(Admin.getAvatar()).into(ivAvatarAdmin);
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
    }
}