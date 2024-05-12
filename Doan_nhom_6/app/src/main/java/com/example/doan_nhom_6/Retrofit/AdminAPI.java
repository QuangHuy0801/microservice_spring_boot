package com.example.doan_nhom_6.Retrofit;

import com.example.doan_nhom_6.Model.ReportTotal;
import com.example.doan_nhom_6.Model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface AdminAPI {
    RetrofitService retrofitService = new RetrofitService();
    AdminAPI adminApi = retrofitService.getRetrofit().create(AdminAPI.class);

    @GET("/api/user/loginAdmin")
    Call<User> Login(@Query("id") String id, @Query("password") String password);
    @FormUrlEncoded
    @POST("/api/user/forgotadmin")
    Call<String> forgotPassword(@Field("id") String user_id);
    @FormUrlEncoded
    @POST("/api/user/forgotnewpassadmin")
    Call<String> forgotNewPass(@Field("id") String userId, @Field("code") String code, @Field("password") String password);
    @FormUrlEncoded
    @POST("/api/user/changepasswordadmin")
    Call<String> changePassword(@Field("id")String userId, @Field("password") String password);
    @Multipart
    @POST("/api/user/updateadmin")
    Call<User> update(@Part("id") RequestBody userId, @Part MultipartBody.Part avatar, @Part("fullname") RequestBody fullName, @Part("email") RequestBody email,
                      @Part("phoneNumber") RequestBody phoneNumber, @Part("address") RequestBody address);
    @GET("/revenue-statistic")
    Call<List<ReportTotal>> RevenueStatistic(@Query("dateFrom") String dateFrom, @Query("dateTo") String dateTo);
    @GET("/quantity-statistic")
    Call<List<ReportTotal>> QuantityStatistic(@Query("dateFrom") String dateFrom, @Query("dateTo") String dateTo);
    @GET("/api/product/product-statistic")
    Call<List<ReportTotal>> ProductStatistic();
    @GET("/api/product/unit-of-product-statistic")
    Call<List<ReportTotal>> UnitOfProductStatistic();
}
