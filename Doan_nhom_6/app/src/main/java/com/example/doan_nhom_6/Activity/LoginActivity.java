package com.example.doan_nhom_6.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.doan_nhom_6.Model.Address;
import com.example.doan_nhom_6.Model.User;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.UserAPI;
import com.example.doan_nhom_6.Somethings.ObjectSharedPreferences;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etPassword, etUserName;
    Button btnLogin;
    TextView tvRegister, tvForgotPassword, tvAdmin;
    User user = new User();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setControl();
        setEvent();
    }

    private void setEvent() {
        btnLoginClick();
        tvRegisterClick();
        tvForgotPasswordClick();
        tvAdminClick();
    }

    private void tvAdminClick() {
        tvAdmin.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, LoginDashboardActivity.class));
        });
    }




    private void tvForgotPasswordClick() {
        tvForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });

    }

    private void tvRegisterClick() {
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    private void btnLoginClick() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }

    private void Login() {
        if (TextUtils.isEmpty(etUserName.getText().toString())){
            etUserName.setError("Please enter your username");
            etUserName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())){
            etPassword.setError("Please enter your password");
            etPassword.requestFocus();
            return;
        }
        String username = etUserName.getText().toString();
        String password = etPassword.getText().toString();
//        Log.e("ffff", "1======"+username);
//        Log.e("ffff", "2======"+password);
        UserAPI.userApi.Login(username,password).enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
//                        User user = new User();
//                        user.setUser_Name("dmm");
                if(response.isSuccessful()){
                user = response.body();
                if (user!=null){
                    Toast.makeText(LoginActivity.this,"Login Successfully", Toast.LENGTH_LONG).show();
//                    Log.e("ffff", user.toString());
                    ObjectSharedPreferences.saveObjectToSharedPreference(LoginActivity.this, "User", "MODE_PRIVATE", user);
                    if(user.getAddress()!=null && user.getPhone_Number()!=null){
                        Address address = new Address(user.getUser_Name(), user.getPhone_Number(), user.getAddress());
                        ObjectSharedPreferences.saveObjectToSharedPreference(LoginActivity.this, "address", "MODE_PRIVATE", address);
                    }

                    Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("object", user);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this,"Incorrect UserName or Password", Toast.LENGTH_LONG).show();
                }
                Log.e("ffff", "Đăng nhập thành công");
            }else{
                    Toast.makeText(LoginActivity.this,"Error Server", Toast.LENGTH_LONG).show();
            }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Login failed", Toast.LENGTH_LONG).show();
                Log.e("ffff", "Kết nối API Login thất bại");
                Log.e("TAG", t.toString());
            }
        });
    }

    private void setControl() {
        btnLogin = findViewById(R.id.btnSignUp);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvAdmin = findViewById(R.id.tvAdmin);
        etPassword = findViewById(R.id.etPassword);
        etUserName = findViewById(R.id.etUserName);
    }
}
