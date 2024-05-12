package com.example.doan_nhom_6.Adapter;

import static com.example.doan_nhom_6.R.id.tvOderId;
import static com.example.doan_nhom_6.R.id.tvPhoneNumber;
import static com.example.doan_nhom_6.R.id.tvTrangThai;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_nhom_6.Interface.UpdateStatusInterface;
import com.example.doan_nhom_6.Model.Order;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.OrderAPI;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.ViewHolder>{
    List<Order> orders;
    Context context;
    final UpdateStatusInterface updateStatusInterface;
    public AdminOrderAdapter(List<Order> orders, Context context, UpdateStatusInterface updateStatusInterface) {
        this.orders = orders;
        this.context = context;
        this.updateStatusInterface = updateStatusInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_admin_order, parent, false);
        return new AdminOrderAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.orderLayout.setBackground(context.getDrawable(R.drawable.order_item_background));
        Order order = orders.get(holder.getAdapterPosition());
        if (order.getId()<10)
            holder.tvOderId.setText("Order #0"+order.getId());
        else
            holder.tvOderId.setText("Order #"+order.getId());
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        holder.tvPurchaseDay.setText(simpleDateFormat.format(order.getBooking_Date()));
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);
        holder.tvTotalPrice.setText(en.format(order.getTotal()));
        holder.tvQuantity.setText(order.getOrder_Item().size()+"");
        holder.tvStatus.setText(order.getStatus());
        holder.tvPaymentMethod.setText(order.getPayment_Method());

        holder.itemView.setOnClickListener(v -> {
            if(holder.orderLayout2.getVisibility() == View.VISIBLE){
                holder.orderLayout.setBackground(context.getDrawable(R.drawable.order_item_background));
                holder.orderLayout1.setBackground(null);
                holder.orderLayout2.setVisibility(View.GONE);
                holder.ivHide.setVisibility(View.GONE);
                holder.ivShowMore.setVisibility(View.VISIBLE);
            }
            else{
                holder.orderLayout.setBackground(null);
                holder.orderLayout1.setBackground(context.getDrawable(R.drawable.order_item_background));
                holder.orderLayout2.setVisibility(View.VISIBLE);
                holder.ivHide.setVisibility(View.VISIBLE);
                holder.ivShowMore.setVisibility(View.GONE);
                if (order.getOrder_Item().size()>1)
                    holder.tvTotalItem.setText(order.getOrder_Item().size()+" Items");
                else
                    holder.tvTotalItem.setText(order.getOrder_Item().size()+" Item");
                holder.tvFullName.setText(order.getFullname());
                holder.tvPhoneNumber.setText(order.getPhone());
                holder.tvAddress.setText(order.getAddress());
                holder.orderLayout2.requestFocus();
            }

        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
        holder.recyclerViewOrderItem = holder.itemView.findViewById(R.id.recyclerViewOrderItemAdmin);
        holder.recyclerViewOrderItem.setLayoutManager(linearLayoutManager);
        holder.orderItemAdapter = new OrderItemAdapter(order.getOrder_Item(), context);
        holder.recyclerViewOrderItem.setAdapter(holder.orderItemAdapter);

        if(order.getStatus().equals("Canceled") || order.getStatus().equals("Completed")){
            holder.tvHuyDon.setVisibility(View.INVISIBLE);
            holder.tvTrangThai.setVisibility(View.INVISIBLE);
        }

        holder.tvHuyDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderAPI.orderAPI.updateStatus(order.getId(),"Canceled").enqueue(new Callback<Order>() {
                    @Override
                    public void onResponse(Call<Order> call, Response<Order> response) {
                        Order updateStatusOrder = response.body();
                        if(updateStatusOrder != null){
                            orders.set(holder.getAdapterPosition(), updateStatusOrder);
                            notifyItemChanged(holder.getAdapterPosition());
                            updateStatusInterface.ReloadData();
                            Toast.makeText(context, "Cập nhật trạng thái đơn hàng thành công...!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(context, "Cập nhật trạng thái đơn hàng thất bại...?", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Order> call, Throwable t) {
                        Log.e("====", "Call API Update Status Order fail");
                    }
                });
            }
        });
        if(order.getStatus().equals("Delivering")){
            holder.tvTrangThai.setText("Hoàn tất đơn hàng");
        }
        holder.tvTrangThai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (order.getStatus().equals("Pending")){
                    OrderAPI.orderAPI.updateStatus(order.getId(),"Delivering").enqueue(new Callback<Order>() {
                        @Override
                        public void onResponse(Call<Order> call, Response<Order> response) {
                            Order updateStatusOrder = response.body();
                            if(updateStatusOrder != null){
                                orders.set(holder.getAdapterPosition(), updateStatusOrder);
                                notifyItemChanged(holder.getAdapterPosition());
                                updateStatusInterface.ReloadData();
                                Toast.makeText(context, "Cập nhật trạng thái đơn hàng thành công...!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(context, "Cập nhật trạng thái đơn hàng thất bại...?", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Order> call, Throwable t) {
                            Log.e("====", "Call API Update Status Order fail");
                        }
                    });
                }
                else if(order.getStatus().equals("Delivering")){
                    OrderAPI.orderAPI.updateStatus(order.getId(),"Completed").enqueue(new Callback<Order>() {
                        @Override
                        public void onResponse(Call<Order> call, Response<Order> response) {
                            Order updateStatusOrder = response.body();
                            if(updateStatusOrder != null){
                                orders.set(holder.getAdapterPosition(), updateStatusOrder);
                                notifyItemChanged(holder.getAdapterPosition());
                                updateStatusInterface.ReloadData();
                                Toast.makeText(context, "Cập nhật trạng thái đơn hàng thành công...!", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(context, "Cập nhật trạng thái đơn hàng thất bại...?", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Order> call, Throwable t) {
                            Log.e("====", "Call API Update Status Order fail");
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvOderId, tvPurchaseDay, tvQuantity,tvTotalPrice, tvTotalItem, tvFullName, tvPhoneNumber, tvAddress, tvPaymentMethod, tvTrangThai, tvStatus, tvHuyDon;
        ImageView ivShowMore, ivHide;
        ConstraintLayout orderLayout, orderLayout1, orderLayout2;
        OrderItemAdapter orderItemAdapter;
        RecyclerView recyclerViewOrderItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOderId = itemView.findViewById(R.id.tvOderId);
            tvPurchaseDay = itemView.findViewById(R.id.tvPurchaseDay);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            ivShowMore = itemView.findViewById(R.id.ivShowMore);
            orderLayout = itemView.findViewById(R.id.orderLayout);
            ivHide = itemView.findViewById(R.id.ivHide);
            orderLayout1 = itemView.findViewById(R.id.orderLayout1);
            orderLayout2 = itemView.findViewById(R.id.orderLayout2);
            tvTotalItem = itemView.findViewById(R.id.tvTotalItem);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPaymentMethod = itemView.findViewById(R.id.tvPaymentMethod);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            tvHuyDon = itemView.findViewById(R.id.tvHuyDon);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
