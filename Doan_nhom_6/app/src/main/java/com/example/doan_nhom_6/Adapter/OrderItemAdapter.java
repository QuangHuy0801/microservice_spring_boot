package com.example.doan_nhom_6.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan_nhom_6.Model.Order_Item;
import com.example.doan_nhom_6.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder>{
    List<Order_Item> order_items;
    Context context;

    public OrderItemAdapter(List<Order_Item> order_items, Context context) {
        this.order_items = order_items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order_item, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order_Item order_item = order_items.get(position);
        if (order_item.getProduct()!=null) {
            Glide.with(holder.itemView.getContext())
                    .load(order_item.getProduct().getProductImage().get(0).getUrl_Image())
                    .into(holder.ivProductImage);
            holder.tvProductName.setText(order_item.getProduct().getProduct_Name());
            Locale localeEN = new Locale("en", "EN");
            NumberFormat en = NumberFormat.getInstance(localeEN);
            holder.tvPrice.setText(en.format(order_item.getProduct().getPrice()));
            holder.tvUnits.setText(order_item.getCount() + "");
            holder.tvTotalPrice.setText(en.format(order_item.getProduct().getPrice() * order_item.getCount()));
        }else{
            holder.ivProductImage.setVisibility(View.GONE);
            holder.tvProductName.setVisibility(View.GONE);
            holder.tvUnits.setVisibility(View.GONE);
            holder.tvPrice.setVisibility(View.GONE);
            holder.tvTotalPrice.setVisibility(View.GONE);
            Toast.makeText(context, "Có lỗi xảy ra với dữ liệu sản phẩm", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return  order_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName, tvUnits, tvPrice, tvTotalPrice;
        ImageView ivProductImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvUnits = itemView.findViewById(R.id.tvUnits);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
        }
    }
}
