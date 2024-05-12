package com.example.doan_nhom_6.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.doan_nhom_6.Model.Category;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.CategoryAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.ViewHolder> {
    List<Category> categories;
    Context context;

    public interface DialogListener {
        void onOpenDialogEdit(int pos, int categoryID);
    }
    private DialogListener mListener;

    public AdminCategoryAdapter(List<Category> categoryDomains, Context context, DialogListener listener) {
        this.categories = categoryDomains;
        this.context = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_admin_category, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String id = String.valueOf(categories.get(position).getId());
        Category category = categories.get(holder.getAdapterPosition());
        holder.categoryName.setText(categories.get(position).getCategory_Name());
        Glide.with(context)
                .load(categories.get(position).getCategory_Image())
                .into(holder.categoryPic);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa loại sản phẩm này?");
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (category.getProduct() == null || category.getProduct().isEmpty()) {
                            CategoryAPI.categoryAPI.DeleteCategory(category.getId()).enqueue(new Callback<Category>() {
                                @Override
                                public void onResponse(Call<Category> call, Response<Category> response) {
                                    Log.e("====", response.message());
                                    if (response.isSuccessful()) {
                                        categories.remove(category);
                                        notifyItemRemoved(holder.getAdapterPosition());
                                        Toast.makeText(context, "Xóa thành công...!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Xóa không thành công???", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Category> call, Throwable t) {
                                    Log.e("====", t.getMessage());
                                }
                            });
                        } else {
                            Toast.makeText(context, "Không thể xóa loại sản phẩm đã có sản phẩm...!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                // Get the AlertDialog instance
                AlertDialog dialog = builder.create();

                // Set color for positive and negative button text
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                        positiveButton.setTextColor(context.getResources().getColor(android.R.color.black));

                        Button negativeButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                        negativeButton.setTextColor(context.getResources().getColor(android.R.color.black));
                    }
                });

                // Show the AlertDialog
                dialog.show();
            }
        });


        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onOpenDialogEdit(holder.getAdapterPosition(), categories.get(holder.getAdapterPosition()).getId());
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
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ImageView categoryPic, btnDelete, btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.categoryName);
            categoryPic = itemView.findViewById(R.id.categoryPic);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
        }
    }
}
