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
import com.example.doan_nhom_6.Model.Product;
import com.example.doan_nhom_6.Model.Promotion_Item;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.PromotionAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductPromotionAdapter extends RecyclerView.Adapter<ProductPromotionAdapter.ViewHolder>{
    List<Promotion_Item> promotion_items;
    Context context;

    public ProductPromotionAdapter(List<Promotion_Item> promotion_items, Context context) {
        this.promotion_items = promotion_items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_admin_product, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.btnEdit.setVisibility(View.GONE);
        Product product = promotion_items.get(position).getProduct();
        holder.productName.setText(product.getProduct_Name());
        holder.productQuantity.setText("Số lượng: "+String.valueOf(product.getQuantity()));
        holder.productPrice.setText("Giá: "+String.valueOf(product.getPrice()));
        Glide.with(holder.itemView.getContext())
                .load(product.getProductImage().get(0).getUrl_Image())
                .into(holder.productPic);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromotionAPI.promotionAPI.DeleteProductInPromotion(promotion_items.get(holder.getAdapterPosition()).getId()).enqueue(new Callback<Promotion_Item>() {
                    @Override
                    public void onResponse(Call<Promotion_Item> call, Response<Promotion_Item> response) {
                        if (response.isSuccessful()) {
                            promotion_items.remove(promotion_items.get(holder.getAdapterPosition()));
                            notifyItemRemoved(holder.getAdapterPosition());
                            Toast.makeText(context, "Xóa thành công...!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Xóa không thành công...?", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Promotion_Item> call, Throwable t) {
                        Log.e("=====", t.getMessage());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return promotion_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView productName,productQuantity,productPrice;
        ImageView productPic, btnDelete, btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productPrice = itemView.findViewById(R.id.productPrice);
            productPic = itemView.findViewById(R.id.productPic);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}
