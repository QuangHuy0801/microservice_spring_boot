package com.example.doan_nhom_6.Adapter;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan_nhom_6.Activity.ShowDetailActivity;
import com.example.doan_nhom_6.Interface.CartItemInterface;
import com.example.doan_nhom_6.Model.Cart;
import com.example.doan_nhom_6.Model.Promotion;
import com.example.doan_nhom_6.Model.User;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.CartAPI;
import com.example.doan_nhom_6.Retrofit.PromotionAPI;
import com.example.doan_nhom_6.Somethings.ObjectSharedPreferences;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{

    final CartItemInterface cartItemInterface;
    List<Cart> carts;
    Context context;

    public CartAdapter(CartItemInterface cartItemInterface, List<Cart> carts, Context context) {
        this.cartItemInterface = cartItemInterface;
        this.carts = carts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cart = carts.get(position);
        if(cart.getProduct()!=null){
        User user = ObjectSharedPreferences.getSavedObjectFromPreference(context, "User", "MODE_PRIVATE", User.class);
        holder.tvCount.setText(String.valueOf(carts.get(position).getCount()));
        holder.tvProductName.setText(cart.getProduct().getProduct_Name());
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);

        Glide.with(holder.itemView.getContext())
                .load(cart.getProduct().getProductImage().get(0).getUrl_Image())
                .into(holder.ivImage);

        PromotionAPI.promotionAPI.checkProDuctInPromotion(cart.getProduct().getId()).enqueue(new Callback<Promotion>() {
            @Override
            public void onResponse(Call<Promotion> call, Response<Promotion> response) {
                Promotion promotion = response.body();
                if(promotion != null){
                    holder.tvPriceDiscount.setVisibility(View.VISIBLE);
                    holder.tvPriceDiscount.setPaintFlags(holder.tvPriceDiscount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.tvPriceDiscount.getPaint().setFakeBoldText(true);
                    holder.tvPriceDiscount.setTextColor(Color.BLACK);
                    holder.tvPriceDiscount.setText(en.format(cart.getProduct().getPrice()));
                    double Price = cart.getProduct().getPrice() - (cart.getProduct().getPrice() * promotion.getDiscountPercent());
                    holder.tvPrice.setText(en.format(Price));
                    holder.tvTotalPrice.setText(en.format(Price * cart.getCount()));
                }else{
                    holder.tvPriceDiscount.setVisibility(View.INVISIBLE);
                    holder.tvPrice.setText(en.format(cart.getProduct().getPrice()));
                    holder.tvTotalPrice.setText(en.format(cart.getProduct().getPrice() * cart.getCount()));
                }
            }

            @Override
            public void onFailure(Call<Promotion> call, Throwable t) {
                Toast.makeText(context.getApplicationContext(), "Call API check product in promotion fail", Toast.LENGTH_SHORT).show();
            }
        });

//        int Total= 0;
//        for(Cart y: carts){
//            Total += y.getCount()*y.getProduct().getPrice();
//        }
//        CartActivity.tvTotal.setText(en.format(Total));
        holder.layout_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int price = parseInt(holder.tvTotalPrice.getText().toString().replace(",","")) * (-1);
                CartAPI.cartAPI.deleteCart(cart.getId(), user.getId()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()){
                            cartItemInterface.onClickUpdatePrice(price);
                            notifyItemRemoved(holder.getAdapterPosition());
                            carts.remove(cart);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("====", t.getMessage());
                    }
                });
            }

        });

        holder.ivImage.setOnClickListener(v ->{
            Intent intent = new Intent(holder.itemView.getContext(), ShowDetailActivity.class);
            intent.putExtra("product", cart.getProduct());
            holder.itemView.getContext().startActivity(intent);
        });

        holder.ivPlus.setOnClickListener(v -> {
            if (cart.getCount()<cart.getProduct().getQuantity()){
                cart.setCount(cart.getCount()+1);
                CartAPI.cartAPI.addToCart(user.getId(), cart.getProduct().getId(), 1).enqueue(new Callback<Cart>() {
                    @Override
                    public void onResponse(Call<Cart> call, Response<Cart> response) {
                        int price = parseInt(holder.tvPrice.getText().toString().replace(",",""));
                        holder.tvCount.setText(parseInt(holder.tvCount.getText().toString())+1+"");
                        holder.tvTotalPrice.setText(en.format(price * parseInt(holder.tvCount.getText().toString())));
                        cartItemInterface.onClickUpdatePrice(price);
                    }
                    @Override
                    public void onFailure(Call<Cart> call, Throwable t) {
                        Log.e("++++","update cart fail");
                    }
                });
            }
        });

        holder.ivMinus.setOnClickListener(v -> {
            if (cart.getCount()>1){
                cart.setCount(cart.getCount()-1);
                CartAPI.cartAPI.addToCart(user.getId(), cart.getProduct().getId(), -1).enqueue(new Callback<Cart>() {
                    @Override
                    public void onResponse(Call<Cart> call, Response<Cart> response) {
                        int price = parseInt(holder.tvPrice.getText().toString().replace(",",""));
                        holder.tvCount.setText(parseInt(holder.tvCount.getText().toString())-1+"");
                        holder.tvTotalPrice.setText(en.format(price * parseInt(holder.tvCount.getText().toString())));
                        cartItemInterface.onClickUpdatePrice(price*-1);
                    }
                    @Override
                    public void onFailure(Call<Cart> call, Throwable t) {
                        Log.e("++++","update cart fail");
                    }
                });
            }
        });
    } else {
        // Nếu product là null, đặt cart adapter là rỗng
        holder.itemView.setVisibility(View.GONE);
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
    }
    }


    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTotalPrice, tvPrice, tvProductName, tvCount, tvPriceDiscount;
        ImageView ivImage, ivPlus, ivMinus;
        ConstraintLayout layout_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout_delete = itemView.findViewById(R.id.layout_delete);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvCount = itemView.findViewById(R.id.tvCount);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivPlus = itemView.findViewById(R.id.ivPlus);
            ivMinus = itemView.findViewById(R.id.ivMinus);
            tvPriceDiscount = itemView.findViewById(R.id.tvPriceDiscountCart);
        }
    }
}
