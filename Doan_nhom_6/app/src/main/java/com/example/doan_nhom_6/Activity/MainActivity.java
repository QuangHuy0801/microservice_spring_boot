package com.example.doan_nhom_6.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.doan_nhom_6.Adapter.CategoryAdapter;
import com.example.doan_nhom_6.Adapter.ProductAdapter;
import com.example.doan_nhom_6.Model.Category;
import com.example.doan_nhom_6.Model.Product;
import com.example.doan_nhom_6.Model.User;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.CategoryAPI;
import com.example.doan_nhom_6.Retrofit.ProductAPI;
import com.example.doan_nhom_6.Somethings.ObjectSharedPreferences;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    private RecyclerView.Adapter adapter, adapter2, adapter3;

    private RecyclerView recyclerViewCategoryList, recyclerViewNewProductList, recyclerViewBestSellersList;
    TextView tvHiName;
    EditText etSearch;

    ImageView ivAvatar, ivHome, ivUser, ivCart, ivHistory, ivSearch,ivSearchMap;
    User user;

    //Api

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        setEvent();
    }

    private void setEvent() {
        appBarClick();
        ivSearchClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadUserInfor();
        LoadCategories();
        LoadNewProducts();
        LoadBestSellers();
    }

    private void ivSearchClick() {
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
                intent.putExtra("searchContent", etSearch.getText().toString());
                startActivity(intent);
            }
        });

        etSearch.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
                    intent.putExtra("searchContent", etSearch.getText().toString());
                    intent.putExtra("category_id", "-1");
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    private void appBarClick() {
        ivSearchMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            }
        });
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserActivity.class));
                finish();
            }
        });
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                finish();
            }
        });

        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OrderActivity.class));
                finish();
            }
        });
    }

    private void LoadBestSellers() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewBestSellersList = findViewById(R.id.view3);
        recyclerViewBestSellersList.setLayoutManager(linearLayoutManager);

        //GET API
        ProductAPI.productApi.getBestSellers().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> newProductsList = response.body();
                    adapter3 = new ProductAdapter(newProductsList, MainActivity.this);
                    recyclerViewBestSellersList.setAdapter(adapter3);
                }
                else{
                    recyclerViewCategoryList.setAdapter(null);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("====", "Call API Get Best Sellers fail");
            }
        });
    }

    private void LoadNewProducts() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerViewNewProductList = findViewById(R.id.view2);
        recyclerViewNewProductList.setLayoutManager(linearLayoutManager);

        //GET API
        ProductAPI.productApi.getNewProduct().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                List<Product> newProductsList = response.body();
                adapter2 = new ProductAdapter(newProductsList, MainActivity.this);
                recyclerViewNewProductList.setAdapter(adapter2);}
                else{
                    showConfirmationDialog();
                    recyclerViewCategoryList.setAdapter(null);
                    }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("++++", t.getMessage());
                Log.e("====", "Call API Get New Products fail");
            }
        });
    }
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Error main page");
        builder.setMessage("Đã xảy ra sự cố ở trang chủ!!!");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                AlertDialog alertDialog = (AlertDialog) dialog;
                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setTextColor(getResources().getColor(R.color.black));
            }
        });
        dialog.show();
    }

    private void LoadCategories() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategoryList = findViewById(R.id.view1);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);
        //Get API
        CategoryAPI.categoryAPI.GetAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    List<Category> categoriesList = response.body();
                    adapter = new CategoryAdapter(categoriesList, MainActivity.this);
                    recyclerViewCategoryList.setAdapter(adapter);
                }
                else{
                        recyclerViewCategoryList.setAdapter(null);
                    }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("====", "Call API Get Categories fail");

            }
        });
    }

    private void LoadUserInfor() {
        user = ObjectSharedPreferences.getSavedObjectFromPreference(MainActivity.this, "User", "MODE_PRIVATE", User.class);
        tvHiName.setText("Hi "+ user.getUser_Name());
        try {
            Glide.with(getApplicationContext()).load(user.getAvatar()).into(ivAvatar);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setControl() {
        tvHiName = findViewById(R.id.tvHiName);
        ivAvatar = findViewById(R.id.ivAvatar);
        ivHome = findViewById(R.id.ivHome);
        ivUser = findViewById(R.id.ivUser);
        ivCart = findViewById(R.id.ivCart);
        ivHistory = findViewById(R.id.ivHistory);
        etSearch = findViewById(R.id.etSearch);
        ivSearch = findViewById(R.id.ivSearch);
        ivSearchMap =findViewById(R.id.ivSearchMap);
    }
}