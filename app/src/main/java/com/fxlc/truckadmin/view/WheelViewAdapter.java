package com.fxlc.truckadmin.view;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cyd on 2017/3/24.
 */


public class WheelViewAdapter extends BaseAdapter {
    private List<String> mData;
    private int showCount;
    private boolean cycleable;

    public WheelViewAdapter(List<String> data, int showCount, boolean cycleable) {
        this.mData = data;
        this.showCount = showCount;
        this.cycleable = cycleable;
    }

    @Override
    public int getCount() {
        if(cycleable)
            return  Integer.MAX_VALUE;
        return mData.size() + showCount - 1;
    }

    @Override
    public Object getItem(int i) {
        if (i < showCount / 2
                || (!cycleable && i >= mData.size() + showCount / 2) )
            return "";
        else {
            return mData.get((i - showCount / 2)%mData.size());
        }

    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView text = null;
        if (view == null) {
            text = new TextView(viewGroup.getContext());
        }else {
            text = (TextView) view;
        }
        text.setGravity(Gravity.CENTER);
        text.setSingleLine(true);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        text.setPadding(10, 10, 10, 10);
        text.setText((String) getItem(i));
        return text;
    }
}

