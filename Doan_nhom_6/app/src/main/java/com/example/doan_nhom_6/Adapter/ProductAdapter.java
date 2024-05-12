package com.example.doan_nhom_6.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan_nhom_6.Activity.ShowDetailActivity;
import com.example.doan_nhom_6.Model.Cart;
import com.example.doan_nhom_6.Model.Product;
import com.example.doan_nhom_6.Model.Promotion;
import com.example.doan_nhom_6.Model.User;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.CartAPI;
import com.example.doan_nhom_6.Retrofit.ProductAPI;
import com.example.doan_nhom_6.Retrofit.PromotionAPI;
import com.example.doan_nhom_6.Somethings.ObjectSharedPreferences;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    List<Product> Products;
    Context context;
    public ProductAdapter(List<Product> products, Context context) {
        this.Products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_products, parent, false);

        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = Products.get(position);
        holder.title.setText(Products.get(position).getProduct_Name());
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);
        //holder.fee.setText(en.format(Products.get(position).getPrice()));
        holder.fee1.setPaintFlags(holder.fee.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        Glide.with(holder.itemView.getContext())
                .load(Products.get(position).getProductImage().get(0).getUrl_Image())
                .into(holder.pic);

        holder.addBtn.setOnClickListener(v -> {
            User user = ObjectSharedPreferences.getSavedObjectFromPreference(context, "User", "MODE_PRIVATE", User.class);
            CartAPI.cartAPI.addToCart(user.getId(), product.getId(), 1).enqueue(new Callback<Cart>() {
                @Override
                public void onResponse(Call<Cart> call, Response<Cart> response) {
                    Cart cart = response.body();
                    if(cart !=null){
                        Toast.makeText(context.getApplicationContext(), "Thêm vào giỏ thành công", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(context.getApplicationContext(), "Thêm vào giỏ thất bại", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<Cart> call, Throwable t) {
                    Toast.makeText(context.getApplicationContext(), "Call API Add to cart fail", Toast.LENGTH_SHORT).show();
                }
            });
        });

        holder.itemView.setOnClickListener(v ->{
            Intent intent = new Intent(holder.itemView.getContext(), ShowDetailActivity.class);
            intent.putExtra("product", product);
            holder.itemView.getContext().startActivity(intent);
        });

        PromotionAPI.promotionAPI.checkProDuctInPromotion(product.getId()).enqueue(new Callback<Promotion>() {
            @Override
            public void onResponse(Call<Promotion> call, Response<Promotion> response) {
                Promotion promotion = response.body();
                if(promotion != null){
                    holder.tvPercent.setVisibility(View.VISIBLE);
                    holder.tvPercent.setText("-" + (int) (promotion.getDiscountPercent()*100) + "%");
                    holder.fee1.setVisibility(View.VISIBLE);
                    holder.fee1.setText(en.format(Products.get(position).getPrice()));
                    holder.fee1.setPaintFlags(holder.fee.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.fee1.getPaint().setFakeBoldText(true);
                    holder.fee1.setTextColor(Color.BLACK);
                    double favorablePrice = Products.get(position).getPrice() - (Products.get(position).getPrice() * promotion.getDiscountPercent());
                    holder.fee.setText(en.format(favorablePrice));
                }else{
                    holder.tvPercent.setVisibility(View.INVISIBLE);
                    holder.fee1.setVisibility(View.INVISIBLE);
                    holder.fee.setText(en.format(Products.get(position).getPrice()));
                }
            }

            @Override
            public void onFailure(Call<Promotion> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), "Call API Check Product in promotion fail", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return Products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, fee, fee1, tvPercent;
        ImageView pic;
        ImageView addBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            pic = itemView.findViewById(R.id.ivImage);
            fee = itemView.findViewById(R.id.fee);
            fee1 = itemView.findViewById(R.id.fee1);
            addBtn = itemView.findViewById(R.id.addBtn);
            tvPercent = itemView.findViewById(R.id.tvPercent);
        }
    }
}
