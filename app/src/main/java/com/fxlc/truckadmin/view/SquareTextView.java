package com.fxlc.truckadmin.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by cyd on 2017/5/27.
 */


public  class SquareTextView extends android.support.v7.widget.AppCompatTextView {

    public SquareTextView(Context context) {
        super(context);
    }

    public SquareTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

  

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}