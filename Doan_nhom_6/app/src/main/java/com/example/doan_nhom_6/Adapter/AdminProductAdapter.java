package com.example.doan_nhom_6.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan_nhom_6.Model.Product;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.ProductAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ViewHolder> {
    List<Product> products;
    Context context;

    public interface DialogListener {
        void onOpenDialogEdit(int pos, int productID,Product product);
    }
    private AdminProductAdapter.DialogListener mListener;

    public AdminProductAdapter(List<Product> productDomains, Context context, AdminProductAdapter.DialogListener listener) {
        this.products = productDomains;
        this.context = context;
        this.mListener = listener;
    }

    public void notifyDataChanged() {
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public AdminProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_admin_product, parent, false);
        return new AdminProductAdapter.ViewHolder(inflate);
    }
    private void deleteProduct(int position) {
        Product product = products.get(position);
//        if (product.getOrder_Item() == null || product.getOrder_Item().isEmpty())
//        {
            ProductAPI.productApi.DeleteProduct(product.getId()).enqueue(new Callback<Product>() {
                @Override
                public void onResponse(Call<Product> call, Response<Product> response) {
                    Log.e("====", response.message());
                    if (response.isSuccessful()) {
                        products.remove(product);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Xóa thành công...!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Xóa không thành công???", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Product> call, Throwable t) {
                    Log.e("====", t.getMessage());
                }
            });
//        } else {
//            Toast.makeText(context, "Không thể xóa sản phẩm đã có order item...!", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull AdminProductAdapter.ViewHolder holder, int position) {
        String id = String.valueOf(products.get(position).getId());
        Product product = products.get(holder.getAdapterPosition());
        holder.productName.setText(products.get(position).getProduct_Name());
        holder.productQuantity.setText("Số lượng: "+String.valueOf(products.get(position).getQuantity()));
        holder.productPrice.setText("Giá: "+String.valueOf(products.get(position).getPrice()));
        Glide.with(holder.itemView.getContext())
                .load(products.get(position).getProductImage().get(0).getUrl_Image())
                .into(holder.productPic);


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa");

                // Đổi màu cho tiêu đề
                int titleTextColor = context.getResources().getColor(android.R.color.black);
                builder.setTitle(Html.fromHtml("<font color='" + titleTextColor + "'>Xác nhận xóa</font>"));

                // Thiết lập nội dung
                builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm này?");

                // Thêm nút xác nhận
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteProduct(holder.getAdapterPosition());
                    }
                });

                // Thêm nút hủy bỏ
                builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                // Hiển thị hộp thoại AlertDialog
                AlertDialog dialog = builder.create();

                // Đổi màu cho nút xác nhận
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        AlertDialog alertDialog = (AlertDialog) dialogInterface;

                        // Nút xác nhận
                        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positiveButton.setTextColor(context.getResources().getColor(android.R.color.black));

                        // Nút hủy bỏ
                        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                        negativeButton.setTextColor(context.getResources().getColor(android.R.color.black));
                    }
                });

                dialog.show();
            }
        });



        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onOpenDialogEdit(holder.getAdapterPosition(), products.get(holder.getAdapterPosition()).getId(), products.get(holder.getAdapterPosition()));
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
