package com.example.doan_nhom_6.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan_nhom_6.Adapter.AdminCategoryAdapter;
import com.example.doan_nhom_6.Adapter.AdminUserAdapter;
import com.example.doan_nhom_6.Model.Category;
import com.example.doan_nhom_6.Model.User;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.CategoryAPI;
import com.example.doan_nhom_6.Retrofit.UserAPI;
import com.example.doan_nhom_6.Somethings.RealPathUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAllUserActivity extends AppCompatActivity implements AdminUserAdapter.DialogListener{

    private RecyclerView rcvUser;
    List<User> userList;
    AdminUserAdapter adapter;
    ImageView ivBack;
    TextView tvUserName,tvUserEmail,tvUserPhone,tvUserAddress;
    Button btnClose;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_user);
        AnhXa();
        LoadUser();
        ivBackClick();
    }


    private void ivBackClick() {
        ivBack.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }

    private void LoadUser() {
        UserAPI.userApi.GetAllUser().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()){
                    userList = response.body();
                adapter = new AdminUserAdapter(userList, GetAllUserActivity.this, GetAllUserActivity.this);
                rcvUser.setHasFixedSize(true);
                GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
                rcvUser.setLayoutManager(layoutManager);
                rcvUser.setAdapter(adapter);
            }else{
                        Toast.makeText(GetAllUserActivity.this, "Đã có lỗi ở user service", Toast.LENGTH_LONG).show();
                        onBackPressed();
                        finish();
            }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("====", "Call API Get Categories fail");

            }
        });
    }

    private void AnhXa() {
        rcvUser = findViewById(R.id.rcvUsers);
        ivBack = findViewById(R.id.ivBack);
    }

    @Override
    public void onOpenDialogEdit(int pos, String userID) {
        User user = userList.get(pos);
        Dialog userInfoDialog = new Dialog(this);
        userInfoDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        userInfoDialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        userInfoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        userInfoDialog.getWindow().setGravity(Gravity.BOTTOM);
        userInfoDialog.setContentView(R.layout.dialog_user_info);
        tvUserName = userInfoDialog.findViewById(R.id.tvUserName);
        tvUserEmail = userInfoDialog.findViewById(R.id.tvUserEmail);
        tvUserPhone = userInfoDialog.findViewById(R.id.tvUserPhone);
        tvUserAddress = userInfoDialog.findViewById(R.id.tvUserAddress);
        tvUserName.setText(user.getUser_Name());
        tvUserEmail.setText(user.getEmail());
        tvUserPhone.setText(user.getPhone_Number());
        tvUserAddress.setText(user.getAddress());
        btnClose = userInfoDialog.findViewById(R.id.btnClose);
        // Thêm hành động để đóng dialog khi nút được nhấn
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfoDialog.dismiss(); // Đóng dialog
            }
        });
        userInfoDialog.show();

    }
}