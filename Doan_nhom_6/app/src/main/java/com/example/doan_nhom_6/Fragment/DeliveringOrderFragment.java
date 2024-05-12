package com.example.doan_nhom_6.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_nhom_6.Adapter.AdminOrderAdapter;
import com.example.doan_nhom_6.Interface.UpdateStatusInterface;
import com.example.doan_nhom_6.Model.Order;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.OrderAPI;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveringOrderFragment extends Fragment implements UpdateStatusInterface{
    RecyclerView rvDeliveringOrder;
    AdminOrderAdapter adminOrderAdapter;
    List<Order> deliveringOrderList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivering_order, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnhXa();
        LoadData();
    }

    private void LoadData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvDeliveringOrder.setLayoutManager(linearLayoutManager);
        UpdateStatusInterface updateStatusInterface = this;
        OrderAPI.orderAPI.getOrderByStatus("Delivering").enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if(response.isSuccessful()){
                deliveringOrderList = response.body();
                adminOrderAdapter = new AdminOrderAdapter(deliveringOrderList, getContext().getApplicationContext(), updateStatusInterface);
                rvDeliveringOrder.setAdapter(adminOrderAdapter);}
                else{
                    Toast.makeText(getContext(), "Đã có lỗi ở order service", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(getContext(), "Đã có lỗi ở order service", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AnhXa() {
        rvDeliveringOrder = getView().findViewById(R.id.rvDeliveringOrder);
    }

    public void ReloadDataOnTabLayoutChaged() {
        LoadData();
    }

    @Override
    public void ReloadData() {
        LoadData();
    }
}