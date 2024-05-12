package com.example.doan_nhom_6.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.doan_nhom_6.Adapter.AdminProductAdapter;
import com.example.doan_nhom_6.Adapter.AdminProductImagesAdapter;
import com.example.doan_nhom_6.Model.Category;
import com.example.doan_nhom_6.Model.Product;
import com.example.doan_nhom_6.Model.ProductImage;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.CategoryAPI;
import com.example.doan_nhom_6.Retrofit.ProductAPI;
import com.example.doan_nhom_6.Somethings.RealPathUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminProductsActivity extends AppCompatActivity implements AdminProductAdapter.DialogListener{

    private RecyclerView rcvProduct;
    List<Product> productList ;
    EditText etSearch;
    AdminProductAdapter adapter;
    ImageView ivBack,ivSearch;
    Button btnAddProduct, btnAddProductDialog;
    Dialog dialog;
    EditText etProductName,etProductQuantity,etProductPrice,etProductDecription,etProductSold;
    Switch switchIsSelling,switchIsActive;
    ImageView imgProduct;
    TextView tvResult, tvSelectImage;;
    Spinner spinnerCategory;
    ArrayAdapter<String> adapter1;

    RecyclerView rvImage;
    private List<Uri> imageUriList = new ArrayList<>();;
    AdminProductImagesAdapter productImagesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_products);
        AnhXa();
        ivBackClick();
        btnAddProductClick();
        loadCategorys();
        khoitaoimage();
        LoadProducts();
        ivSearchClick();

    }

    private void khoitaoimage() {
        productImagesAdapter = new AdminProductImagesAdapter(this, imageUriList);
        rvImage.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
        rvImage.setLayoutManager(layoutManager);
        rvImage.setAdapter(productImagesAdapter);
    }

    private void loadCategorys() {
        CategoryAPI.categoryAPI.GetAllCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.isSuccessful()) {
                    List<Category> categories = response.body();
                    List<String> categoryNames = new ArrayList<>();
                    for (Category category : categories) {
                        categoryNames.add(category.getId() + " - " + category.getCategory_Name());
                    }
                    adapter1 = new ArrayAdapter<>(AdminProductsActivity.this, android.R.layout.simple_list_item_1, categoryNames);
                    // Gán Adapter cho Spinner
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("====", "Call API Get Categories fail");
            }
        });
    }

    private void loadProductSearch() {
        ProductAPI.productApi.search(etSearch.getText().toString()).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful()){
                productList.clear();
                productList = response.body();
                tvResult.setText(productList.size() +" Results");
                adapter = new AdminProductAdapter(productList, AdminProductsActivity.this, AdminProductsActivity.this);
                rcvProduct.setHasFixedSize(true);
                GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
                rcvProduct.setLayoutManager(layoutManager);
                rcvProduct.setAdapter(adapter);}
            else{
                Toast.makeText(AdminProductsActivity.this, "Đã có lỗi ở product service", Toast.LENGTH_LONG).show();
                onBackPressed();
                finish();
            }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("====", "Call API Search fail");
            }
        });
    }

    public void onOpenDialogEdit(int pos, int ProductID,Product product){
        imageUriList.clear();
        btnAddProductDialog.setText("Update");
        etProductName.setText(product.getProduct_Name());
        etProductPrice.setText(String.valueOf(product.getPrice()));
        etProductQuantity.setText(String.valueOf(product.getQuantity()));
        etProductDecription.setText(String.valueOf(product.getDescription()));
        etProductSold.setText(String.valueOf(product.getSold()));
        switchIsActive.setChecked(product.getIs_Active()== 1);
        switchIsSelling.setChecked(product.getIs_Selling()== 1);
        spinnerCategory.setAdapter(adapter1);
        List<ProductImage> images = product.getProductImage();
        List<String> imagePaths =new ArrayList<>();
        for(ProductImage img : images){
            String imagePath = img.getUrl_Image();
            imagePaths.add(imagePath);
        }
        for (String imagePath : imagePaths) {
            Uri imageUri = Uri.parse(imagePath);
            imageUriList.add(imageUri);
        }
        productImagesAdapter.notifyDataSetChanged();
        for (Uri uri : imageUriList) {
            Log.e("Image URI =====", uri.toString());
        }
        dialog.show();

        tvSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckPermissions();
            }
        });


        btnAddProductDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedCategory = spinnerCategory.getSelectedItem().toString();
                String isSellingValue = switchIsSelling.isChecked() ? "1" : "0";
                String isActiveValue = switchIsActive.isChecked() ? "1" : "0";
                String[] parts = selectedCategory.split(" - ");
                String categoryId = parts[0]; // Lấy ID từ xâu đã phân tích
                RequestBody productID = RequestBody.create(String.valueOf(ProductID), MediaType.parse("multipart/form-data"));
                RequestBody productName = RequestBody.create(etProductName.getText().toString(), MediaType.parse("multipart/form-data"));
                RequestBody productQuanity = RequestBody.create(etProductQuantity.getText().toString(), MediaType.parse("multipart/form-data"));
                RequestBody productPrice = RequestBody.create(etProductPrice.getText().toString(), MediaType.parse("multipart/form-data"));
                RequestBody productDecription = RequestBody.create(etProductDecription.getText().toString(), MediaType.parse("multipart/form-data"));
                RequestBody productCategory = RequestBody.create(categoryId, MediaType.parse("multipart/form-data"));
                RequestBody productSold = RequestBody.create(etProductSold.getText().toString(), MediaType.parse("multipart/form-data"));
                RequestBody productIsSelling = RequestBody.create(isSellingValue, MediaType.parse("multipart/form-data"));
                RequestBody productIsActive = RequestBody.create(isActiveValue, MediaType.parse("multipart/form-data"));
                List<MultipartBody.Part> productImage = new ArrayList<>();
                CountDownLatch latch = new CountDownLatch(imageUriList.size());
                for (int i = 0; i < imageUriList.size(); i++) {
                    Uri imageUri = imageUriList.get(i);
                    Glide.with(view.getContext())
                            .asFile()
                            .load(imageUri)
                            .addListener(new RequestListener<File>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                                    latch.countDown();
                                    Log.e("fail =====", "fail");
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                                    RequestBody requestFile = RequestBody.create(resource, MediaType.parse("multipart/form-data"));
                                    MultipartBody.Part imagePart = MultipartBody.Part.createFormData("product_images", resource.getName(), requestFile);
                                    productImage.add(imagePart);
                                    latch.countDown();
                                    return false;
                                }
                            })
                            .submit();
                }
                try {
                    latch.await(); // Đợi cho tất cả các tải hình ảnh hoàn thành
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.e("Image URI1 =====", productImage.toString());
                ProductAPI.productApi.UpdateProduct(productID, productName,productPrice,productQuanity,productDecription,productCategory,productImage,productSold,productIsSelling,productIsActive).enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if(response.isSuccessful()){
                        Product updateProduct = response.body();
                        if(updateProduct != null){
                            dialog.dismiss();
                            productList.set(pos, updateProduct);
                            adapter.notifyItemChanged(pos);
                            Toast.makeText(AdminProductsActivity.this, "Sửa Product thành công...!", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(AdminProductsActivity.this, "Sửa Product không thành công???", Toast.LENGTH_SHORT).show();
                        }}else{
                            Toast.makeText(AdminProductsActivity.this, "Đã có lỗi ở product service", Toast.LENGTH_LONG).show();
                            onBackPressed();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        Log.e("====", "call fail + " + t.getMessage());
                    }
                });

            }
        });

    }

    private void btnAddProductClick() {
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAddProductDialog.setText("Add");
                etProductName.setText("");
                etProductPrice.setText(String.valueOf(""));
                etProductQuantity.setText(String.valueOf(""));
                etProductDecription.setText(String.valueOf(""));
                etProductSold.setText(String.valueOf(""));
                switchIsActive.setChecked(false);
                switchIsSelling.setChecked(false);
                spinnerCategory.setAdapter(adapter1);
                imageUriList.clear();
                productImagesAdapter.notifyDataSetChanged();
                dialog.show();
                tvSelectImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CheckPermissions();
                    }
                });

                btnAddProductDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String selectedCategory = spinnerCategory.getSelectedItem().toString();
                        String isSellingValue = switchIsSelling.isChecked() ? "1" : "0";
                        String isActiveValue = switchIsActive.isChecked() ? "1" : "0";
                        String[] parts = selectedCategory.split(" - ");
                        String categoryId = parts[0]; // Lấy ID từ xâu đã phân tích
                        RequestBody productName = RequestBody.create(etProductName.getText().toString(), MediaType.parse("multipart/form-data"));
                        RequestBody productQuanity = RequestBody.create(etProductQuantity.getText().toString(), MediaType.parse("multipart/form-data"));
                        RequestBody productPrice = RequestBody.create(etProductPrice.getText().toString(), MediaType.parse("multipart/form-data"));
                        RequestBody productDecription = RequestBody.create(etProductDecription.getText().toString(), MediaType.parse("multipart/form-data"));
                        RequestBody productCategory = RequestBody.create(categoryId, MediaType.parse("multipart/form-data"));
                        RequestBody productSold = RequestBody.create(etProductSold.getText().toString(), MediaType.parse("multipart/form-data"));
                        RequestBody productIsSelling = RequestBody.create(isSellingValue, MediaType.parse("multipart/form-data"));
                        RequestBody productIsActive = RequestBody.create(isActiveValue, MediaType.parse("multipart/form-data"));
                        List<MultipartBody.Part> productImage = new ArrayList<>();
                        for (int i = 0; i < imageUriList.size(); i++) {
                            Uri imageUri = imageUriList.get(i);
                            String imagePath = RealPathUtil.getRealPath(view.getContext(), imageUri);
                            File file = new File(imagePath);
                            RequestBody requestFile = RequestBody.create(file, MediaType.parse("multipart/form-data"));
                            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("product_images", file.getName(), requestFile);
                            productImage.add(imagePart);
                        }
                        ProductAPI.productApi.AddProduct(productName,productPrice,productQuanity,productDecription,productCategory,productImage,productSold,productIsSelling,productIsActive).enqueue(new Callback<Product>() {
                            @Override
                            public void onResponse(Call<Product> call, Response<Product> response) {
                                if(response.isSuccessful()){
                                Product newProduct = response.body();
                                if(newProduct != null){
                                    dialog.dismiss();
                                    productList.add(newProduct);
                                    adapter.notifyItemInserted(productList.size() - 1);
                                    Toast.makeText(AdminProductsActivity.this, "Thêm Product thành công...!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(AdminProductsActivity.this, "Thêm Product không thành công???", Toast.LENGTH_SHORT).show();
                                }}else{
                                    Toast.makeText(AdminProductsActivity.this, "Đã có lỗi ở product service", Toast.LENGTH_LONG).show();
                                    onBackPressed();
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<Product> call, Throwable t) {
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

    private void LoadProducts() {
        ProductAPI.productApi.GetAllProduct().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful()){
                productList = response.body();
                tvResult.setText(productList.size() +" Results");
                adapter = new AdminProductAdapter(productList, AdminProductsActivity.this, AdminProductsActivity.this);
                rcvProduct.setHasFixedSize(true);
                GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),1);
                rcvProduct.setLayoutManager(layoutManager);
                rcvProduct.setAdapter(adapter);
                 }else{
                Toast.makeText(AdminProductsActivity.this, "Đã có lỗi ở product service", Toast.LENGTH_LONG).show();
                onBackPressed();
                finish();
            }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("====", "Call API Get Categories fail");

            }
        });
    }
    private void ivSearchClick() {
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadProductSearch();
            }
        });

        etSearch.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Intent intent = new Intent(AdminProductsActivity.this, AdminProductsActivity.class);
                    intent.putExtra("searchContent", etSearch.getText().toString());
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    private void AnhXa() {
        rcvProduct = findViewById(R.id.rcvProduct);
        ivBack = findViewById(R.id.ivBack);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        dialog = new Dialog(AdminProductsActivity.this);
        dialog.setContentView(R.layout.dialog_add_product);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        etProductName = dialog.findViewById(R.id.etProductName);
        etProductPrice = dialog.findViewById(R.id.etProductPrice);
        etProductQuantity = dialog.findViewById(R.id.etProductQuanity);
        etProductDecription = dialog.findViewById(R.id.etProductDecription);
        etProductSold= dialog.findViewById(R.id.etProductSold);
        spinnerCategory = dialog.findViewById(R.id.spinnerCategory);
        switchIsActive = dialog.findViewById(R.id.switchIsActive);
        switchIsSelling = dialog.findViewById(R.id.switchIsSelling);
        btnAddProductDialog = dialog.findViewById(R.id.btnAdd);
        etSearch = findViewById(R.id.etSearch);
        ivSearch = findViewById(R.id.ivSearch);
        tvResult=findViewById(R.id.tvResult);
        imgProduct = dialog.findViewById(R.id.imgProduct);
        tvSelectImage = dialog.findViewById(R.id.tvSelectImage);
        rvImage = dialog.findViewById(R.id.rvImage);
        imageUriList = new ArrayList<>();
    }

    //Upload Image
    private Uri mUri;
    private ProgressDialog mProgessDialog;
    public static final int MY_REQUEST_CODE = 100;
    public static final String TAG = AdminProductsActivity.class.getName();
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
                        imageUriList.add(uri);
                        productImagesAdapter.notifyDataSetChanged();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                            imgProduct.setImageBitmap(bitmap);
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
}