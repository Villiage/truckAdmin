package com.fxlc.truckadmin.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxlc.truckadmin.BaseActivity;
import com.fxlc.truckadmin.Constant;
import com.fxlc.truckadmin.MainActivity;
import com.fxlc.truckadmin.R;
import com.fxlc.truckadmin.bean.Cars;
import com.fxlc.truckadmin.bean.Truck;
import com.fxlc.truckadmin.util.BitmapUtil;
import com.fxlc.truckadmin.util.DialogUtil;
import com.fxlc.truckadmin.util.UriUtil;
import com.fxlc.truckadmin.util.Util;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HandCarActivity extends BaseActivity implements View.OnClickListener {

    TextView carnoTx;
    Dialog carNoDialog;

    Truck truck;
    StringBuffer sb = new StringBuffer();
    private static String[] Provinces = {"京", "津", "沪", "渝", "冀", "晋", "蒙", "陕", "辽",  "吉", "黑", "湘", "皖", "鲁", "苏", "浙", "赣", "鄂",
            "甘",  "闽", "贵", "粤", "青", "藏","豫", "新", "桂","云", "川", "宁", "琼", "✖"};
    private static String[] Nums = new String[10];
    private static String[] Chars = new String[24];
    private static List<String> characters = new ArrayList<>();
    GridView gridView;
    GridAdapter gridAdapter;
    int statu;
    ImageView manageImg;

    private String bossMobile;
    private String followMobile;
    Dialog choiceDialog;
    public static int ALBUM_CODE = 101;
    public static int CAPTURE_CODE = 102;
    public static int PEMISSION_WRITE = 100;
    private static int PEMISSION_READ = 103;

    static {
        for (int i = 0; i < 10; i++) {
            Nums[i] = String.valueOf(i);
        }
        int index = 0;
        for (char i = 'A'; i <= 'Z'; i++) {

            if (i != 'I' && i != 'O') {
                Chars[index] = String.valueOf(i);
                index++;
            }
        }
        Iterator<String> iterator1 = Arrays.asList(Nums).iterator();
        Iterator<String> iterator2 = Arrays.asList(Chars).iterator();
        for (int i = 0; i < Nums.length + Chars.length; i++) {
            if (i % 7 < 2) {
                characters.add(iterator1.next());
            } else {
                characters.add(iterator2.next());
            }

        }
        characters.add("✖");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        truck = (Truck) getIntent().getSerializableExtra("truck");

        followMobile = getIntent().getStringExtra("follow");
        bossMobile = getIntent().getStringExtra("boss");
        setContentView(R.layout.activity_handcar);


        manageImg = (ImageView) findViewById(R.id.manageimg);


        carnoTx = (TextView) findViewById(R.id.carno);


        findViewById(R.id.carno).setOnClickListener(this);
        findViewById(R.id.add_snapshoot).setOnClickListener(this);
        findViewById(R.id.next).setOnClickListener(this);
        initCarNoDialog();
        int[] ids = {R.id.dialog_album, R.id.dialog_capture};
        choiceDialog = DialogUtil.createPickDialog(this, new String[]{"相册", "拍照"}, ids, this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        title("添加挂车");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.add_snapshoot:

                choiceDialog.show();

                break;
            case R.id.carno:
                carNoDialog.show();

                break;
            case R.id.dialog_album:
                choiceDialog.dismiss();
                 checkReadPermission();
                break;
            case R.id.dialog_capture:
                checkWritePermission();
                choiceDialog.dismiss();
                break;
            case R.id.next:

                if (notEmpty()) {

                    upload();
                }
                break;

        }
    }

    private boolean notEmpty() {
        if (TextUtils.isEmpty(truck.getHandcarNo())) {
            toast("信息不完整");
            return false;
        } else if (truck.getHandImgs() == null) {
            toast("请添加相应的图片");
            return false;
        }
        return true;
    }

//

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

//            if (requestCode == 101) {
//                truck = (Truck) data.getSerializableExtra("truck");
//                brandTx.setText(truck.getBrand() + " " + truck.getStyle());
//            }
            String path = null;
            if (requestCode == ALBUM_CODE) {
                path = UriUtil.getRealFilePath(this, data.getData());
            }
            if (requestCode == CAPTURE_CODE) {
                path = captureFile.getPath();
            }

            truck.setHandImgs(path);
            Glide.with(ctx).load(path).fitCenter().into(manageImg);


        }

    }

    private void initCarNoDialog() {
        carNoDialog = new Dialog(this, R.style.dialog_alert);
        gridView = new GridView(this);
        gridView.setNumColumns(8);
        gridView.setBackgroundColor(Color.GRAY);
        gridView.setPadding(1, 1, 1, 1);
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -2);
        carNoDialog.setContentView(gridView, params);
        gridAdapter = new GridAdapter();
        gridAdapter.setValues(Arrays.asList(Provinces));
        gridView.setAdapter(gridAdapter);
        Window win = carNoDialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == adapterView.getCount() - 1) {
                    if (sb.length() > 0) sb.deleteCharAt(sb.length() - 1);
                    if (sb.length() == 0) statu = 0;
                } else {
                    if (sb.length() < 6) sb.append(gridAdapter.getItem(i));
                }
                if (sb.length() == 0) {
                    gridView.setNumColumns(8);
                    gridAdapter.setValues(Arrays.asList(Provinces));
                    gridAdapter.notifyDataSetChanged();
                } else if (statu == 0) {
                    statu = 1;
                    gridView.setNumColumns(7);
                    gridAdapter.setValues(characters);
                    gridAdapter.notifyDataSetChanged();
                } else if (sb.length() == 6) {
                    carNoDialog.dismiss();
                }
                carnoTx.setText(sb.toString());
                truck.setHandcarNo(sb.toString() + "挂");
            }
        });
    }

    class GridAdapter extends BaseAdapter {
        List<String> values;

        @Override
        public int getCount() {
            return values.size();
        }

        @Override
        public String getItem(int i) {
            return values.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public void setValues(List<String> values) {
            this.values = values;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null)
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_list_item, viewGroup, false);
            TextView txt = (TextView) view;
            txt.setBackgroundColor(getResources().getColor(R.color.white));
            txt.setText(values.get(i));
            return view;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 100) {

                toast("上传成功");
            }
            if (msg.what == 101) {

                toast((String) msg.obj);
            }

        }
    };

    public void upload() {
        proDialog.show();
        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder data = new MultipartBody.Builder();

        data.addFormDataPart("userId", user.getId());
        data.addFormDataPart("token", user.getToken());

        data.addFormDataPart("followMobile", followMobile);
        data.addFormDataPart("bossMobile", bossMobile);

        data.addFormDataPart("carNo", truck.getCarNo());
        data.addFormDataPart("handcarNo", truck.getHandcarNo());
//
        MediaType type = MediaType.parse("application/otcet-stream");

        RequestBody mana = RequestBody.create(type, BitmapUtil.cpPicToByte(truck.getImgs(),200));
        data.addFormDataPart("imgs", "imgs.img", mana);
        /*
          挂车
         */

        RequestBody hm = RequestBody.create(type, BitmapUtil.cpPicToByte(truck.getHandImgs(),200));
        data.addFormDataPart("handImgs", "handmanaImg.img", hm);


        Request request = new Request.Builder().url(Constant.Host + "saveTruck").post(data.build()).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
                proDialog.dismiss();
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                proDialog.dismiss();
                if (response.isSuccessful()) {

                    String json = response.body().string();
                    Log.d("cyd",json);
                    try {
                        JSONObject jsonObj = new JSONObject(json);
                        if (jsonObj.getBoolean("success")) {
                            it.setClass(ctx, MainActivity.class);

                            String body = jsonObj.getString("body");
                            Cars.CarlistBean car = new Gson().fromJson(body, Cars.CarlistBean.class);
                            it.putExtra("car", car);
                            startActivity(it);
                            handler.sendEmptyMessage(100);
                            finish();

                        } else {

                            String msg = jsonObj.getString("msg");
                            Message message = Message.obtain();
                            message.what = 101;
                            message.obj = msg;
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
        } else   {
            captureFile = Util.getFile("handImgs");
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
                captureFile = Util.getFile("handImgs");
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

    File captureFile;

    private void pickImage() {

        it = new Intent(Intent.ACTION_PICK);
        it.setType("image/*");
        startActivityForResult(it, ALBUM_CODE);
    }
}
