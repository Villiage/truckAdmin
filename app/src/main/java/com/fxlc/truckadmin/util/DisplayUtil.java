package com.fxlc.truckadmin.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by cyd on 2017/3/28.
 */

public class DisplayUtil {
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm;
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
   public static int getScreenWidth(Context context){
       DisplayMetrics dm = context.getResources().getDisplayMetrics();
       int width = dm.widthPixels;

       return width;
   }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public enum Size {
        with, height
    }

    public static int measureSize(View view, Size type) {

        int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        view.measure(widthSpec, heightSpec);
        int h = view.getMeasuredHeight();
        int w = view.getMeasuredWidth();
        switch (type) {
            case with:
                return w;
            case height:
                return h;
            default:
                return 0;
        }

    }

    public static void setWindowStatusBarColor(Activity context, int colorResId) {

        Window window = context.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(context.getResources().getColor(colorResId));
        }

    }
    public static void  translucentStatubar(Activity context){
        Window window = context.getWindow();
        WindowManager.LayoutParams localLayoutParams = window.getAttributes();
        localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);

    }
}
