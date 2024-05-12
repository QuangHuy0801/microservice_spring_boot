package com.example.doan_nhom_6.Retrofit;
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
public interface UserAPI {
    RetrofitService retrofitService = new RetrofitService();
    UserAPI userApi = retrofitService.getRetrofit().create(UserAPI.class);


    //    @Headers("Accept: application/json; charset=utf-8")

    @GET("/api/user/login")
    Call<User> Login(@Query("id") String id, @Query("password") String password);
    @GET("/api/user/getalluser")
    Call<List<User>> GetAllUser();
    @FormUrlEncoded
    @POST("/api/user/signup")
    Call<User> SignUp(@Field("username") String username, @Field("fullname") String fullname, @Field("email") String email, @Field("password")String password);

    @FormUrlEncoded
    @POST("/api/user/forgot")
    Call<String> forgotPassword(@Field("id") String user_id);

    @FormUrlEncoded
    @POST("/api/user/forgotnewpass")
    Call<String> forgotNewPass(@Field("id") String userId, @Field("code") String code, @Field("password") String password);

    @FormUrlEncoded
    @POST("/api/user/changepassword")
    Call<String> changePassword(@Field("id")String userId, @Field("password") String password);

    @Multipart
    @POST("/api/user/update")
    Call<User> update(@Part("id") RequestBody userId, @Part MultipartBody.Part avatar, @Part("fullname") RequestBody fullName, @Part("email") RequestBody email,
                      @Part("phoneNumber") RequestBody phoneNumber, @Part("address") RequestBody address);

    @FormUrlEncoded
    @POST("/api/user/google")
    Call<User> LoginWitGoogle(@Field("id") String userId, @Field("fullname") String fullName, @Field("email") String email, @Field("avatar") String avatar);

}
