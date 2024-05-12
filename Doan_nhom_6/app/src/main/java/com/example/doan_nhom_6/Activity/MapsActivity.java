package com.example.doan_nhom_6.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.doan_nhom_6.Fragment.MapsFragment;
import com.example.doan_nhom_6.R;

public class MapsActivity extends AppCompatActivity {
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setControl();
        setEvent();
    }

    private void setEvent() {
        ivBackClick();
        mapControl();
    }

    private void setControl() {
        ivBack = findViewById(R.id.ivBack);

    }

    private void ivBackClick() {
        ivBack.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }

    private void mapControl() {
        MapsFragment mapsFragment = new MapsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, mapsFragment);
        fragmentTransaction.commit();
    }
}