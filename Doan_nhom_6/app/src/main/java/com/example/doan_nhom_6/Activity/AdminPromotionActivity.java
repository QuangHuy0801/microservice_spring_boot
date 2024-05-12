package com.example.doan_nhom_6.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.doan_nhom_6.Adapter.AdminCategoryAdapter;
import com.example.doan_nhom_6.Adapter.AdminPromotionAdapter;
import com.example.doan_nhom_6.Model.Product;
import com.example.doan_nhom_6.Model.Promotion;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.ProductAPI;
import com.example.doan_nhom_6.Retrofit.PromotionAPI;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPromotionActivity extends AppCompatActivity implements AdminPromotionAdapter.DialogListener{
    ImageView ivBack;
    RecyclerView rcvPromotion;
    Button btnAddPromo, btnAddDialog;
    List<Promotion> promoList;
    AdminPromotionAdapter adapter;
    Dialog dialog;
    EditText etPromoName, etDescription, etStartDate, etEndDate, etDiscount;
    ImageView ivStartDate, ivEndDate;
    Switch swIsActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_promotion);
        AnhXa();
        ivBackClick();
        LoadPromotion();
        btnAddPromoClick();
    }

    private void btnAddPromoClick() {
        btnAddPromo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAddDialog.setText("Add");
                etPromoName.setText("");
                etDescription.setText("");
                etStartDate.setText("");
                etEndDate.setText("");
                etDiscount.setText("");
                swIsActive.setChecked(false);
                dialog.show();

                ivStartDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Lấy ngày hiện tại
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                        // Tạo DatePickerDialog
                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                AdminPromotionActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        etStartDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                    }
                                },
                                year, month, dayOfMonth);
                        datePickerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                Button positiveButton = ((DatePickerDialog) datePickerDialog).getButton(DialogInterface.BUTTON_POSITIVE);
                                positiveButton.setTextColor(getResources().getColor(android.R.color.black));

                                Button negativeButton = ((DatePickerDialog) datePickerDialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                                negativeButton.setTextColor(getResources().getColor(android.R.color.black));
                            }
                        });
                        datePickerDialog.show();
                    }
                });

                ivEndDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Lấy ngày hiện tại
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                        // Tạo DatePickerDialog
                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                AdminPromotionActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        etEndDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                    }
                                },
                                year, month, dayOfMonth);
                        datePickerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                Button positiveButton = ((DatePickerDialog) datePickerDialog).getButton(DialogInterface.BUTTON_POSITIVE);
                                positiveButton.setTextColor(getResources().getColor(android.R.color.black));

                                Button negativeButton = ((DatePickerDialog) datePickerDialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                                negativeButton.setTextColor(getResources().getColor(android.R.color.black));
                            }
                        });
                        datePickerDialog.show();
                    }
                });

                btnAddDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String isActiveValue = swIsActive.isChecked() ? "Active" : "Inactive";
                        double discount = Double.valueOf(etDiscount.getText().toString()) / 100;
                        RequestBody PromoName = RequestBody.create(etPromoName.getText().toString(), MediaType.parse("multipart/form-data"));
                        RequestBody Description = RequestBody.create(etDescription.getText().toString(), MediaType.parse("multipart/form-data"));
                        RequestBody StartDate = RequestBody.create(etStartDate.getText().toString(), MediaType.parse("multipart/form-data"));
                        RequestBody EndDate = RequestBody.create(etEndDate.getText().toString(), MediaType.parse("multipart/form-data"));
                        RequestBody Discount = RequestBody.create(String.valueOf(discount), MediaType.parse("multipart/form-data"));
                        RequestBody Status = RequestBody.create(isActiveValue, MediaType.parse("multipart/form-data"));

                        PromotionAPI.promotionAPI.newPromotion(PromoName, Description, StartDate, EndDate, Discount, Status).enqueue(new Callback<Promotion>() {
                            @Override
                            public void onResponse(Call<Promotion> call, Response<Promotion> response) {
                                Promotion newPromotion = response.body();
                                if (newPromotion != null){
                                    dialog.dismiss();
                                    promoList.add(newPromotion);
                                    adapter.notifyItemInserted(promoList.size() - 1);
                                    Toast.makeText(AdminPromotionActivity.this, "Thêm khuyến mãi thành công ...", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(AdminPromotionActivity.this, "Thêm khuyến mãi thất bại ...", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Promotion> call, Throwable t) {
                                Toast.makeText(AdminPromotionActivity.this, "Call API new promotion fail!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
    }

    private void LoadPromotion() {
        PromotionAPI.promotionAPI.getAllPromotion().enqueue(new Callback<List<Promotion>>() {
            @Override
            public void onResponse(Call<List<Promotion>> call, Response<List<Promotion>> response) {
                if(response.isSuccessful()){
                promoList = response.body();
                adapter = new AdminPromotionAdapter(promoList, AdminPromotionActivity.this, AdminPromotionActivity.this);
                rcvPromotion.setHasFixedSize(true);
                GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
                rcvPromotion.setLayoutManager(layoutManager);
                rcvPromotion.setAdapter(adapter);
            }else{
                Toast.makeText(AdminPromotionActivity.this, "Đã có lỗi ở product service", Toast.LENGTH_LONG).show();
                onBackPressed();
                finish();
            }
            }

            @Override
            public void onFailure(Call<List<Promotion>> call, Throwable t) {
                Toast.makeText(AdminPromotionActivity.this, "Call API get all promotion fail!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ivBackClick() {
        ivBack.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }

    private void AnhXa() {
        ivBack = findViewById(R.id.ivBack);
        rcvPromotion = findViewById(R.id.rcvPromotion);
        btnAddPromo = findViewById(R.id.btnAddPromo);
        dialog = new Dialog(AdminPromotionActivity.this);
        dialog.setContentView(R.layout.dialog_add_promotion);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        etPromoName = dialog.findViewById(R.id.etPromoName);
        etDescription = dialog.findViewById(R.id.etDescription);
        etStartDate = dialog.findViewById(R.id.etStartDate);
        etEndDate = dialog.findViewById(R.id.etEndDate);
        etDiscount = dialog.findViewById(R.id.etDiscount);
        swIsActive = dialog.findViewById(R.id.swIsActive);
        ivStartDate = dialog.findViewById(R.id.ivStartDate);
        ivEndDate = dialog.findViewById(R.id.ivEndDate);
        btnAddDialog = dialog.findViewById(R.id.btnAddDialog);
    }

    @Override
    public void onOpenDialogEdit(int pos, Promotion promotion) {
        btnAddDialog.setText("Update");
        etPromoName.setText(promotion.getName());
        etDescription.setText(promotion.getDescription());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        etStartDate.setText(format.format(promotion.getStartDate()));
        etEndDate.setText(format.format(promotion.getEndDate()));
        etDiscount.setText(String.valueOf((int)(promotion.getDiscountPercent() * 100)));
        swIsActive.setChecked(promotion.getStatus().equals("Active"));
        dialog.show();

        ivStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy ngày hiện tại
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Tạo DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(AdminPromotionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                etStartDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        },
                        year, month, dayOfMonth);
                datePickerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button positiveButton = ((DatePickerDialog) datePickerDialog).getButton(DialogInterface.BUTTON_POSITIVE);
                        positiveButton.setTextColor(getResources().getColor(android.R.color.black));

                        Button negativeButton = ((DatePickerDialog) datePickerDialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                        negativeButton.setTextColor(getResources().getColor(android.R.color.black));
                    }
                });
                datePickerDialog.show();
            }
        });

        ivEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy ngày hiện tại
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Tạo DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AdminPromotionActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                etEndDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        },
                        year, month, dayOfMonth);
                datePickerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button positiveButton = ((DatePickerDialog) datePickerDialog).getButton(DialogInterface.BUTTON_POSITIVE);
                        positiveButton.setTextColor(getResources().getColor(android.R.color.black));

                        Button negativeButton = ((DatePickerDialog) datePickerDialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                        negativeButton.setTextColor(getResources().getColor(android.R.color.black));
                    }
                });
                datePickerDialog.show();
            }
        });

        btnAddDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Status = swIsActive.isChecked() ? "Active" : "Inactive";
                double discount = Double.valueOf(etDiscount.getText().toString()) / 100;
                String PromoId = String.valueOf(promotion.getId());
                String PromoName = etPromoName.getText().toString();
                String Description = etDescription.getText().toString();
                String StartDate = etStartDate.getText().toString();
                String EndDate = etEndDate.getText().toString();
                String Discount = String.valueOf(discount);

                PromotionAPI.promotionAPI.updatePromotion(PromoId,PromoName,Description,StartDate,EndDate,Discount,Status).enqueue(new Callback<Promotion>() {
                    @Override
                    public void onResponse(Call<Promotion> call, Response<Promotion> response) {
                        Promotion updatePromotion = response.body();
                        if(updatePromotion != null){
                            dialog.dismiss();
                            promoList.set(pos, updatePromotion);
                            adapter.notifyItemChanged(pos);
                            Toast.makeText(AdminPromotionActivity.this, "Sửa Promotion thành công...!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(AdminPromotionActivity.this, "Sửa Promotion thất bại...!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Promotion> call, Throwable t) {
                        Toast.makeText(AdminPromotionActivity.this, "Call API update promotion fail!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}