package com.example.doan_nhom_6.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan_nhom_6.Adapter.AddProductPromotionAdapter;
import com.example.doan_nhom_6.Adapter.AdminCategoryAdapter;
import com.example.doan_nhom_6.Adapter.AdminProductAdapter;
import com.example.doan_nhom_6.Adapter.ProductPromotionAdapter;
import com.example.doan_nhom_6.Model.Product;
import com.example.doan_nhom_6.Model.Promotion;
import com.example.doan_nhom_6.Model.Promotion_Item;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.ProductAPI;
import com.example.doan_nhom_6.Retrofit.PromotionAPI;

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowPromotionDetailActivity extends AppCompatActivity implements AddProductPromotionAdapter.OnItemSelectedListener
{
    TextView tvName, tvDescription, tvStartDate, tvEndDate, tvDiscount, tvStatus;
    RecyclerView rcvProduct, rcvAddProduct;
    ImageView ivBack;
    Button btnThem, btnAddProduct;
    Promotion promotion;
    ProductPromotionAdapter adapter;
    AddProductPromotionAdapter adapter1;
    List<Promotion_Item> promotion_items;
    List<Product> products;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_promotion_detail);
        promotion = (Promotion) getIntent().getSerializableExtra("promotion");
        AnhXa();
        LoadData();
        btnThemClick();
        ivBackClick();
    }

    private void ivBackClick() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void btnThemClick() {
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductAPI.productApi.getProductNotInPromotion().enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        products = response.body();
                        adapter1 = new AddProductPromotionAdapter(products, ShowPromotionDetailActivity.this, ShowPromotionDetailActivity.this);
                        rcvAddProduct.setHasFixedSize(true);
                        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
                        rcvAddProduct.setLayoutManager(layoutManager);
                        rcvAddProduct.setAdapter(adapter1);
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        Log.e("=====", t.getMessage());
                    }
                });
                dialog.show();
            }
        });
    }

    private void LoadData() {
        tvName.setText(promotion.getName());
        tvDescription.setText(promotion.getDescription());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        tvStartDate.setText(format.format(promotion.getStartDate()));
        tvEndDate.setText(format.format(promotion.getEndDate()));
        tvDiscount.setText(String.valueOf((int)(promotion.getDiscountPercent() * 100)));
        if(promotion.getStatus().equals("Active")){
            tvStatus.setText("Active");
        }
        else{
            tvStatus.setText("Inactive");
        }

        promotion_items = promotion.getPromotion_Item();
        adapter = new ProductPromotionAdapter(promotion_items, ShowPromotionDetailActivity.this);
        rcvProduct.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
        rcvProduct.setLayoutManager(layoutManager);
        rcvProduct.setAdapter(adapter);

    }

    private void AnhXa() {
        tvName = findViewById(R.id.tvPromoName);
        tvDescription = findViewById(R.id.tvPromoDescription);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        tvDiscount = findViewById(R.id.tvPromoDiscount);
        tvStatus = findViewById(R.id.tvPromoStatus);
        rcvProduct = findViewById(R.id.rcvProductPromo);
        btnThem = findViewById(R.id.btnThemSanPham);
        ivBack = findViewById(R.id.ivBack);
        dialog = new Dialog(ShowPromotionDetailActivity.this);
        dialog.setContentView(R.layout.dialog_add_product_in_promotion);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        rcvAddProduct = dialog.findViewById(R.id.rcvAddProduct);
        btnAddProduct = dialog.findViewById(R.id.btnAddProduct);
    }

    @Override
    public void onItemSelected(List<Product> selectedProducts) {
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromotionAPI.promotionAPI.addProductInPromotion(promotion.getId(), selectedProducts).enqueue(new Callback<Promotion>() {
                    @Override
                    public void onResponse(Call<Promotion> call, Response<Promotion> response) {
                        Promotion promotion1 = response.body();
                        if (promotion1 != null){
                            promotion_items.clear();
                            promotion_items.addAll(promotion1.getPromotion_Item());
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                            Toast.makeText(ShowPromotionDetailActivity.this, "Thêm sản phẩm thành công...", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(ShowPromotionDetailActivity.this, "Thêm sản phẩm thất bại...", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Promotion> call, Throwable t) {
                        Log.e("++++", "Call API Add Product In Promotion Fail..." );
                    }
                });
            }
        });
    }
}