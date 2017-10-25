package com.fxlc.truckadmin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fxlc.truckadmin.bean.User;


public class BaseActivity extends AppCompatActivity {
    public TextView titleTxt;
    private View back;
    public Context ctx;
    public Intent it = new Intent();
    public static String TAG = "HttpResp";
    public ProgressDialog proDialog;
    public SharedPreferences sp = MyApplication.getSp();
    public String netErrorMsg = "网络异常";
    public User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;

        user = MyApplication.getUser();
        proDialog = new ProgressDialog(this);
        proDialog.setMessage("加载中");


    }


    @Override
    protected void onResume() {
        super.onResume();
        back = findViewById(R.id.back);
        titleTxt = (TextView) findViewById(R.id.title);
        if (back != null)
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });


    }

    public void title(String title) {
        titleTxt.setText(title);
    }

    protected void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    protected void save(String key, String str) {
        sp.edit().putString(key, str).commit();
    }





}
