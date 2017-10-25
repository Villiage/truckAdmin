package com.fxlc.truckadmin.activity;

import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.fxlc.truckadmin.BaseActivity;
import com.fxlc.truckadmin.MyApplication;
import com.fxlc.truckadmin.R;
import com.fxlc.truckadmin.util.DataCleanManager;

import java.io.File;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    TextView cacheTxt;
    TextView mobileTx;
    File cacheFile;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        findViewById(R.id.action).setOnClickListener(this);
        findViewById(R.id.clear_cache).setOnClickListener(this);
        findViewById(R.id.setpwd).setOnClickListener(this);

        mobileTx = (TextView) findViewById(R.id.mobile);
        cacheTxt = (TextView) findViewById(R.id.cache);

//        mobileTx.setText(user.getMobile());

        cacheFile = new File(Environment.getExternalStorageDirectory(), "images");
        getCache();

    }


    @Override
    protected void onResume() {
        super.onResume();
        title("我的");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.clear_cache:
                proDialog.setMessage("请稍候...");
                proDialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DataCleanManager.cleanApplicationData(ctx, cacheFile.getPath());
                        getCache();
                        proDialog.dismiss();
                    }
                }, 1000 * 2);

                break;

            case R.id.action:

                sp.edit().remove("user").commit();
                MyApplication.getInstance().exit();
                it.setClass(ctx, LoginActivity.class);
                startActivity(it);
                finish();
                break;
            case R.id.setpwd:

                it.setClass(ctx, FindPwdActivity.class);

                startActivity(it);

                break;

        }
    }

    private void getCache() {
        try {

            String size = DataCleanManager.getCacheSize(cacheFile);
            cacheTxt.setText(size);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    String apkUrl = "http://47.95.122.121:80/truckCard/trucker-release.apk";
    public void downLoad(String url){

        DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(url));
        //设置状态栏中显示Notification
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        //设置可用的网络类型
        request.setAllowedNetworkTypes( DownloadManager.Request.NETWORK_WIFI);
        //不显示下载界面
//        request.setVisibleInDownloadsUi(true);

        //创建文件的下载路径
//        File folder = Environment.getExternalStoragePublicDirectory(DOWNLOAD_FOLDER_NAME);
//        if (!folder.exists() || !folder.isDirectory()) {
//            folder.mkdirs();
//        }
//        //指定下载的路径为和上面创建的路径相同
//        request.setDestinationInExternalPublicDir(DOWNLOAD_FOLDER_NAME, DOWNLOAD_FILE_NAME);

        //设置文件类型
        request.setMimeType("application/vnd.android.package-archive");
        //将请求加入请求队列会 downLoadManager会自动调用对应的服务执行者个请求
        manager.enqueue(request);

    }
}
