package com.example.doan_nhom_6.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.doan_nhom_6.Activity.ShowDetailActivity;
import com.example.doan_nhom_6.Activity.ShowPromotionDetailActivity;
import com.example.doan_nhom_6.Model.Category;
import com.example.doan_nhom_6.Model.Product;
import com.example.doan_nhom_6.Model.Promotion;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.CategoryAPI;
import com.example.doan_nhom_6.Retrofit.PromotionAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPromotionAdapter extends RecyclerView.Adapter<AdminPromotionAdapter.ViewHolder>{
    List<Promotion> promotions;
    Context context;

    public interface DialogListener {
        void onOpenDialogEdit(int pos, Promotion promotion);
    }
    private AdminPromotionAdapter.DialogListener mListener;

    public AdminPromotionAdapter(List<Promotion> promotions, Context context, AdminPromotionAdapter.DialogListener listener) {
        this.promotions = promotions;
        this.context = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_admin_promotion, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Promotion promotion = promotions.get(holder.getAdapterPosition());
        holder.tvPromoName.setText(promotion.getName());
        holder.tvDescription.setText(promotion.getDescription());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa khuyến mãi này không?");
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PromotionAPI.promotionAPI.DeletePromotion(promotion.getId()).enqueue(new Callback<Promotion>() {
                            @Override
                            public void onResponse(Call<Promotion> call, Response<Promotion> response) {
                                if(response.isSuccessful()){
                                    promotions.remove(promotions.get(holder.getAdapterPosition()));
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    Toast.makeText(context, "Xóa thành công...!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Xóa không thành công...?", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Promotion> call, Throwable t) {
                                Toast.makeText(context, "Call API delete promotion fail!", Toast.LENGTH_SHORT).show();
                            }
                        });
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
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onOpenDialogEdit(holder.getAdapterPosition(), promotions.get(holder.getAdapterPosition()));
                }
            }
        });

        holder.ivHotSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ShowPromotionDetailActivity.class);
                intent.putExtra("promotion", promotion);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return promotions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvPromoName, tvDescription;
        ImageView btnEdit, btnDelete, ivHotSale;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPromoName = itemView.findViewById(R.id.tvPromoName);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            ivHotSale = itemView.findViewById(R.id.ivHotSale);
        }
    }
}
