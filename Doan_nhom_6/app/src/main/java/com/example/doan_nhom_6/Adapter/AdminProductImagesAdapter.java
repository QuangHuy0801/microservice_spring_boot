package com.example.doan_nhom_6.Adapter;

import android.content.Context;
import android.net.Uri;
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
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.ProductAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminProductImagesAdapter extends RecyclerView.Adapter<AdminProductImagesAdapter.ViewHolder> {
    private List<Uri> imageList;
    private Context context;
    private OnDeleteClickListener onDeleteClickListener;

    public AdminProductImagesAdapter(Context context, List<Uri> imageList) {
        this.context = context;
        this.imageList = imageList;
    }
    public void removeItem(int position) {
        imageList.remove(position);
        notifyItemRemoved(position);
    }
    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_image_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Uri imageUri = imageList.get(position);
        Glide.with(context).load(imageUri).into(holder.imageView);
        int position1=position+1;
        holder.tvPos.setText("Image: " + position1);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageList.remove(position);
                notifyItemRemoved(holder.getAdapterPosition());
                Toast.makeText(context, "Xóa thành công...!", Toast.LENGTH_SHORT).show();}


        });

    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,btnDelete;
        TextView tvPos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.IvProductImage);
            tvPos = itemView.findViewById(R.id.tvPos);
            btnDelete =itemView.findViewById(R.id.btnDelete1);

        }
    }
}
