package com.example.doan_nhom_6.Activity;

import static java.lang.Integer.parseInt;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.doan_nhom_6.Adapter.SliderAdapter;
import com.example.doan_nhom_6.Model.Cart;
import com.example.doan_nhom_6.Model.Product;
import com.example.doan_nhom_6.Model.Promotion;
import com.example.doan_nhom_6.Model.User;
import com.example.doan_nhom_6.R;
import com.example.doan_nhom_6.Retrofit.CartAPI;
import com.example.doan_nhom_6.Retrofit.PromotionAPI;
import com.example.doan_nhom_6.Somethings.ObjectSharedPreferences;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;

public class ShowDetailActivity extends AppCompatActivity {
    TextView tvTitle, tvdescription, tvPrice, tvTotalPrice, tvSold, tvAvailable, tvNumber, tvAddToCart, tvPriceDisCount, tvDiscount;
    ImageView ivMinus, ivPlus;
    Product product;
    ConstraintLayout clBack;
    LinearLayout lnPriceDiscount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);
        product = (Product) getIntent().getSerializableExtra("product");
        setControl();
        setEvent();
    }

    private void setEvent() {
        ivMinusClick();
        ivPlusClick();
        tvAddToCartClick();
        clBackClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadProduct();
    }

    private void clBackClick() {
        clBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void tvAddToCartClick() {
        tvAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = ObjectSharedPreferences.getSavedObjectFromPreference(ShowDetailActivity.this, "User", "MODE_PRIVATE", User.class);
                int count = parseInt(tvNumber.getText().toString());
                CartAPI.cartAPI.addToCart(user.getId(), product.getId(), count).enqueue(new Callback<Cart>() {
                    @Override
                    public void onResponse(Call<Cart> call, Response<Cart> response) {
                        Cart cart = response.body();
                        if(cart !=null){
                            Toast.makeText(ShowDetailActivity.this.getApplicationContext(), "Thêm vào giỏ thành công", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Cart> call, Throwable t) {
                        Toast.makeText(ShowDetailActivity.this.getApplicationContext(), "Thêm vào giỏ thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void LoadProduct() {
        LoadImage();
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);
        PromotionAPI.promotionAPI.checkProDuctInPromotion(product.getId()).enqueue(new Callback<Promotion>() {
            @Override
            public void onResponse(Call<Promotion> call, Response<Promotion> response) {
                Promotion promotion = response.body();
                if(promotion != null){
                    lnPriceDiscount.setVisibility(View.VISIBLE);
                    tvDiscount.setText("-" + (int) (promotion.getDiscountPercent()*100) + "%");
                    tvPriceDisCount.setText(en.format(product.getPrice()));
                    tvPriceDisCount.setPaintFlags(tvPriceDisCount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tvPriceDisCount.getPaint().setFakeBoldText(true);
                    tvPriceDisCount.setTextColor(Color.BLACK);
                    double Price = product.getPrice() - (product.getPrice() * promotion.getDiscountPercent());
                    tvPrice.setText(en.format(Price));
                    tvTotalPrice.setText(en.format(Price));
                }
                else {
                    lnPriceDiscount.setVisibility(View.GONE);
                    tvPrice.setText(en.format(product.getPrice()));
                    tvTotalPrice.setText(en.format(product.getPrice()));
                }
            }

            @Override
            public void onFailure(Call<Promotion> call, Throwable t) {
                Toast.makeText(ShowDetailActivity.this, "Call API Check Product in promotion fail", Toast.LENGTH_SHORT).show();
            }
        });
        tvTitle.setText(product.getProduct_Name());
        tvdescription.setText(product.getDescription());
        tvSold.setText(String.valueOf(product.getSold()));
        tvAvailable.setText(String.valueOf(product.getQuantity()));
        tvNumber.setText("1");

    }

    private void LoadImage() {
        SliderView sliderView = findViewById(R.id.imageSlider);

        SliderAdapter adapter = new SliderAdapter(ShowDetailActivity.this, product.getProductImage());

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }

    private void ivPlusClick() {
        ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = parseInt(tvNumber.getText().toString()) +1;
                if(number <= product.getQuantity()){
                    tvNumber.setText(String.valueOf(number));
                    Locale localeEN = new Locale("en", "EN");
                    NumberFormat en = NumberFormat.getInstance(localeEN);
                    tvTotalPrice.setText(en.format(parseInt(tvPrice.getText().toString().replace(",", ""))*number));
                }
            }
        });
    }

    private void ivMinusClick() {
        ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = parseInt(tvNumber.getText().toString()) - 1;
                if(number >= 1){
                    tvNumber.setText(String.valueOf(number));
                    Locale localeEN = new Locale("en", "EN");
                    NumberFormat en = NumberFormat.getInstance(localeEN);
                    tvTotalPrice.setText(en.format(parseInt(tvPrice.getText().toString().replace(",", ""))*number));
                }
            }
        });
    }

    private void setControl() {
        tvAddToCart = findViewById(R.id.tvAddToCart);
        tvTitle = findViewById(R.id.tvTitle);
        tvdescription = findViewById(R.id.tvDescription);
        tvPrice = findViewById(R.id.tvPrice);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvSold = findViewById(R.id.tvSold);
        tvAvailable = findViewById(R.id.tvAvailable);
        tvNumber = findViewById(R.id.tvNumber);
        ivMinus = findViewById(R.id.ivMinus);
        ivPlus = findViewById(R.id.ivPlus);
        clBack = findViewById(R.id.clBack);
        lnPriceDiscount = findViewById(R.id.lnPriceDiscont);
        tvDiscount = findViewById(R.id.tvDiscount);
        tvPriceDisCount = findViewById(R.id.tvPriceDiscont);
    }
}
