package com.fxlc.truckadmin.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.fxlc.truckadmin.BaseActivity;
import com.fxlc.truckadmin.MainActivity;
import com.fxlc.truckadmin.MyApplication;
import com.fxlc.truckadmin.R;
import com.fxlc.truckadmin.api.UserService;
import com.fxlc.truckadmin.bean.User;
import com.fxlc.truckadmin.net.HttpCallback;
import com.fxlc.truckadmin.net.HttpResult;
import com.google.gson.Gson;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {


    private EditText mPhoneView;
    private EditText mPwdView;
    private String phone, pass;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_login);
        initView();

    }

    private void initView() {

        mPhoneView = (EditText) findViewById(R.id.phone);
        mPwdView = (EditText) findViewById(R.id.pwd);
        findViewById(R.id.reg).setOnClickListener(this);
        findViewById(R.id.setpwd).setOnClickListener(this);

        findViewById(R.id.action).setOnClickListener(this);
    }

    private void getValue() {
        phone = mPhoneView.getText().toString();
        pass = mPwdView.getText().toString();

    }


    private void login() {

        proDialog.show();

        Retrofit retrofit = MyApplication.getRetrofit();
        UserService apiService = retrofit.create(UserService.class);

        HashMap map = new HashMap();
        map.put("mobile", phone);
        map.put("pass", pass);


        Call<HttpResult<User>> call = apiService.login(map);

        call.enqueue(new HttpCallback<User>() {
            @Override
            public void onSuccess(User user) {
                proDialog.dismiss();
                toast("登录成功");
                save("user", new Gson().toJson(user));
                /*
                 关闭侧滑
                 */
                sp.edit().putBoolean("close",true).commit();
                MyApplication.setUser(user);
                it.setClass(ctx, MainActivity.class);
                startActivity(it);
                finish();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                proDialog.dismiss();
            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reg:
                it.setClass(context, RegistActivity.class);
                startActivity(it);
                break;
            case R.id.setpwd:
                it.setClass(context, FindPwdActivity.class);
                startActivity(it);
                break;
            case R.id.action:
                getValue();
                login();

                break;
        }
    }


}

