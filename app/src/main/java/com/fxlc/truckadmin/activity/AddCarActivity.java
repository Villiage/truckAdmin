package com.fxlc.truckadmin.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxlc.truckadmin.BaseActivity;
import com.fxlc.truckadmin.R;
import com.fxlc.truckadmin.bean.Truck;
import com.fxlc.truckadmin.util.DialogUtil;
import com.fxlc.truckadmin.util.UriUtil;
import com.fxlc.truckadmin.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class AddCarActivity extends BaseActivity implements View.OnClickListener {

    Context context;
    Dialog carNoDialog;
    TextView brandTx;
    TextView carnoTx;
    EditText followMobileTx, ownerMobileTx;
    GridView gridView;
    GridAdapter gridAdapter;
    Truck truck = new Truck();
    StringBuffer sb = new StringBuffer();

    private static String[] Provinces = {"京", "津", "沪", "渝", "冀", "晋", "蒙", "陕", "辽", "吉", "黑", "湘", "皖", "鲁", "苏", "浙", "赣", "鄂",
            "甘", "闽", "贵", "粤", "青", "藏", "豫", "新", "桂", "云", "川", "宁", "琼", "✖"};
    private static String[] Nums = new String[10];
    private static String[] Chars = new String[24];
    private static List<String> characters = new ArrayList<>();


    int statu;
    ImageView manageImg;
    String truckerId;
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
        context = this;
        truckerId = getIntent().getStringExtra("truckerId");
        setContentView(R.layout.activity_add_car);
        findViewById(R.id.getBrand).setOnClickListener(this);
        findViewById(R.id.carno).setOnClickListener(this);

        followMobileTx = (EditText) findViewById(R.id.follow_mobile);
        ownerMobileTx = (EditText) findViewById(R.id.owner_mobile);
        manageImg = (ImageView) findViewById(R.id.manageimg);

        brandTx = (TextView) findViewById(R.id.brand);
        carnoTx = (TextView) findViewById(R.id.carno);
//        findViewById(R.id.get_drive_license).setOnClickListener(this);
        findViewById(R.id.add_snapshoot).setOnClickListener(this);
        findViewById(R.id.next).setOnClickListener(this);
        initDialog();
        int[] ids = {R.id.dialog_album, R.id.dialog_capture};
        choiceDialog = DialogUtil.createPickDialog(this, new String[]{"相册", "拍照"}, ids, this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        title("添加主车");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.getBrand:

                it.setClass(context, BrandActivity.class);
                it.putExtra("truck", truck);
                startActivityForResult(it, 101);

                break;

            case R.id.carno:
                carNoDialog.show();

                break;

            case R.id.add_snapshoot:
                choiceDialog.show();

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
                    it.setClass(context, HandCarActivity.class);
                    it.putExtra("truck", truck);
                    it.putExtra("follow", followMobileTx.getText().toString());
                    it.putExtra("boss", ownerMobileTx.getText().toString());
                    startActivity(it);
                }

                break;

        }
    }


    public void checkReadPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            reuestReadPermission();
        } else {
            pickImage();
        }

    }

    public void checkWritePermission() {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestWritePermission();
        } else {
            captureFile = Util.getFile("imgs");
            Util.photo(ctx, captureFile, CAPTURE_CODE);
        }

    }

    private void reuestReadPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PEMISSION_READ);

    }


    private void requestWritePermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PEMISSION_WRITE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PEMISSION_WRITE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                captureFile = Util.getFile("imgs");
                Log.d("cyd", captureFile.getPath());
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

    private boolean notEmpty() {
        if (TextUtils.isEmpty(ownerMobileTx.getText().toString()) || TextUtils.isEmpty(truck.getCarNo()) || TextUtils.isEmpty(followMobileTx.getText())) {
            toast("信息不完整");
            return false;
        } else if (truck.getImgs() == null) {
            toast("请添加相应的图片");
            return false;
        }
        return true;
    }

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

            truck.setImgs(path);
            Glide.with(ctx).load(path).fitCenter().into(manageImg);


        }

    }


    private void initDialog() {
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
                    if (sb.length() < 7) sb.append(gridAdapter.getItem(i));
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
                } else if (sb.length() == 7) {
                    carNoDialog.dismiss();
                }
                carnoTx.setText(sb.toString());
                truck.setCarNo(sb.toString());
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

    File captureFile;

    private void pickImage() {

        it = new Intent(Intent.ACTION_PICK);
        it.setType("image/*");
        startActivityForResult(it, ALBUM_CODE);
    }
}
