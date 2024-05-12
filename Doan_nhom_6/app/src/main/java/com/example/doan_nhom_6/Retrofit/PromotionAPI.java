package com.example.doan_nhom_6.Retrofit;

import com.example.doan_nhom_6.Model.Product;
import com.example.doan_nhom_6.Model.Promotion;
import com.example.doan_nhom_6.Model.Promotion_Item;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PromotionAPI {
    RetrofitService retrofitService = new RetrofitService();
    PromotionAPI promotionAPI = retrofitService.getRetrofit().create(PromotionAPI.class);
    @GET("/api/product/promotion")
    Call<List<Promotion>> getAllPromotion();
    @GET("/api/product/promotion/checkProduct/{productId}")
    Call<Promotion> checkProDuctInPromotion(@Path("productId") int id);
    @Multipart
    @POST("/api/product/promotion/new")
    Call<Promotion> newPromotion(@Part("name") RequestBody PromoName,
                                 @Part("description") RequestBody Description,
                                 @Part("start") RequestBody StartDate,
                                 @Part("end") RequestBody EndDate,
                                 @Part("discount") RequestBody Discount,
                                 @Part("status") RequestBody Status);

    @PUT("/api/product/promotion/update")
    Call<Promotion> updatePromotion(@Query("id") String PromoId,
                                    @Query("name") String PromoName,
                                    @Query("description") String Description,
                                    @Query("start") String StartDate,
                                    @Query("end") String EndDate,
                                    @Query("discount") String Discount,
                                    @Query("status") String Status);

    @DELETE("/api/product/promotion/delete/{id}")
    Call<Promotion> DeletePromotion(@Path("id") int id);

    @DELETE("/api/product/promotion/deleteProduct/{id}")
    Call<Promotion_Item> DeleteProductInPromotion(@Path("id") int id);

    @PUT("/api/product/promotion/addProduct")
    Call<Promotion> addProductInPromotion(@Query("promotionId") int promotionId, @Body List<Product> products);
}
