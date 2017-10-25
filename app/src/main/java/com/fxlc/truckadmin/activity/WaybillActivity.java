package com.fxlc.truckadmin.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxlc.truckadmin.BaseActivity;
import com.fxlc.truckadmin.Constant;
import com.fxlc.truckadmin.MyApplication;
import com.fxlc.truckadmin.R;
import com.fxlc.truckadmin.api.BillService;
import com.fxlc.truckadmin.bean.BillInfo;
import com.fxlc.truckadmin.net.HttpCallback;
import com.fxlc.truckadmin.net.HttpResult;
import com.fxlc.truckadmin.util.BitmapUtil;
import com.fxlc.truckadmin.util.DialogUtil;
import com.fxlc.truckadmin.util.UriUtil;
import com.fxlc.truckadmin.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;

public class WaybillActivity extends BaseActivity implements View.OnClickListener {

    TextView carnoTx, billnoTx, bossMobileTx, followMobileTx;
    TextView loadWeightTx, unloadWeightTx;
    TextView insuranceTx;
    CheckBox checkBox;
    ImageView loadImg, unloadImg;

    String loadImgPath, unloadImgPath;
    int type = 1;
    Dialog choiceDialog;
    public static int ALBUM_CODE = 101;
    public static int CAPTURE_CODE = 102;
    public static int PEMISSION_WRITE = 100;
    private static int PEMISSION_READ = 103;

