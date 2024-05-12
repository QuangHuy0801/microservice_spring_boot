package com.example.doan_nhom_6.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.doan_nhom_6.Model.User;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Somethings.ObjectSharedPreferences;

public class IventoryFragment extends Fragment {

    CardView cvCategory, cvPromotion, cvUser, cvOrder, cvRevenue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_iventory, container, false);
        anhXa(view);
        cardViewClick();
        return view;
    }

    private void cardViewClick() {
        cvCategory.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), AdminCategoryActivity.class));
        });

        cvUser.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), GetAllUserActivity.class));
        });

        cvOrder.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), AdminProductsActivity.class));
        });

        cvPromotion.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), AdminPromotionActivity.class));
        });
    }

    private void anhXa(View view) {
        cvCategory = view.findViewById(R.id.cvCategory);
        cvUser = view.findViewById(R.id.cvUser);
        cvOrder = view.findViewById(R.id.cvOrder);
        cvRevenue = view.findViewById(R.id.cvRevenue);
        cvPromotion = view.findViewById(R.id.cvPromotion);
    }
}