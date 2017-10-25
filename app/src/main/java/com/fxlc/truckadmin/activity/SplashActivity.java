package com.fxlc.truckadmin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.fxlc.truckadmin.MainActivity;
import com.fxlc.truckadmin.MyApplication;
import com.fxlc.truckadmin.bean.User;

public class SplashActivity extends Activity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        user = MyApplication.getUser();
        if (user == null) {
            Intent it = new Intent();
            it.setClass(this, LoginActivity.class);
            startActivity(it);
            finish();
        } else {
            Intent it = new Intent();
            it.setClass(this, MainActivity.class);
            startActivity(it);
            finish();
        }

    }
}