    BillInfo billInfo;
    String sourceId, carId;
    int statu;
    private Dialog imgDialog;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waybill);

        billInfo = (BillInfo) getIntent().getSerializableExtra("billInfo");
        sourceId = getIntent().getStringExtra("sourceId");
        carId = getIntent().getStringExtra("carId");

        carnoTx = (TextView) findViewById(R.id.carno);
        followMobileTx = (TextView) findViewById(R.id.follow_mobile);
        bossMobileTx = (TextView) findViewById(R.id.boss_mobile);
        billnoTx = (TextView) findViewById(R.id.billno);
        loadWeightTx = (TextView) findViewById(R.id.load_weight);
        unloadWeightTx = (TextView) findViewById(R.id.unload_weight);
        loadImg = (ImageView) findViewById(R.id.loadimg);
        unloadImg = (ImageView) findViewById(R.id.unloadimg);

        insuranceTx = (TextView) findViewById(R.id.insurance);
        checkBox = (CheckBox) findViewById(R.id.insure_check);

        findViewById(R.id.load_submit).setOnClickListener(this);
        findViewById(R.id.unload_submit).setOnClickListener(this);
        findViewById(R.id.fee_info).setOnClickListener(this);

        loadImg.setOnClickListener(this);
        unloadImg.setOnClickListener(this);
        int[] ids = {R.id.dialog_album, R.id.dialog_capture};
        choiceDialog = DialogUtil.createPickDialog(this, new String[]{"相册", "拍照"}, ids, this);

        setValue();


    }


    private void setValue() {

        Glide.with(this).load(billInfo.getLoadBill()).into(loadImg);
        Glide.with(this).load(billInfo.getUnloadBill()).into(unloadImg);
        loadWeightTx.setText(billInfo.getLoadWeight());
        unloadWeightTx.setText(billInfo.getUnloadWeight());

        carnoTx.setText("车牌号:" + billInfo.getCarNo() + " " + billInfo.getHandcarNo());
        bossMobileTx.setText("车主手机号：" + billInfo.getBossMobile());
        followMobileTx.setText("随车手机号：" + billInfo.getFollowMobile());
        bossMobileTx.setOnClickListener(this);
        followMobileTx.setOnClickListener(this);

        billnoTx.setText("订单编号：" + billInfo.getOrderNo());

        insuranceTx.setText("运费险：￥" + billInfo.getInsurance());
        checkBox.setChecked(billInfo.getIsInsurance() == 1);
        if (!TextUtils.isEmpty(billInfo.getOrderId())) {
            checkBox.setEnabled(false);
        }

        if (!TextUtils.isEmpty(billInfo.getOrderStatus())) {
            statu = Integer.parseInt(billInfo.getOrderStatus());
        }

        if (statu == 5) {
            findViewById(R.id.load_submit).setVisibility(View.GONE);
            findViewById(R.id.unload_submit).setVisibility(View.GONE);
            loadWeightTx.setEnabled(false);
            unloadWeightTx.setEnabled(false);
            initImgDialog();
        }
        if (statu >= 3) {
            findViewById(R.id.fee_info).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        title(billInfo.getSourceName());

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.load_submit:

                if (TextUtils.isEmpty(loadWeightTx.getText().toString())) {
                    toast("净重不能为空");
                    return;
                }

                if (TextUtils.isEmpty(billInfo.getLoadBill())) {

                    if (TextUtils.isEmpty(loadImgPath)) {
                        toast("装货磅单不能为空");
                        return;
                    }

                }
                loadOrder();
                break;
            case R.id.unload_submit:
                findViewById(R.id.unload_submit).setEnabled(false);
                if (TextUtils.isEmpty(unloadWeightTx.getText().toString())) {
                    toast("净重不能为空");
                    return;
                }


                if (TextUtils.isEmpty(billInfo.getUnloadBill())) {
                    if (TextUtils.isEmpty(unloadImgPath)) {
                        toast("卸货货磅单不能为空");
                        return;
                    }

                }
                if (TextUtils.isEmpty(billInfo.getOrderId())) {
                    toast("请先上传装货地信息");
                }
                unloadOrder();
                break;

            case R.id.loadimg:
                type = 1;
                if (statu == 5) {
                    Glide.with(ctx).load(billInfo.getLoadBill()).centerCrop().into(img);
                    imgDialog.show();

                } else {
                    choiceDialog.show();

                }
                break;
            case R.id.unloadimg:
                type = 2;
                if (statu == 5) {
                    Glide.with(ctx).load(billInfo.getUnloadBill()).centerCrop().into(img);
                    imgDialog.show();
                } else {
                    choiceDialog.show();
                }
                break;
            case R.id.dialog_album:
                choiceDialog.dismiss();
                checkReadPermission();
                break;
            case R.id.dialog_capture:
                choiceDialog.dismiss();
                checkWritePermission();
                break;

            case R.id.fee_info:

                it.setClass(ctx, FeeInfoActivity.class);
                it.putExtra("orderId", billInfo.getOrderId());

                startActivity(it);

                break;

            case R.id.boss_mobile:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + billInfo.getBossMobile()));
                startActivity(intent);

                break;
            case R.id.follow_mobile:
                Intent intents = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + billInfo.getFollowMobile()));
                startActivity(intents);
                break;
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 100) {
                if (!TextUtils.isEmpty(billInfo.getCarNo())) {
                    billnoTx.setText("订单编号：" + billInfo.getOrderNo());
                    checkBox.setEnabled(false);
                }
                if (statu >= 3) {
                    findViewById(R.id.fee_info).setVisibility(View.VISIBLE);
                }

                toast("保存成功");


            } else {
                if (msg.what == 108) {
                    findViewById(R.id.unload_submit).setEnabled(true);
                } else if (msg.what == 109) {
                    findViewById(R.id.load_submit).setEnabled(true);
                }
                String err = (String) msg.obj;
                toast(err);
            }

        }
    };

    OkHttpClient client = new OkHttpClient();

    private void initImgDialog() {
        imgDialog = new Dialog(ctx, R.style.dialog_alert);

        imgDialog.setContentView(R.layout.dialog_img);
        img = imgDialog.findViewById(R.id.img);
        Window win = imgDialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);
    }

    private void loadOrder() {
        findViewById(R.id.load_submit).setEnabled(false);
        proDialog.show();

        MultipartBody.Builder data = new MultipartBody.Builder();

        data.addFormDataPart("userId", user.getId());
        data.addFormDataPart("token", user.getToken());
        if (!TextUtils.isEmpty(billInfo.getOrderId()))
            data.addFormDataPart("orderId", billInfo.getOrderId());
        if (!TextUtils.isEmpty(billInfo.getCarId()))
            data.addFormDataPart("carId", billInfo.getCarId());
        data.addFormDataPart("sourceId", sourceId);
        data.addFormDataPart("isInsurance", checkBox.isChecked() ? "1" : "0");
        data.addFormDataPart("loadWeight", loadWeightTx.getText().toString());

        MediaType type = MediaType.parse("application/otcet-stream");
        if (!TextUtils.isEmpty(loadImgPath)) {
            RequestBody body = RequestBody.create(type, BitmapUtil.cpPicToByte(loadImgPath, 200));
            data.addFormDataPart("loadImg", "loadimg.img", body);
        }


        Request request = new Request.Builder().url(Constant.Host + "loadOrder").post(data.build()).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
                handler.sendEmptyMessage(109);
                proDialog.dismiss();
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                proDialog.dismiss();
                handler.sendEmptyMessage(109);

                if (response.isSuccessful()) {
                    try {
                        String jsonStr = response.body().string();

                        JSONObject jsonObj = new JSONObject(jsonStr);
                        JSONObject body = jsonObj.getJSONObject("body");
                        billInfo.setOrderId(body.getString("orderId"));
                        billInfo.setOrderNo(body.getString("orderNo"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    handler.sendEmptyMessage(100);

                }
            }
        });


    }

    private void unloadOrder() {
        findViewById(R.id.unload_submit).setEnabled(false);
        proDialog.show();

        MultipartBody.Builder data = new MultipartBody.Builder();

        data.addFormDataPart("userId", user.getId());
        data.addFormDataPart("token", user.getToken());

        data.addFormDataPart("orderId", billInfo.getOrderId());

        data.addFormDataPart("unloadWeight", unloadWeightTx.getText().toString());

        MediaType type = MediaType.parse("application/otcet-stream");
        if (!TextUtils.isEmpty(unloadImgPath)) {
            RequestBody body = RequestBody.create(type, BitmapUtil.cpPicToByte(unloadImgPath, 200));
            data.addFormDataPart("unloadImg", "unloadimg.img", body);
        }


        Request request = new Request.Builder().url(Constant.Host + "unloadOrder").post(data.build()).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
                proDialog.dismiss();
                handler.sendEmptyMessage(108);

            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                proDialog.dismiss();
                handler.sendEmptyMessage(108);
                if (response.isSuccessful()) {
                    try {
                        String jsonStr = response.body().string();
                        Log.d("cyd", jsonStr);
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        boolean success = jsonObj.getBoolean("success");
                        if (success) {
                            statu = 3;
                            handler.sendEmptyMessage(100);
                        } else {
                            String msg = jsonObj.getString("msg");
                            Message message = Message.obtain();
                            message.obj = msg;
                            message.what = 101;
                            handler.sendMessage(message);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });


    }

    public void checkReadPermission() {
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            reuestReadPermission();
        } else {
            pickImage();
        }

    }

    public void checkWritePermission() {

        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(ctx, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestWritePermission();
        } else {
            captureFile = Util.getFile("loadImg");
            Util.photo(ctx, captureFile, CAPTURE_CODE);
        }

    }

    private void reuestReadPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PEMISSION_READ);

    }


    private void requestWritePermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PEMISSION_WRITE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PEMISSION_WRITE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureFile = Util.getFile("loadImg");
                Util.photo(ctx, captureFile, CAPTURE_CODE);
            } else {
                // Permission Denied
                Toast.makeText(this, "请在 设置->权限 中授权拍照", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PEMISSION_READ) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            } else {
                // Permission Denied
                Toast.makeText(this, "请在 设置->权限 中授权SD卡", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String path = null;
            if (requestCode == ALBUM_CODE) {
                path = UriUtil.getRealFilePath(this, data.getData());
            }
            if (requestCode == CAPTURE_CODE) {
                path = captureFile.getPath();
            }

            if (type == 1) {
                Glide.with(ctx).load(loadImgPath = path).fitCenter().into(loadImg);
            } else {
                Glide.with(ctx).load(unloadImgPath = path).fitCenter().into(unloadImg);
            }


        }

    }


    File captureFile;

    private void pickImage() {

        it = new Intent(Intent.ACTION_PICK);
        it.setType("image/*");
        startActivityForResult(it, ALBUM_CODE);
    }


}
