package com.example.doan_nhom_6.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_nhom_6.Activity.MainActivity;
import com.example.doan_nhom_6.Activity.OrderActivity;
import com.example.doan_nhom_6.Adapter.OrderAdapter;
import com.example.doan_nhom_6.Model.Order;
import com.example.doan_nhom_6.Model.User;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.OrderAPI;
import com.example.doan_nhom_6.Somethings.ObjectSharedPreferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllOrderFragment extends Fragment {

    RecyclerView rvAllOrder;
    OrderAdapter orderAdapter;
    List<Order> orderList;

    public AllOrderFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AnhXa();
        LoadData();
    }

    private void LoadData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvAllOrder.setLayoutManager(linearLayoutManager);
        User user = ObjectSharedPreferences.getSavedObjectFromPreference(getContext(), "User", "MODE_PRIVATE", User.class);
        OrderAPI.orderAPI.getOrderByUserId(user.getId()).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if(response.isSuccessful()){
                orderList = response.body();
                orderAdapter = new OrderAdapter(orderList, getContext().getApplicationContext());
                rvAllOrder.setAdapter(orderAdapter);}
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {

            }
        });
    }

    private void AnhXa() {
        rvAllOrder = getView().findViewById(R.id.rvAllOrder);
    }


}