package com.fxlc.truckadmin.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fxlc.truckadmin.ListActiviity;
import com.fxlc.truckadmin.MyApplication;
import com.fxlc.truckadmin.R;
import com.fxlc.truckadmin.api.BillService;
import com.fxlc.truckadmin.api.CarService;
import com.fxlc.truckadmin.bean.AllBill;
import com.fxlc.truckadmin.bean.BillInfo;
import com.fxlc.truckadmin.net.HttpCallback;
import com.fxlc.truckadmin.net.HttpResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

public class AllbilActivity extends ListActiviity implements View.OnClickListener {
    List<AllBill.BillBean> finishList;
    List<AllBill.BillBean> unfinishList;
    MListAdapter finishAdapter;
    UnListAdapter unfinishAdapter;

    String sourceId;
    int tab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_allbil);
        super.onCreate(savedInstanceState);

        sourceId = getIntent().getStringExtra("sourceId");
        findViewById(R.id.finish).setOnClickListener(this);
        findViewById(R.id.unfinish).setOnClickListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                AllBill.BillBean bean = (AllBill.BillBean) listView.getItemAtPosition(i);

                billInfo(bean.getOrderId());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        title("运单管理");
        loadData();
    }

    public void billInfo(String orderId) {

        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();

        BillService service = retrofit.create(BillService.class);

        Call<HttpResult<BillInfo>> call = service.billDetail(orderId);

        call.enqueue(new HttpCallback<BillInfo>() {
            @Override
            public void onSuccess(BillInfo billInfo) {
                proDialog.dismiss();

                it.putExtra("billInfo", billInfo);
                it.putExtra("sourceId", sourceId);

                it.setClass(ctx, WaybillActivity.class);
                it.putExtra("statu", billInfo.getOrderStatus());
                startActivity(it);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                proDialog.dismiss();
            }
        });


    }

    @Override
    public void loadData() {
        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();

        CarService service = retrofit.create(CarService.class);

        Call<HttpResult<AllBill>> call = service.billManage(sourceId);

        call.enqueue(new HttpCallback<AllBill>() {
            @Override
            public void onSuccess(AllBill allBill) {
                proDialog.dismiss();
                finishList = allBill.getFinishList();
                unfinishList = allBill.getUnfinishList();

                finishAdapter = new MListAdapter(finishList);
                unfinishAdapter = new UnListAdapter(unfinishList);
                findViewById(R.id.finish).setEnabled(true);
                findViewById(R.id.unfinish).setEnabled(true);

                if (tab == 0) {
                    findViewById(R.id.unfinish).performClick();

                } else {
                    findViewById(R.id.finish).performClick();

                }

            }


        });
    }

    @Override
    public void onClick(View view) {
        view.setEnabled(false);
        switch (view.getId()) {
            case R.id.finish:
                tab = 1;
                if (finishList.size() > 0) {
                    showDataView();
                    listView.setAdapter(finishAdapter);
                } else showEmptyView();
                findViewById(R.id.unfinish).setEnabled(true);

                break;
            case R.id.unfinish:
                tab = 0;
                if (unfinishList.size() > 0) {
                    showDataView();
                    listView.setAdapter(unfinishAdapter);
                } else showEmptyView();

                findViewById(R.id.finish).setEnabled(true);

                break;

        }
    }

    class UnListAdapter extends BaseAdapter {

        public List<AllBill.BillBean> dataList;

        public UnListAdapter(List<AllBill.BillBean> dataList) {
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public AllBill.BillBean getItem(int i) {
            return dataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bill_un, null);
                holder.carNoTx = view.findViewById(R.id.carno);
                holder.loadTimeTx = view.findViewById(R.id.load_time);

                holder.loadWeightTx = view.findViewById(R.id.load_weight);

                view.setTag(holder);
            } else holder = (ViewHolder) view.getTag();
            AllBill.BillBean bill = getItem(i);
            holder.carNoTx.setText("车牌号：" + bill.getCarNo());

            holder.loadTimeTx.setText("装车时间：" + bill.getLoadDate());
            holder.loadWeightTx.setText("装车净重：" + bill.getLoadWeight());

            return view;
        }
    }

    class MListAdapter extends BaseAdapter {

        public List<AllBill.BillBean> dataList;

        public MListAdapter(List<AllBill.BillBean> dataList) {
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public AllBill.BillBean getItem(int i) {
            return dataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_bill, null);
                holder.carNoTx = view.findViewById(R.id.carno);
                holder.loadTimeTx = view.findViewById(R.id.load_time);
                holder.unloadTimeTx = view.findViewById(R.id.unload_time);
                holder.loadWeightTx = view.findViewById(R.id.load_weight);
                holder.unloadWeightTx = view.findViewById(R.id.unload_weight);
                holder.freightTx = view.findViewById(R.id.freight);
                holder.markTx = view.findViewById(R.id.mark);
                view.setTag(holder);
            } else holder = (ViewHolder) view.getTag();
            AllBill.BillBean bill = getItem(i);
            holder.carNoTx.setText("车牌号：" + bill.getCarNo());


            holder.unloadTimeTx.setText("卸车时间：" + bill.getUnloadDate());
            holder.unloadWeightTx.setText("卸车净重：" + bill.getUnloadWeight());
            holder.freightTx.setText( bill.getEndSum());

            holder.loadTimeTx.setText("装车时间：" + bill.getLoadDate());

            holder.loadWeightTx.setText("装车净重：" + bill.getLoadWeight());

            if (bill.getOrderStatus().equals("5")) {
                holder.markTx.setText("已结算");
                holder.markTx.setTextColor(getResources().getColor(R.color.text_blue));
            } else if (bill.getOrderStatus().equals("3")) {
                holder.markTx.setText("待结算");
                holder.markTx.setTextColor(getResources().getColor(R.color.assist));
            }

            return view;
        }
    }

    class ViewHolder {

        TextView carNoTx, loadTimeTx, unloadTimeTx, loadWeightTx, unloadWeightTx, freightTx, markTx;


    }
}
