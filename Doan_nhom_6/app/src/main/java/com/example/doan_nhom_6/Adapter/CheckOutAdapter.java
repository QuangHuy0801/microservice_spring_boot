package com.example.doan_nhom_6.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan_nhom_6.Model.Cart;
import com.example.doan_nhom_6.Model.Promotion;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.PromotionAPI;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutAdapter extends RecyclerView.Adapter<CheckOutAdapter.ViewHolder>{
    List<Cart> listCart;
    Context context;

    public CheckOutAdapter(List<Cart> listCart, Context context) {
        this.listCart = listCart;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_checkout_item, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cart = listCart.get(position);
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);

        Glide.with(holder.itemView.getContext())
                .load(cart.getProduct().getProductImage().get(0).getUrl_Image())
                .into(holder.ivProductImage);

        holder.tvProductName.setText(cart.getProduct().getProduct_Name());
        holder.tvCount.setText(cart.getCount()+"");

        PromotionAPI.promotionAPI.checkProDuctInPromotion(cart.getProduct().getId()).enqueue(new Callback<Promotion>() {
            @Override
            public void onResponse(Call<Promotion> call, Response<Promotion> response) {
                Promotion promotion = response.body();
                if (promotion != null){
                    int PriceDiscount = cart.getProduct().getPrice() - (int)(cart.getProduct().getPrice()* promotion.getDiscountPercent());
                    holder.tvProductPrice.setText(en.format(PriceDiscount));
                    holder.tvTotalPrice.setText(en.format(cart.getCount() * PriceDiscount));
                }
                else {
                    holder.tvProductPrice.setText(en.format(cart.getProduct().getPrice()));
                    holder.tvTotalPrice.setText(en.format(cart.getCount() * cart.getProduct().getPrice()));
                }
            }

            @Override
            public void onFailure(Call<Promotion> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), "Call API check product in promotion fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvProductPrice, tvCount, tvTotalPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvCount = itemView.findViewById(R.id.tvCount);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
        }
    }
}
