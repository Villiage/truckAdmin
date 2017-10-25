package com.fxlc.truckadmin;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by cyd on 2017/7/27.
 */

public class ListActiviity extends BaseActivity{
    public ListView listView;
    public PtrClassicFrameLayout mPtrFrame;
    public View dataEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.ptr_frame);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadData();
                mPtrFrame.refreshComplete();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });



        listView = (ListView) findViewById(R.id.list);
        dataEmptyView = findViewById(R.id.empty);

        dataEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void loadData(){




    }
    public void showEmptyView() {

        if (dataEmptyView != null) {
            dataEmptyView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }

    public void showDataView() {

        if (dataEmptyView != null) {
            dataEmptyView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }

    }


}
