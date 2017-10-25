package com.fxlc.truckadmin.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by cyd on 2017/3/24.
 */

public class WheelView extends ListView {
    public static final String TAG = "WheelView";
    public static final int showCount = 5;
    private int freeScrollTime = 200;//ms
    private int itemHeight;
    private int selectIndex = -1;


    public WheelView(Context context) {
        super(context);
        init();
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public void init() {

        setVerticalScrollBarEnabled(false);
        setFastScrollEnabled(false);
        setDividerHeight(0);
        setOnScrollListener(osl);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() > 0) {
            View view = getChildAt(0);
            itemHeight = view.getMeasuredHeight();
            setMeasuredDimension(widthMeasureSpec, itemHeight * showCount);

        }
    }

    public int getItemHeight() {
        return itemHeight;
    }

    @Override
    public void fling(int velocityY) {

        super.fling(velocityY / 10);

    }


    private OnScrollListener osl = new OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
            if (i == SCROLL_STATE_IDLE) {
                adjust();

            }

        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisiableItem, int visiableItemCount, int totalItemCount) {
            refresh(firstVisiableItem, visiableItemCount);

        }


    };

    public void adjust() {

        View itemView = getChildAt(0);
        if (itemView != null) {
            float deltaY = itemView.getY();

            if (Math.abs(deltaY) < itemHeight / 2) {
                smoothScrollBy((int) deltaY, 0);
            } else {
                smoothScrollBy((int) (itemHeight + deltaY), 0);
            }
        }
        if (oscl != null)
            oscl.onSelect(selectIndex);


    }


    public int getSelectIndex() {
        return selectIndex;
    }

    public void refresh(int firstVisiableItem, int visiableItemCount) {

        if (getChildAt(0) == null) {
            return;
        }
        int offset = showCount / 2;
        int position = 0;
        if (Math.abs(getChildAt(0).getY()) <= itemHeight / 2) {
            position = firstVisiableItem + offset;
        } else {
            position = firstVisiableItem + offset + 1;
        }
        if (position == selectIndex + offset) {
            return;
        }
        selectIndex = (position - offset) % getCount();
        for (int i = 0; i < visiableItemCount; i++) {
            TextView txt = (TextView) getChildAt(i);
            if (firstVisiableItem + i == position) {
                txt.setTextColor(Color.DKGRAY);
            } else txt.setTextColor(Color.LTGRAY);
        }

    }

    public interface OnSelectChangeLisenter {

        void onSelect(int p);

    }

    private OnSelectChangeLisenter oscl;

    public void setOscl(OnSelectChangeLisenter oscl) {
        this.oscl = oscl;
    }
}
