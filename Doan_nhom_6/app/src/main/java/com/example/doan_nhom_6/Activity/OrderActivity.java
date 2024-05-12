package com.example.doan_nhom_6.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.doan_nhom_6.Adapter.OrderFragmentAdapter;
import com.example.doan_nhom_6.Model.Order;
import com.example.doan_nhom_6.Model.User;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.OrderAPI;
import com.example.doan_nhom_6.Somethings.ObjectSharedPreferences;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    OrderFragmentAdapter orderFragmentAdapter;
    ImageView ivHome, ivUser, ivCart, ivHistory;

    ConstraintLayout clOrder, clEmptyOrder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        AnhXa();
        appBarClick();
        FragmentManager fragmentManager = getSupportFragmentManager();
        orderFragmentAdapter = new OrderFragmentAdapter(fragmentManager, getLifecycle());
        CheckEmpty();
        viewPager2.setAdapter(orderFragmentAdapter);
        tabLayout.addTab(tabLayout.newTab().setText("All Order"));
        tabLayout.addTab(tabLayout.newTab().setText("Pay On Delivery"));
        tabLayout.addTab(tabLayout.newTab().setText("Pay With ZaloPay"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    private void CheckEmpty() {
        User user = ObjectSharedPreferences.getSavedObjectFromPreference(OrderActivity.this, "User", "MODE_PRIVATE", User.class);
        OrderAPI.orderAPI.getOrderByUserId(user.getId()).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful()) {
                    List<Order> orders = response.body();
                    if (orders != null && orders.isEmpty()) {
                        clOrder.setVisibility(View.GONE);
                        clEmptyOrder.setVisibility(View.VISIBLE);
                    }
                } else {
                    showConfirmationDialog();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                showConfirmationDialog();
            }
        });

    }
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
        builder.setTitle("Error Order page");
        builder.setMessage("Đã xảy ra sự cố order. Quay lại trang chủ!!!");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(OrderActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Dismiss dialog if "No" is clicked
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                AlertDialog alertDialog = (AlertDialog) dialog;
                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setTextColor(getResources().getColor(R.color.black));

                Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setTextColor(getResources().getColor(R.color.black)); // Đổi màu chữ của nút "No" thành màu đen
            }
        });
        dialog.show();
    }

    private void appBarClick() {
        ivHome.setOnClickListener(v -> {
            startActivity(new Intent(OrderActivity.this, MainActivity.class));
            finish();
        });
        ivUser.setOnClickListener(v -> {
            startActivity(new Intent(OrderActivity.this, UserActivity.class));
            finish();
        });
        ivCart.setOnClickListener(v -> {
            startActivity(new Intent(OrderActivity.this, CartActivity.class));
            finish();
        });

        ivHistory.setOnClickListener(v -> {
            startActivity(new Intent(OrderActivity.this, OrderActivity.class));
            finish();
        });
    }

    private void AnhXa() {
        ivHome = findViewById(R.id.ivHome);
        ivUser = findViewById(R.id.ivUser);
        ivCart = findViewById(R.id.ivCart);
        ivHistory = findViewById(R.id.ivHistory);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager2);
        clOrder = findViewById(R.id.clOrder);
        clEmptyOrder = findViewById(R.id.clEmptyOrder);
    }
}
