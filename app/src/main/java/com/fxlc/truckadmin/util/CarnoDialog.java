package com.fxlc.truckadmin.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.fxlc.truckadmin.R;
import com.fxlc.truckadmin.activity.AddCarActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cyd on 2017/10/18.
 */

public class CarnoDialog {

    private static String[] Provinces = {"京", "津", "沪", "渝", "冀", "晋", "蒙", "陕", "辽", "吉", "黑", "湘", "皖", "鲁", "苏", "浙", "赣", "鄂",
            "甘", "闽", "贵", "粤", "青", "藏", "豫", "新", "桂", "云", "川", "宁", "琼", "✖"};
    private static String[] Nums = new String[10];
    private static String[] Chars = new String[24];
    private static List<String> characters = new ArrayList<>();
    private static StringBuffer sb = new StringBuffer();
    private static int statu;
    private static GridView gridView;
    private static GridAdapter gridAdapter;
    private static Dialog carNoDialog;

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

    private void generate(Context context, final TextView carnoTx) {


        carNoDialog = new Dialog(context, R.style.dialog_alert);
        gridView = new GridView(context);
        gridView.setNumColumns(8);
        gridView.setBackgroundColor(Color.GRAY);
        gridView.setPadding(1, 1, 1, 1);
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(-1, -2);
        carNoDialog.setContentView(gridView, params);
        gridAdapter = new GridAdapter(context);
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

            }
        });
    }


    class GridAdapter extends BaseAdapter {
        private Context context;

        public GridAdapter(Context context) {
            this.context = context;
        }

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
            txt.setBackgroundColor(context.getResources().getColor(R.color.white));
            txt.setText(values.get(i));
            return view;
        }
    }
}
