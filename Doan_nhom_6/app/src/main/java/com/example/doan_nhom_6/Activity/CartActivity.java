package com.example.doan_nhom_6.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_nhom_6.Adapter.CartAdapter;
import com.example.doan_nhom_6.Interface.CartItemInterface;
import com.example.doan_nhom_6.Model.Cart;
import com.example.doan_nhom_6.Model.Promotion;
import com.example.doan_nhom_6.Model.User;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.CartAPI;
import com.example.doan_nhom_6.Retrofit.PromotionAPI;
import com.example.doan_nhom_6.Somethings.ObjectSharedPreferences;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements CartItemInterface {
    CartAdapter cartAdapter;
    RecyclerView recyclerViewCart;
    Button btnCheckout;
    TextView tvTotalPrice;
    ImageView ivHome, ivUser, ivCart, ivHistory;
    ConstraintLayout clCartIsEmpty, clCart;
    List<Cart> listCart = new ArrayList<>();
    int Total = 0;
    int totalCalls;
    int completedCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setControl();
        setEvent();
    }

    private void setEvent() {
        appBarClick();
        btnCheckoutClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Total = 0;
        LoadCart();
    }

    private void btnCheckoutClick() {
        btnCheckout.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, CheckOutActivity.class);
            startActivity(intent);
        });
    }

    private void appBarClick() {
        ivHome.setOnClickListener(v -> {
            startActivity(new Intent(CartActivity.this, MainActivity.class));
            finish();
        });
        ivUser.setOnClickListener(v -> {
            startActivity(new Intent(CartActivity.this, UserActivity.class));
            finish();
        });
        ivCart.setOnClickListener(v -> {
            startActivity(new Intent(CartActivity.this, CartActivity.class));
            finish();
        });

        ivHistory.setOnClickListener(v -> {
            startActivity(new Intent(CartActivity.this, OrderActivity.class));
            finish();
        });
    }

    private void LoadCart() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerViewCart = findViewById(R.id.view);
        recyclerViewCart.setLayoutManager(linearLayoutManager);
        User user = ObjectSharedPreferences.getSavedObjectFromPreference(CartActivity.this, "User", "MODE_PRIVATE", User.class);
        CartItemInterface cartItemInterface = this;
        //GET API
        CartAPI.cartAPI.cartOfUser(user.getId()).enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(Call<List<Cart>> call, Response<List<Cart>> response) {
                if (response.isSuccessful()) {
                listCart = response.body();
                if (listCart.isEmpty()){
                    clCartIsEmpty.setVisibility(View.VISIBLE);
                    clCart.setVisibility(View.GONE);
                }
                else{
//                Log.e("++==", String.valueOf(listCart));

                    cartAdapter = new CartAdapter(cartItemInterface, listCart, CartActivity.this);
                    recyclerViewCart.setAdapter(cartAdapter);

                    totalCalls = listCart.size();
                    completedCalls = 0;
                    for(Cart y: listCart){
                        //Total += y.getCount() * y.getProduct().getPrice();
                        if(y.getProduct()!=null){
                        int priceProduct = y.getProduct().getPrice();
                        int count = y.getCount();
                        PromotionAPI.promotionAPI.checkProDuctInPromotion(y.getProduct().getId()).enqueue(new Callback<Promotion>() {
                            @Override
                            public void onResponse(Call<Promotion> call, Response<Promotion> response) {
                                Promotion promotion = response.body();
                                if(promotion != null){
                                    Total += count * (priceProduct - priceProduct * promotion.getDiscountPercent());
                                }
                                else{
                                    Total += count * priceProduct;
                                }
                                completedCalls++;
                                if (completedCalls == totalCalls) {
                                    updateTotalPrice(Total);
                                }
                            }

                            @Override
                            public void onFailure(Call<Promotion> call, Throwable t) {
                                Log.e("====", "Call API check product in promotion fail");
                            }
                        });
                    }
                    else{
                        showConfirmationDialog();
                    }
                    }

                }}
                else{
                    showConfirmationDialog();
                }
            }
            @Override
            public void onFailure(Call<List<Cart>> call, Throwable t) {
                Log.e("====", "Call API Cart of user fail");
            }
        });
    }
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        builder.setTitle("Error cart page");
        builder.setMessage("Đã xảy ra sự cố cart. Quay lại trang chủ!!!");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
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

    private void updateTotalPrice(int total) {
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);
        tvTotalPrice.setText(en.format(Total));
    }

    private void setControl() {
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        ivHome = findViewById(R.id.ivHome);
        ivUser = findViewById(R.id.ivUser);
        ivCart = findViewById(R.id.ivCart);
        ivHistory = findViewById(R.id.ivHistory);
        clCart = findViewById(R.id.clCart);
        clCartIsEmpty = findViewById(R.id.clCartIsEmpty);
        btnCheckout = findViewById(R.id.btnCheckout);
    }

    @Override
    public void onClickUpdatePrice(int price) {
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);
        int total = Integer.parseInt(tvTotalPrice.getText().toString().replace(",",""));
        total += price;
        if (total ==0){
            clCartIsEmpty.setVisibility(View.VISIBLE);
            clCart.setVisibility(View.GONE);
        }
        tvTotalPrice.setText(en.format(total));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
