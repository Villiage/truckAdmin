package com.fxlc.truckadmin.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.fxlc.truckadmin.BaseActivity;
import com.fxlc.truckadmin.Constant;
import com.fxlc.truckadmin.MyApplication;
import com.fxlc.truckadmin.R;
import com.fxlc.truckadmin.api.UserService;
import com.fxlc.truckadmin.net.HttpResult;
import com.fxlc.truckadmin.net.SimpleCallback;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FindPwdActivity extends BaseActivity implements View.OnClickListener {
    private TextView mPhoneView, mVerifyCodeView, mPwdView, mRePwdView;
    private String phone, pass, repass, verifyCode;
    private TextView getSmsTx;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd);
        initView();
        retrofit = MyApplication.getRetrofit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        title("找回密码");
    }

    private void initView() {

        mPhoneView = (TextView) findViewById(R.id.phone);
        mVerifyCodeView = (TextView) findViewById(R.id.verfiy_code);
        mPwdView = (TextView) findViewById(R.id.pwd);
        mRePwdView = (TextView) findViewById(R.id.repwd);
        getSmsTx = (TextView) findViewById(R.id.getverify);
        findViewById(R.id.getverify).setOnClickListener(this);
        findViewById(R.id.action).setOnClickListener(this);

        if (user != null && !TextUtils.isEmpty(user.getMobile())){
             mPhoneView.setText(user.getMobile());
            mPhoneView.setEnabled(false);
        }

    }


    private void getValue() {
        phone = mPhoneView.getText().toString();
        pass = mPwdView.getText().toString();
        repass = mRePwdView.getText().toString();
        verifyCode = mVerifyCodeView.getText().toString();


    }

    private boolean notEmpty() {
        if (TextUtils.isEmpty(phone)) {
            toast("请输入手机号");
            return false;
        }
        if (TextUtils.isEmpty(verifyCode)) {
            toast("请输入验证码");
            return false;
        }
        if (TextUtils.isEmpty(pass)) {
            toast("请输入密码");
            return false;
        }
        if (TextUtils.isEmpty(repass)) {
            toast("请再次输入密码");
            return false;
        }
        if (!pass.equals(repass)) {
            toast("两次密码输入不一致");
            return false;
        }
        return true;
    }

    private void change() {
        proDialog.show();
        Retrofit retrofit = MyApplication.getInstance().getRetrofit();
        UserService apiService = retrofit.create(UserService.class);

        HashMap map = new HashMap();
        map.put("mobile", phone);
        map.put("yzm", verifyCode);
        map.put("newpass", pass);


        Call<HttpResult> call = apiService.setpwd(map);
        call.enqueue(new SimpleCallback() {
            @Override
            public void onSuccess(HttpResult result) {
                proDialog.dismiss();
                toast("密码重置成功");
                finish();
            }
        });


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.action:
                getValue();
                if (notEmpty())
                    change();

                break;
            case R.id.getverify:
                if (TextUtils.isEmpty(phone)) {
                    getsms();
                } else {
                    toast("请输入手机号");
                }


                break;
        }
    }

    int count = 60;
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {

                count--;
                if (count > 0) {
                    getSmsTx.setText(count + "s");
                    getSmsTx.setClickable(false);

                    sendEmptyMessageDelayed(0, 1000);
                } else if (count == 0) {
                    getSmsTx.setText("获取验证码");
                    getSmsTx.setClickable(true);

                }


            }

        }
    };

    public void getsms() {
        count = 60;
        handler.sendEmptyMessage(0);

        //retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.102:8080/zklm/a/app/").build();
        retrofit = new Retrofit.Builder().baseUrl(Constant.Host_SMS).build();
        UserService service = retrofit.create(UserService.class);
        String mobile = mPhoneView.getText().toString();
        Call<ResponseBody> call = service.getSms(mobile);
        call.enqueue(new Callback<ResponseBody>() {


            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                toast("发送成功");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

}
