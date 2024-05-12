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
import com.example.doan_nhom_6.Model.User;
import com.example.doan_nhom_6.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.ViewHolder>{
    List<User> users;
    Context context;

    public interface DialogListener {
        void onOpenDialogEdit(int pos, String userID);
    }
    private AdminUserAdapter.DialogListener mListener;

    public AdminUserAdapter(List<User> userDomains, Context context, DialogListener listener) {
        this.users = userDomains;
        this.context = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public AdminUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_admin_user, parent, false);
        return new AdminUserAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminUserAdapter.ViewHolder holder, int position) {
        String id = String.valueOf(users.get(position).getId());
//        User user = users.get(holder.getAdapterPosition());
        holder.userName.setText(users.get(position).getUser_Name());
        holder.userPhone.setText(users.get(position).getPhone_Number());
        Glide.with(context)
                .load(users.get(position).getAvatar())
                .into(holder.userPic);



        holder.btnSee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onOpenDialogEdit(holder.getAdapterPosition(), users.get(holder.getAdapterPosition()).getId());
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
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName,userPhone;
        ImageView userPic, btnSee;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userPhone = itemView.findViewById(R.id.userPhone);
            userPic = itemView.findViewById(R.id.userPic);
            btnSee = itemView.findViewById(R.id.btnSee);
        }
    }
}
