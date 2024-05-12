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
import com.example.doan_nhom_6.Adapter.CategoryAdapter;
import com.example.doan_nhom_6.Model.Category;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.CategoryAPI;
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

public class AdminCategoryActivity extends AppCompatActivity implements AdminCategoryAdapter.DialogListener{
    private RecyclerView rcvCategoty;
    List<Category> categoriesList;
    AdminCategoryAdapter adapter;
    ImageView ivBack;
    Button btnAddCategory, btnAddCategoryDialog;
    Dialog dialog;
    EditText etCategoryName;
    ImageView imgCategory;
    TextView tvSelectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);
        AnhXa();
        LoadCategories();
        ivBackClick();
        btnAddCategoryClick();
    }

    public void onOpenDialogEdit(int pos, int categoryID){
        btnAddCategoryDialog.setText("Update");
        dialog.show();

        Log.e("===== CategoryID =====", String.valueOf(categoryID));

        tvSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckPermissions();
            }
        });

        btnAddCategoryDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestBody ID = RequestBody.create(String.valueOf(categoryID), MediaType.parse("multipart/form-data"));
                RequestBody categoryName = RequestBody.create(etCategoryName.getText().toString(), MediaType.parse("multipart/form-data"));
                MultipartBody.Part categoryImage = null;
                if(mUri!=null){
                    String IMAGE_PATH = RealPathUtil.getRealPath(view.getContext(), mUri);
                    Log.e("ffff", IMAGE_PATH);
                    File file = new File(IMAGE_PATH);
                    RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
                    categoryImage = MultipartBody.Part.createFormData("category_image", file.getName(), requestFile);
                }

                CategoryAPI.categoryAPI.UpdateCategory(ID, categoryName, categoryImage).enqueue(new Callback<Category>() {
                    @Override
                    public void onResponse(Call<Category> call, Response<Category> response) {
                        if(response.isSuccessful()){
                        Category updateCategory = response.body();
                        if(updateCategory != null){
                            dialog.dismiss();
                            categoriesList.set(pos, updateCategory);
                            adapter.notifyItemChanged(pos);
                            Toast.makeText(AdminCategoryActivity.this, "Sửa Category thành công...!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(AdminCategoryActivity.this, "Sửa Category không thành công???", Toast.LENGTH_SHORT).show();
                        }}else{
                            Toast.makeText(AdminCategoryActivity.this, "Đã có lỗi ở product service", Toast.LENGTH_LONG).show();
                            onBackPressed();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Category> call, Throwable t) {
                        Log.e("====", "call fail + " + t.getMessage());
                    }
                });

            }
        });

    }

    private void btnAddCategoryClick() {
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAddCategoryDialog.setText("Add");
                dialog.show();

                tvSelectImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckPermissions();
                    }
                });

                btnAddCategoryDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RequestBody categoryName = RequestBody.create(etCategoryName.getText().toString(), MediaType.parse("multipart/form-data"));
                        MultipartBody.Part categoryImage = null;
                        if(mUri!=null){
                            String IMAGE_PATH = RealPathUtil.getRealPath(view.getContext(), mUri);
                            Log.e("ffff", IMAGE_PATH);
                            File file = new File(IMAGE_PATH);
                            RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
                            categoryImage = MultipartBody.Part.createFormData("category_image", file.getName(), requestFile);
                        }

                        CategoryAPI.categoryAPI.AddCategory(categoryName, categoryImage).enqueue(new Callback<Category>() {
                            @Override
                            public void onResponse(Call<Category> call, Response<Category> response) {
                                if(response.isSuccessful()){
                                Category newCategory = response.body();
                                if(newCategory != null){
                                    dialog.dismiss();
                                    categoriesList.add(newCategory);
                                    adapter.notifyItemInserted(categoriesList.size() - 1);
                                    Toast.makeText(AdminCategoryActivity.this, "Thêm Category thành công...!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(AdminCategoryActivity.this, "Thêm Category không thành công???", Toast.LENGTH_SHORT).show();
                                }}
                                else{
                                    Toast.makeText(AdminCategoryActivity.this, "Đã có lỗi ở product service", Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<Category> call, Throwable t) {
                                Log.e("====", "call fail + " + t.getMessage());
                            }
                        });
                    }
                });

            }
        });
    }

    private void ivBackClick() {
        ivBack.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
    }

    private void LoadCategories() {
        CategoryAPI.categoryAPI.GetAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.isSuccessful()){
                categoriesList = response.body();
                adapter = new AdminCategoryAdapter(categoriesList, AdminCategoryActivity.this, AdminCategoryActivity.this);
                rcvCategoty.setHasFixedSize(true);
                GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
                rcvCategoty.setLayoutManager(layoutManager);
                rcvCategoty.setAdapter(adapter);}
                else{
                    Toast.makeText(AdminCategoryActivity.this, "Đã có lỗi ở product service", Toast.LENGTH_LONG).show();
                    onBackPressed();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("====", "Call API Get Categories fail");

            }
        });
    }

    private void AnhXa() {
        rcvCategoty = findViewById(R.id.rcvCategory);
        ivBack = findViewById(R.id.ivBack);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        dialog = new Dialog(AdminCategoryActivity.this);
        dialog.setContentView(R.layout.dialog_add_category);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        etCategoryName = dialog.findViewById(R.id.etCategoryName);
        imgCategory = dialog.findViewById(R.id.imgCategory);
        tvSelectImage = dialog.findViewById(R.id.tvSelectImage);
        btnAddCategoryDialog = dialog.findViewById(R.id.btnAdd);
    }

    //Upload Image
    private Uri mUri;
    private ProgressDialog mProgessDialog;
    public static final int MY_REQUEST_CODE = 100;
    public static final String TAG = AdminCategoryActivity.class.getName();
    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }

    private void CheckPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openGallery();
        } else {
            requestPermissions(permissions(), MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLaucher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    private ActivityResultLauncher<Intent> mActivityResultLaucher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>(){
                @Override
                public void onActivityResult(ActivityResult result){
                    Log.e(TAG, "onActivityResult");
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data == null){
                            return;
                        }
                        Uri uri = data.getData();
                        mUri = uri;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                            imgCategory.setImageBitmap(bitmap);
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
}