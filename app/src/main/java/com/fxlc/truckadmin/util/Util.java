package com.fxlc.truckadmin.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.util.List;

/**
 * Created by cyd on 2017/9/22.
 */

public class Util {

    public static File getFile(String fileName) {
        File imagePath = new File(Environment.getExternalStorageDirectory(), "images");
        if (!imagePath.exists()) imagePath.mkdirs();

        fileName += fileName + System.currentTimeMillis();
        File captureFile = new File(imagePath, fileName);
        return captureFile;
    }

    public static void photo(Context context, File captureFile,int CAPTURE_CODE) {




        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        Uri contentUri = FileProvider.getUriForFile(context, "com.fxlc.truckadmin.fileprovider", captureFile);
        //兼容版本处理，因为 intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION) 只在5.0以上的版本有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ClipData clip = ClipData.newUri(context.getContentResolver(), "A photo", contentUri);
            intent.setClipData(clip);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            List<ResolveInfo> resInfoList =
                    context.getPackageManager()
                            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, contentUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        ((Activity)context).startActivityForResult(intent, CAPTURE_CODE);

    }
}
