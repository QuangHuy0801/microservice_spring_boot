package com.example.doan_nhom_6.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan_nhom_6.Model.Product;
import com.example.doan_nhom_6.R;

import java.util.ArrayList;
import java.util.List;

public class AddProductPromotionAdapter extends RecyclerView.Adapter<AddProductPromotionAdapter.ViewHolder>{
    List<Product> products;
    Context context;
    List<Product> selectedProducts = new ArrayList<>();

    public interface OnItemSelectedListener {
        void onItemSelected(List<Product> selectedProducts);
    }
    private OnItemSelectedListener mListener;

    public AddProductPromotionAdapter(List<Product> products, Context context, OnItemSelectedListener listener) {
        this.products = products;
        this.context = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_add_product_in_promotion, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);
        holder.productName.setText(product.getProduct_Name());
        holder.productQuantity.setText("Số lượng: "+String.valueOf(product.getQuantity()));
        holder.productPrice.setText("Giá: "+String.valueOf(product.getPrice()));
        Glide.with(holder.itemView.getContext())
                .load(product.getProductImage().get(0).getUrl_Image())
                .into(holder.productPic);

        holder.cbProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.cbProduct.isChecked()){
                    selectedProducts.add(product);
                }
                else{
                    selectedProducts.remove(product);
                }
            }
        });
        mListener.onItemSelected(selectedProducts);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView productName,productQuantity,productPrice;
        ImageView productPic;
        CheckBox cbProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productPrice = itemView.findViewById(R.id.productPrice);
            productPic = itemView.findViewById(R.id.productPic);
            cbProduct = itemView.findViewById(R.id.cbProduct);
        }
    }
}
