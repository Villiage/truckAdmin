package com.fxlc.truckadmin.api;


import com.fxlc.truckadmin.bean.User;
import com.fxlc.truckadmin.net.HttpResult;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by cyd on 2017/6/14.
 */

public interface UserService {
    @POST("register")
    Call<HttpResult> reg(@QueryMap Map<String, String> param);

    @POST("login")
    Call<HttpResult<User>> login(@QueryMap Map<String, String> param);

    @POST("loginPass")
    Call<HttpResult> setpwd(@QueryMap Map<String, String> param);

    @GET("sms/getSms")
    Call<ResponseBody> getSms(@Query("mobile") String mobile);



}
