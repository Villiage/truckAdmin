package com.fxlc.truckadmin.net;

import android.util.Log;
import android.widget.Toast;

import com.fxlc.truckadmin.MyApplication;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by cyd on 2017/6/21.
 */

public abstract class SimpleCallback implements Callback<HttpResult> {
    @Override
    public void onResponse(Call<HttpResult> call, Response<HttpResult> response) {


        HttpResult result = response.body();

        if (result != null) {
            Log.d("response", new Gson().toJson(result));
            if (result.isSuccess()){
                onSuccess(result);
            }else {
                onFailure(call,new MyThrowable(result.getErrorCode()));
                Toast.makeText(MyApplication.getInstance().getApplicationContext(), result.getMsg(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onFailure(Call<HttpResult> call, Throwable throwable) {
        if (throwable instanceof MyThrowable){

        }else{
            Toast.makeText(MyApplication.getInstance().getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();

        }
    }

    public abstract void onSuccess(HttpResult result);
}
