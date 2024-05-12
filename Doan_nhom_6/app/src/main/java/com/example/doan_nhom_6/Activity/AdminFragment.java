package com.example.doan_nhom_6.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.doan_nhom_6.Model.Order;
import com.example.doan_nhom_6.Model.User;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.AdminAPI;
import com.example.doan_nhom_6.Retrofit.UserAPI;
import com.example.doan_nhom_6.Somethings.ObjectSharedPreferences;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminFragment extends Fragment {

    ImageView ivAvatar;
    Button btnEditProfile, btnLogout;
    TextView tvFullName, tvId,tvChangePassword, tvEmail, tvPhone, tvAddress;
    User admin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin, container, false);
        setControl(view);
        setEvent();
        return view;
    }

    private void setEvent() {
        btnLogoutClick();
        tvChangePasswordClick();
        btnEditProfileClick();
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadData();
    }

    private void btnEditProfileClick() {
        btnEditProfile.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), EditProfileAdminActivity.class));
        });
    }

    private void tvChangePasswordClick() {
        tvChangePassword.setOnClickListener(v -> {
            Dialog dialog = new Dialog(requireContext());
            dialog.setContentView(R.layout.dialog_change_password);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.show();
            // Anh xa dialog
            ConstraintLayout clChangePassword = dialog.findViewById(R.id.clChangePassword);
            ConstraintLayout clChangePasswordSuccess = dialog.findViewById(R.id.clChangePasswordSuccess);
            EditText etOldPassword = dialog.findViewById(R.id.etOldPassword);
            EditText etNewPassword = dialog.findViewById(R.id.etNewPassword);
            EditText etReNewPassword = dialog.findViewById(R.id.etReNewPassword);
            TextView tvErrorChangePassword = dialog.findViewById(R.id.tvErrorChangePassword);
            Button btnChangePassword = dialog.findViewById(R.id.btnChangePassword);
            Button btnOK = dialog.findViewById(R.id.btnOk);
            // ====
            btnChangePassword.setOnClickListener(v1 -> {
                String password = etOldPassword.getText().toString();
                String newPassword = etNewPassword.getText().toString();
                String reNewPassword = etReNewPassword.getText().toString();
                admin = ObjectSharedPreferences.getSavedObjectFromPreference(requireContext(), "Admin", "MODE_PRIVATE", User.class);
                if (password.equals(admin.getPassword())) {
                    if (newPassword.equals(reNewPassword)) {
                        AdminAPI.adminApi.changePassword(admin.getId(), newPassword).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String pass = response.body();
                                if (pass != null) {
                                    clChangePassword.setVisibility(View.GONE);
                                    clChangePasswordSuccess.setVisibility(View.VISIBLE);
                                    admin.setPassword(pass);
                                    ObjectSharedPreferences.saveObjectToSharedPreference(requireContext(), "Admin", "MODE_PRIVATE", admin);
                                    btnOK.setOnClickListener(v2 -> {
                                        dialog.dismiss();
                                    });
                                }

                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {

                            }
                        });
                    } else {
                        tvErrorChangePassword.setText("New password and confirm password do not match!");
                    }
                } else {
                    tvErrorChangePassword.setText("Your password is not correct");
                }
            });
        });
    }

    private void btnLogoutClick() {
        btnLogout.setOnClickListener(v -> {
            admin = ObjectSharedPreferences.getSavedObjectFromPreference(requireContext(), "Admin", "MODE_PRIVATE", User.class);
                ObjectSharedPreferences.saveObjectToSharedPreference(requireContext(), "Admin", "MODE_PRIVATE", null);
                startActivity(new Intent(requireContext(), LoginDashboardActivity.class));
        });
    }

    private void LoadData() {
        admin = ObjectSharedPreferences.getSavedObjectFromPreference(requireContext(), "Admin", "MODE_PRIVATE", User.class);
        AdminAPI.adminApi.Login(admin.getId(), admin.getPassword()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                admin = response.body();
                ObjectSharedPreferences.saveObjectToSharedPreference(requireContext(), "Admin", "MODE_PRIVATE", admin);
                Glide.with(requireContext()).load(admin.getAvatar()).into(ivAvatar);
                tvFullName.setText(admin.getUser_Name());
                tvId.setText(admin.getId());
                Locale localeEN = new Locale("en", "EN");
                NumberFormat en = NumberFormat.getInstance(localeEN);
                tvPhone.setText(admin.getPhone_Number());
                tvAddress.setText(admin.getAddress());
                tvEmail.setText(admin.getEmail());}
                else{
                Toast.makeText(getContext(), "Đã có lỗi ở user service", Toast.LENGTH_LONG).show();
                    admin = ObjectSharedPreferences.getSavedObjectFromPreference(requireContext(), "Admin", "MODE_PRIVATE", User.class);
                    ObjectSharedPreferences.saveObjectToSharedPreference(requireContext(), "Admin", "MODE_PRIVATE", null);
                    startActivity(new Intent(requireContext(), LoginDashboardActivity.class));
            }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getContext(), "Đã có lỗi ở user service", Toast.LENGTH_LONG).show();
                admin = ObjectSharedPreferences.getSavedObjectFromPreference(requireContext(), "Admin", "MODE_PRIVATE", User.class);
                ObjectSharedPreferences.saveObjectToSharedPreference(requireContext(), "Admin", "MODE_PRIVATE", null);
                startActivity(new Intent(requireContext(), LoginDashboardActivity.class));
            }
        });
    }



    private void setControl(View view) {
        tvFullName = view.findViewById(R.id.tvFullName);
        tvId = view.findViewById(R.id.tvId);
        tvChangePassword = view.findViewById(R.id.tvChangePassword);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvAddress = view.findViewById(R.id.tvAddress);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnLogout = view.findViewById(R.id.btnLogout);
        ivAvatar = view.findViewById(R.id.ivAvatar);
    }
}