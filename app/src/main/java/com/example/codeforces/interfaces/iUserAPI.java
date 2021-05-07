package com.example.codeforces.interfaces;

import com.example.codeforces.Models.UserModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface iUserAPI {

    @GET("user.info")
    Call<UserModel> getUserInfo(@Query("handles") String handle);
}
