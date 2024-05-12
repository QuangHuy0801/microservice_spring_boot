package com.example.doan_nhom_6.Retrofit;

import com.example.doan_nhom_6.Model.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderAPI {
    RetrofitService retrofitService = new RetrofitService();
    OrderAPI orderAPI = retrofitService.getRetrofit().create(OrderAPI.class);

    @GET("/api/order/checkconnect")
    Call<Boolean> checkconnect();

    @GET("/api/order/allOrder")
    Call<List<Order>> getAllOrder();
    @FormUrlEncoded
    @POST("/api/order/placeorder")
    Call<Order> placeOrder(@Field("user_id") String user_id, @Field("fullname") String fullname,
                           @Field("phoneNumber") String phoneNumber, @Field("address") String address, @Field("paymentMethod") String paymentMethod);
    @GET("/api/order/order")
    Call<List<Order>> getOrderByUserId(@Query("user_id") String user_id);

    @GET("/api/order/ordermethod")
    Call<List<Order>> getOrderByUserIdAndPaymentMethod(@Query("user_id") String user_id, @Query("method") String method);

    @PATCH("/api/order/order/updateStatus/{orderId}")
    Call<Order> updateStatus(@Path("orderId") int id, @Query("newStatus") String newStatus);

    @GET("/api/order/orderStatus")
    Call<List<Order>> getOrderByStatus(@Query("status") String status);
}
