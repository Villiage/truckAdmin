package com.fxlc.truckadmin.api;

import com.fxlc.truckadmin.bean.BillInfo;
import com.fxlc.truckadmin.bean.FeeInfo;
import com.fxlc.truckadmin.bean.SourceList;
import com.fxlc.truckadmin.net.HttpResult;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by cyd on 2017/9/20.
 */

public interface BillService {

    @GET("sourceList")
    Call<HttpResult<SourceList>>  listSource();

    @GET("billInfo")
    Call<HttpResult<BillInfo>>  billInfo(@Query("sourceId") String sourceId, @Query("carId") String carId);

    @Multipart
    @POST("loadOrder")
    Call<HttpResult> loadOrder(@QueryMap Map<String, String> map, @Part("loadImg\"; filename=\"" + "loadImg") RequestBody body);

    @Multipart
    @POST("unloadOrder")
    Call<HttpResult> unloadOrder(@QueryMap Map<String, String> map, @Part("unloadImg\"; filename=\"" + "unloadImg") RequestBody body);


    @GET("feeInfo")
    Call<HttpResult<FeeInfo>> feeInfo(@Query("orderId") String orderId);
    @GET("billDetail")
    Call<HttpResult<BillInfo>> billDetail(@Query("orderId") String orderId);


    @POST("goCount")
    Call<HttpResult> goCount(@Query("orderId") String orderId,@Query("bankCardId") String bankCardId);



}
