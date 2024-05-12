package com.example.doan_nhom_6.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.doan_nhom_6.Model.User;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Somethings.ObjectSharedPreferences;

public class IntroActivity extends AppCompatActivity {
    TextView tvStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        setControl();
        setEvent();
    }

    private void setControl() {
        tvStart = findViewById(R.id.tvStart);
    }

    private void setEvent() {
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User isLoged = ObjectSharedPreferences.getSavedObjectFromPreference(IntroActivity.this, "User", "MODE_PRIVATE", User.class);
//        Log.e("loged", isLoged.toString());
                if (isLoged!=null){
                    startActivity(new Intent(IntroActivity.this, MainActivity.class));
                    finish();
                }
                else{
                    startActivity(new Intent(IntroActivity.this, LoginActivity.class));

                }
            }
        });

    }
}