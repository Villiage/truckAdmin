package com.fxlc.truckadmin.api;

import com.fxlc.truckadmin.bean.BankCards;
import com.fxlc.truckadmin.bean.BankList;
import com.fxlc.truckadmin.net.HttpResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by cyd on 2017/8/18.
 */

public interface AccountService {
     @GET("bankList")
    Call<HttpResult<BankList>>   listBank();
    @GET("bankCardList")
    Call<HttpResult<BankCards>>   listBankCard(@Query("ownerId") String ownerId);
    @POST("saveBank")
    Call<HttpResult>   saveBank(@QueryMap Map<String, String> map);




}
