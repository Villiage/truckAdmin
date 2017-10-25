package com.fxlc.truckadmin.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by cyd on 2017/3/24.
 */

public class CycleWheelView extends ListView {
    public static final String TAG = "CycleWheelView";
    public static final int showCount = 5;
    private int freeScrollTime = 200;//ms
    private int itemHeight;
    private int selectIndex = -1;
    private List<String> mData;
    private WheelViewAdapter mAdapter;
    private boolean cycleEnable;

    public CycleWheelView(Context context) {
        super(context);
        init();
    }

    public CycleWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CycleWheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CycleWheelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
            Log.d("cyd","" + itemHeight);
            setMeasuredDimension(widthMeasureSpec, itemHeight * showCount);

        }
    }

    public int getItemHeight() {
        return itemHeight;
    }

    @Override
    public void fling(int velocityY) {

        super.fling(velocityY/10);

    }


    public void setData(List<String> data) {
        this.mData = data;
        mAdapter = new WheelViewAdapter(data, showCount, cycleEnable);
        setAdapter(mAdapter);
//        setListViewHeight(this);
    }

    public void setListViewHeight(ListView listView) {
        //获取listView的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        //listAdapter.getCount()返回数据项的数目
        for (int i = 0,len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() *  (listAdapter .getCount() - 1));
        listView.setLayoutParams(params);
    }



    public void setCycleEnable(boolean cycleEnable) {
        this.cycleEnable = cycleEnable;
        if(cycleEnable){
            setSelection(mData.size() * (Integer.MAX_VALUE/2 / mData.size())+ selectIndex);
        }
    }

    private OnScrollListener osl = new OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
            if (i == SCROLL_STATE_IDLE) {
                adjust();
                Log.d("scroll","onScrollStateChanged");
            }

        }

        @Override
        public void onScroll(AbsListView absListView, int firstVisiableItem, int visiableItemCount, int totalItemCount) {
            refresh(firstVisiableItem,visiableItemCount);

        }



    };

    public void adjust() {

            View itemView = getChildAt(0);
            if (itemView != null) {
                float deltaY = itemView.getY();
                if (deltaY == 0) {
                    return;
                }
                if (Math.abs(deltaY) < itemHeight / 2) {
                    smoothScrollBy((int)deltaY, 0);
                } else {
                    smoothScrollBy((int)(itemHeight + deltaY), 0);
                }
            }


    }


    public int getSelectIndex() {
        return selectIndex;
    }

    public void refresh(int firstVisiableItem, int visiableItemCount) {

        if (getChildAt(0) == null) {
            return;
        }
        int offset = showCount/2;
        int position = 0;
        if (Math.abs(getChildAt(0).getY()) <= itemHeight / 2) {
            position = firstVisiableItem + offset;
        } else {
            position = firstVisiableItem + offset + 1;
        }
        if(position == selectIndex + offset){
            return;
        }
        selectIndex = (position - offset) % mData.size();
        for(int i = 0;i<  visiableItemCount;i++){
            TextView txt = (TextView) getChildAt(i);
              if( firstVisiableItem + i == position){
                  txt.setTextColor(Color.DKGRAY);
              }else txt.setTextColor(Color.LTGRAY);
        }

    }

   public interface OnSelectChangeLisenter{

        void onSelect(int p);

   }
   private OnSelectChangeLisenter oscl;

    public void setOscl(OnSelectChangeLisenter oscl) {
        this.oscl = oscl;
    }
}
