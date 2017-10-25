package com.fxlc.truckadmin.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fxlc.truckadmin.R;
import com.fxlc.truckadmin.view.CycleWheelView;

import java.util.List;

/**
 * Created by cyd on 2017/4/19.
 */

public class DialogUtil {

    public static Dialog createPickDialog(Context context, String[] items, int [] ids,View.OnClickListener ocl) {

        final Dialog d = new Dialog(context, R.style.dialog_alert);

        LinearLayout linear = new LinearLayout(context);
        linear.setBackgroundColor(context.getResources().getColor(R.color.white));
        linear.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < items.length; i++) {
            TextView textView = new TextView(context);
            textView.setTag(i);
            textView.setOnClickListener(ocl);
            textView.setGravity(Gravity.CENTER);
            textView.setText(items[i]);
            textView.setId(ids[i]);
            textView.setPadding(0, DisplayUtil.dp2px(context, 10), 0, DisplayUtil.dp2px(context, 10));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
            if (i < items.length - 1) params.bottomMargin = 5;
            linear.addView(textView, params);


        }


        d.setContentView(linear);

        Window win = d.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

        return d;
    }


    public static Dialog createDialog(Context context, String[] items, View.OnClickListener ocl) {

        final Dialog d = new Dialog(context, R.style.dialog_alert);

        LinearLayout linear = new LinearLayout(context);
        linear.setOrientation(LinearLayout.VERTICAL);
        int[] ids = {R.id.dialog_item1, R.id.dialog_item2, R.id.dialog_item3};
        for (int i = 0; i < items.length; i++) {
            TextView textView = new TextView(context);
            textView.setTag(i);
            textView.setOnClickListener(ocl);
            textView.setGravity(Gravity.CENTER);
            textView.setText(items[i]);
            textView.setId(ids[i]);
            textView.setPadding(0, DisplayUtil.dp2px(context, 10), 0, DisplayUtil.dp2px(context, 10));
            textView.setBackground(DrawableUtil.createSelecotorDrawable(5));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
            if (i < items.length - 1) params.bottomMargin = 5;
            linear.addView(textView, params);


        }
        TextView textView = new TextView(context);
        textView.setId(R.id.dialog_close);
        textView.setGravity(Gravity.CENTER);
        textView.setText("取消");
        textView.setPadding(0, DisplayUtil.dp2px(context, 10), 0, DisplayUtil.dp2px(context, 10));
        textView.setBackground(DrawableUtil.createSelecotorDrawable(0));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.topMargin = 20;
        linear.addView(textView, params);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });

        d.setContentView(linear);

        Window win = d.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

        return d;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static Dialog createWBDialog(Context context, String[] items, View.OnClickListener ocl) {

        final Dialog d = new Dialog(context, R.style.dialog_alert);

        LinearLayout linear = new LinearLayout(context);
        linear.setOrientation(LinearLayout.VERTICAL);
        int[] ids = {R.id.dialog_item1, R.id.dialog_item2, R.id.dialog_item3};
        for (int i = 0; i < items.length; i++) {
            TextView textView = new TextView(context);
            textView.setTag(i);
            textView.setOnClickListener(ocl);
            textView.setGravity(Gravity.CENTER);
            textView.setText(items[i]);
            textView.setTextColor(context.getResources().getColor(R.color.text_blue));
            textView.setId(ids[i]);
            textView.setPadding(0, DisplayUtil.dp2px(context, 10), 0, DisplayUtil.dp2px(context, 10));
            textView.setBackground(DrawableUtil.createSelecotorDrawable(5));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
//            if (i < items.length - 1) params.bottomMargin = 5;
            linear.addView(textView, params);


        }
        TextView textView = new TextView(context);
        textView.setId(R.id.dialog_close);
        textView.setGravity(Gravity.CENTER);
        textView.setText("取消");
        textView.setTextColor(context.getResources().getColor(R.color.text_blue));
        textView.setPadding(0, DisplayUtil.dp2px(context, 10), 0, DisplayUtil.dp2px(context, 10));
        textView.setBackgroundColor(Color.WHITE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        linear.addView(textView, params);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        d.setContentView(linear);
        Window win = d.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.BOTTOM;

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

        return d;
    }


    public static Dialog createListDialog(Context context, List<String> items, View.OnClickListener ocl) {

        final Dialog d = new Dialog(context, R.style.dialog_alert);
        int pad = DisplayUtil.dp2px(context, 10);
        LinearLayout linear = new LinearLayout(context);
        linear.setOrientation(LinearLayout.VERTICAL);
        linear.setBackgroundColor(context.getResources().getColor(R.color.divider));
        TextView cancelView = new TextView(context);
        cancelView.setPadding(pad, pad, pad, pad);
        cancelView.setText("取消");
        cancelView.setId(R.id.dialog_close);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });


        for (int i = 0; i < items.size(); i++) {

            TextView textView = new TextView(context);
            textView.setTag(i);
            textView.setOnClickListener(ocl);
            textView.setGravity(Gravity.CENTER);
            textView.setText(items.get(i));
            textView.setPadding(0, DisplayUtil.dp2px(context, 10), 0, DisplayUtil.dp2px(context, 10));
            textView.setBackgroundColor(Color.WHITE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
            params.topMargin = 1;
            linear.addView(textView, params);
            textView.setOnClickListener(ocl);
        }


        d.setContentView(linear);

        Window win = d.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.BOTTOM;

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

        return d;
    }
    public static Dialog createSelDialog(Context context, String[] items, View.OnClickListener ocl) {

        final Dialog d = new Dialog(context, R.style.dialog_alert);
        int pad = DisplayUtil.dp2px(context, 10);
        LinearLayout linear = new LinearLayout(context);
        linear.setOrientation(LinearLayout.VERTICAL);
        linear.setBackgroundColor(context.getResources().getColor(R.color.divider));
        TextView cancelView = new TextView(context);
        cancelView.setPadding(pad, pad, pad, pad);
        cancelView.setText("取消");
        cancelView.setId(R.id.dialog_close);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });



        for (int i = 0; i < items.length; i++) {
            TextView textView = new TextView(context);
            textView.setTag(i);
            textView.setOnClickListener(ocl);
            textView.setGravity(Gravity.CENTER);
            textView.setText(items[i]);
            textView.setPadding(0, DisplayUtil.dp2px(context, 10), 0, DisplayUtil.dp2px(context, 10));
            textView.setBackgroundColor(Color.WHITE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
            params.topMargin = 1;
            linear.addView(textView, params);
            textView.setOnClickListener(ocl);
        }


        d.setContentView(linear);

        Window win = d.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.BOTTOM;

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

        return d;
    }

    public static Dialog createWheelDialog(Context context, final List<String> list1, final List<String> list2, final ValueLisener lis) {
        final Dialog d = new Dialog(context, R.style.dialog_alert);

        d.setContentView(R.layout.dialog_wheel);

        final CycleWheelView wheelView1 = (CycleWheelView) d.findViewById(R.id.wheel1);
        wheelView1.setCycleEnable(false);
        wheelView1.setData(list1);


        final CycleWheelView wheelView2 = (CycleWheelView) d.findViewById(R.id.wheel2);
        wheelView2.setCycleEnable(false);
        wheelView2.setData(list2);


        d.findViewById(R.id.dialog_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        d.findViewById(R.id.dialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = list1.get(wheelView1.getSelectIndex()) + "." + list2.get(wheelView2.getSelectIndex());
                lis.onValue(value);
                d.dismiss();
            }
        });


        Window win = d.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.BOTTOM;

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

        return d;
    }



    public  interface ValueLisener{

         void onValue(String value);
    }

    public interface  DateSelLisener{

           void onSel(String year, String month);
    }
}
