package com.example.doan_nhom_6.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan_nhom_6.Model.Address;
import com.example.doan_nhom_6.Model.User;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.AdminAPI;
import com.example.doan_nhom_6.Somethings.ObjectSharedPreferences;

import retrofit2.Call;
import retrofit2.Response;

public class LoginDashboardActivity extends AppCompatActivity {
    EditText etPasswordAdmin, etUserNameAdmin;
    Button btnLoginAdmin;
    TextView tvForgotPasswordAdmin, tvUser;
    User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_dashboard);
        setControl();
        setEvent();
    }

    private void setEvent() {
        btnLoginClick();
        tvForgotPasswordAdminClick();
        tvUserClick();
        appBarClick();
    }

    private void appBarClick() {

    }

    private void tvUserClick() {
        tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginDashboardActivity.this, LoginActivity.class));
            }
        });
    }

    private void tvForgotPasswordAdminClick() {
        tvForgotPasswordAdmin.setOnClickListener(v -> {
            startActivity(new Intent(LoginDashboardActivity.this, ForgotPasswordAdminActivity.class));
        });
    }

    private void btnLoginClick() {
        btnLoginAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }

            private void Login() {
                if (TextUtils.isEmpty(etUserNameAdmin.getText().toString())){
                    etUserNameAdmin.setError("Please enter your username");
                    etUserNameAdmin.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(etPasswordAdmin.getText().toString())){
                    etPasswordAdmin.setError("Please enter your password");
                    etPasswordAdmin.requestFocus();
                    return;
                }
                String username = etUserNameAdmin.getText().toString();
                String password = etPasswordAdmin.getText().toString();
//        Log.e("ffff", "1======"+username);
//        Log.e("ffff", "2======"+password);
                AdminAPI.adminApi.Login(username,password).enqueue(new retrofit2.Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
//                        User user = new User();
//                        user.setUser_Name("dmm");
                        if(response.isSuccessful()){
                        user = response.body();
                        if (user!=null){
                            Toast.makeText(LoginDashboardActivity.this,"Login Successfully", Toast.LENGTH_LONG).show();
//                    Log.e("ffff", user.toString());
                            ObjectSharedPreferences.saveObjectToSharedPreference(LoginDashboardActivity.this, "Admin", "MODE_PRIVATE", user);
                            if(user.getAddress()!=null && user.getPhone_Number()!=null){
                                Address address = new Address(user.getUser_Name(), user.getPhone_Number(), user.getAddress());
                                ObjectSharedPreferences.saveObjectToSharedPreference(LoginDashboardActivity.this, "address", "MODE_PRIVATE", address);
                            }

                            Intent intent= new Intent(LoginDashboardActivity.this, MainDashboardActivity.class);
                            intent.putExtra("object", user);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(LoginDashboardActivity.this,"Incorrect UserName or Password", Toast.LENGTH_LONG).show();
                        }
                        Log.e("ffff", "Đăng nhập thành công");}
                        else{
                            Toast.makeText(LoginDashboardActivity.this,"Error Server", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(LoginDashboardActivity.this,"Login failed", Toast.LENGTH_LONG).show();
                        Log.e("ffff", "Kết nối API Login thất bại");
                        Log.e("TAG", t.toString());
                    }
                });
            }
        });
    }

    private void setControl() {
        btnLoginAdmin = findViewById(R.id.btnSignUpAdmin);
        tvForgotPasswordAdmin = findViewById(R.id.tvForgotPasswordAdmin);
        tvUser = findViewById(R.id.tvUser);
        etPasswordAdmin = findViewById(R.id.etPasswordAdmin);
        etUserNameAdmin = findViewById(R.id.etUserNameAdmin);
    }
}