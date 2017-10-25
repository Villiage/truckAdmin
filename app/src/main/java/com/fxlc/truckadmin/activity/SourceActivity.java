package com.fxlc.truckadmin.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fxlc.truckadmin.ListActiviity;
import com.fxlc.truckadmin.MyApplication;
import com.fxlc.truckadmin.R;
import com.fxlc.truckadmin.api.BillService;
import com.fxlc.truckadmin.bean.BankCards;
import com.fxlc.truckadmin.bean.BankList;
import com.fxlc.truckadmin.bean.SourceList;
import com.fxlc.truckadmin.net.HttpCallback;
import com.fxlc.truckadmin.net.HttpResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

public class SourceActivity extends ListActiviity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_source);
    }

    SourceList sourceList;

    public void listSource() {
        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();

        BillService service = retrofit.create(BillService.class);

        Call<HttpResult<SourceList>> call = service.listSource();

        call.enqueue(new HttpCallback<SourceList>() {
            @Override
            public void onSuccess(SourceList sourceList) {
                sourceList = sourceList;
                proDialog.dismiss();

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                findViewById(R.id.source).setEnabled(true);
                proDialog.dismiss();


            }
        });


    }

    MListAdapter adapter;

    class MListAdapter extends BaseAdapter {

        List<SourceList.SourceBean> list;

        MListAdapter(SourceList sourceList) {
            this.list = sourceList.getSourcesList();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public SourceList.SourceBean getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView text;
            if (view == null) {

                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_list_item, null);
            }
            text = (TextView) view;

            return view;
        }
    }
}
