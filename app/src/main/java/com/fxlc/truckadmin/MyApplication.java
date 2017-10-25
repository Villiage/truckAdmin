package com.fxlc.truckadmin;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.fxlc.truckadmin.bean.User;
import com.fxlc.truckadmin.db.CarDao;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cyd on 2017/7/31.
 */

public class MyApplication extends Application {
    private static MyApplication instance;
    private static Retrofit retrofit;
    private static User user;
    private static SharedPreferences sp ;
    private static List<Activity> activities = new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();


        instance = this;
        sp   = getSharedPreferences("truckerAdmin", Context.MODE_PRIVATE);
        initUser();
        initRetrofit();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                initCarsData();
//
//            }
//        }).start();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                activities.add(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                activities.remove(activity);
            }
        });
    }
    public static MyApplication getInstance() {


        return instance;
    }

    public static Retrofit getRetrofit() {


        return retrofit;
    }

    public static SharedPreferences getSp() {
        return sp;
    }

    private void initRetrofit() {

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Log.d("request", request.url().toString());
                HttpUrl.Builder urlBuilder = request.url().newBuilder();
                if (user != null) {
                    urlBuilder.addQueryParameter("userId", user.getId());
                    urlBuilder.addQueryParameter("token", user.getToken());
                }
                request = request.newBuilder().url(urlBuilder.build()).build();
                okhttp3.Response response = chain.proceed(request);

                return response;
            }
        }).build();

        retrofit = new Retrofit.Builder().baseUrl(Constant.Host)
                //     .addConverterFactory( StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

    }

    private void initUser() {


        String userStr = sp.getString("user", "");
        Log.d("user"," ---" +  userStr);
        if (!TextUtils.isEmpty(userStr))
            user = new Gson().fromJson(userStr, User.class);


    }
    public static void exit() {

        user = null;
        sp.edit().remove("user").commit();
        for (Activity activity : activities) {
            if (activity != null){
                activity.finish();

            }
        }
    }
    public static void setUser(User u) {
        user = u;

    }
    public static User getUser() {


        return user;
    }
    public void initCarsData() {

        CarDao dao = new CarDao(getApplicationContext());
        if (!dao.hasData()) {

            try {
                InputStream is = getAssets().open("car.xls");
                dao.readExcel(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
