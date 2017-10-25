package com.fxlc.truckadmin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Danny on 2016/5/24.
 */
public class FlowLayout extends FrameLayout {
    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    int mHeight,mWidth;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int rowTop = 0;
        int cCount = getChildCount();

        int willWith = 0;
        int rowLeft = 0;
        int lineNo = 0;
        int childWidth = 0;
        int childHeight = 0;
        for (int i = 0; i < cCount; i++) {


            View child = getChildAt(i);


            LayoutParams lp = (LayoutParams) child.getLayoutParams();




            childWidth = child.getMeasuredWidth() + lp.topMargin + lp.bottomMargin;

            childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;



            willWith += childWidth;

            if (willWith > getWidth() - getPaddingLeft() - getPaddingRight()) {

                lineNo++;

                willWith = childWidth;

                rowLeft = 0;
                rowTop = lineNo * childHeight ;
            }

            int left = rowLeft + lp.leftMargin;
            int right = left + child.getMeasuredWidth();

            int top = rowTop + lp.topMargin;
            int bottom = top + child.getMeasuredHeight();

            rowLeft += childWidth;


            child.layout(left, top, right, bottom);

        }
        mHeight = rowTop + childHeight;
        Log.d("onMeasure","onLayout " + mHeight);
        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        // wrap_content
        int width = 0;
        int height = 0;

        int lineWidth = 0;
        int lineHeight = 0;

        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++)
        {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE)
            {
                if (i == cCount - 1)
                {
                    width = Math.max(lineWidth, width);
                    height += lineHeight;
                }
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams lp = (MarginLayoutParams) child
                    .getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.leftMargin
                    + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin
                    + lp.bottomMargin;

            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight())
            {
                width = Math.max(width, lineWidth);
                lineWidth = childWidth;
                height += lineHeight;
                lineHeight = childHeight;
            } else
            {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            if (i == cCount - 1)
            {
                width = Math.max(lineWidth, width);
                height += lineHeight;
            }
        }
        setMeasuredDimension(
                //
                modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width + getPaddingLeft() + getPaddingRight(),
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom()//
        );

    }
}
