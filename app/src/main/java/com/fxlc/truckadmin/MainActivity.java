package com.fxlc.truckadmin;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fxlc.truckadmin.activity.AddCarActivity;
import com.fxlc.truckadmin.activity.AllbilActivity;
import com.fxlc.truckadmin.activity.LoginActivity;
import com.fxlc.truckadmin.activity.SettingActivity;
import com.fxlc.truckadmin.activity.WaybillActivity;
import com.fxlc.truckadmin.api.BillService;
import com.fxlc.truckadmin.api.CarService;
import com.fxlc.truckadmin.bean.BillInfo;
import com.fxlc.truckadmin.bean.Cars;
import com.fxlc.truckadmin.bean.SourceList;
import com.fxlc.truckadmin.net.HttpCallback;
import com.fxlc.truckadmin.net.HttpResult;
import com.fxlc.truckadmin.net.MyThrowable;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    Intent it = new Intent();
    Context ctx;
    ListView listView, reportListView;

    SourceList.SourceBean source;
    TextView sourceTx;
    String carNo = "";
    EditText carNoTx;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = this;

        sourceTx = (TextView) findViewById(R.id.source);

        findViewById(R.id.allbill).setOnClickListener(this);
        findViewById(R.id.source).setOnClickListener(this);
        findViewById(R.id.query).setOnClickListener(this);
        findViewById(R.id.my).setOnClickListener(this);

        carNoTx = (EditText) findViewById(R.id.carno);
        listView = (ListView) findViewById(R.id.list);
        reportListView = (ListView) findViewById(R.id.exist_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (source != null) {

                    Cars.CarlistBean car = carAdapter.getItem(i);
                    billInfo(car.getCarId());
                } else {
                    toast("请先选择货源");
                }
            }
        });
        reportListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (source != null) {
                    Cars.CarlistBean car = existAdapter.getItem(i);
                    billInfo(car.getCarId());
                }


            }
        });
        dialog = new Dialog(ctx,R.style.dialog_alert);
        dialog.setContentView(R.layout.dialog_nocar);
        dialog.findViewById(R.id.cancel).setOnClickListener(this);
        dialog.findViewById(R.id.add).setOnClickListener(this);
        Window win = dialog.getWindow();

//        dialog.setMessage(
//                "未找到该车辆,是否添加？");
//        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "添加", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//                it.setClass(ctx, AddCarActivity.class);
//                startActivity(it);
//            }
//        });
        String sourceJson = sp.getString("source", "");
        if (!TextUtils.isEmpty(sourceJson)) {
            source = new Gson().fromJson(sourceJson, SourceList.SourceBean.class);
            sourceTx.setText(source.getSourceName());
            findViewById(R.id.allbill).setEnabled(true);
            findViewById(R.id.query).setEnabled(true);
            existCar();
        }

        carNoTx.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    carNo = carNoTx.getText().toString();
                    queryCar();
                    return true;
                }

                return false;
            }
        });


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Cars.CarlistBean car = (Cars.CarlistBean) intent.getSerializableExtra("car");

        if (car != null) {
            Cars cars = new Cars();
            List<Cars.CarlistBean> carList = new ArrayList<>();
            carList.add(car);
            cars.setCarlist(carList);

            carAdapter = new CarAdapter(cars);
            listView.setAdapter(carAdapter);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                 dialog.dismiss();
                it.setClass(ctx, AddCarActivity.class);
                startActivity(it);
                break;
            case R.id.cancel:
                dialog.dismiss();
                break;

            case R.id.source:

                view.setEnabled(false);
                listSource();

                break;
            case R.id.allbill:

                it.setClass(ctx, AllbilActivity.class);
                it.putExtra("sourceId", source.getSourceId());
                startActivity(it);
                break;
            case R.id.query:
                carNo = carNoTx.getText().toString();

                queryCar();
                break;
            case R.id.my:
                it.setClass(ctx, SettingActivity.class);
                startActivity(it);

                break;


        }
    }

    public void listSource() {
        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();

        BillService service = retrofit.create(BillService.class);

        Call<HttpResult<SourceList>> call = service.listSource();

        call.enqueue(new HttpCallback<SourceList>() {
            @Override
            public void onSuccess(SourceList sourceList) {
                findViewById(R.id.source).setEnabled(true);
                proDialog.dismiss();
                createDialog(sourceList);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                findViewById(R.id.source).setEnabled(true);
                proDialog.dismiss();


            }
        });


    }

    public void queryCar() {

        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();

        CarService service = retrofit.create(CarService.class);

        Call<HttpResult<Cars>> call = service.listCar(carNo);

        call.enqueue(new HttpCallback<Cars>() {
            @Override
            public void onSuccess(Cars carsData) {
                proDialog.dismiss();
                carAdapter = new CarAdapter(carsData);
                if (carsData.getCarlist().size() > 0) {
                    listView.setAdapter(carAdapter);
                } else {
                    dialog.show();

                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                proDialog.dismiss();
            }
        });


    }

    public void existCar() {

        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();

        CarService service = retrofit.create(CarService.class);

        Call<HttpResult<Cars>> call = service.existCars(source.getSourceId());

        call.enqueue(new HttpCallback<Cars>() {
            @Override
            public void onSuccess(Cars carsData) {
                proDialog.dismiss();
                existAdapter = new CarAdapter(carsData);

                reportListView.setAdapter(existAdapter);


            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                proDialog.dismiss();

                if (t instanceof MyThrowable){
                    MyThrowable mt = (MyThrowable) t;
                    if (mt.getErrorCode() .equals("01")){
                        it.setClass(ctx, LoginActivity.class);
                        startActivity(it);
                        MyApplication.getInstance().exit();

                    }
                }
            }
        });


    }

    public void billInfo(String carid) {

        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();

        BillService service = retrofit.create(BillService.class);

        Call<HttpResult<BillInfo>> call = service.billInfo(source.getSourceId(), carid);

        call.enqueue(new HttpCallback<BillInfo>() {
            @Override
            public void onSuccess(BillInfo billInfo) {
                proDialog.dismiss();
                Intent it = new Intent();
                it.putExtra("billInfo", billInfo);
                it.putExtra("sourceId", source.getSourceId());
                it.setClass(ctx, WaybillActivity.class);
                startActivity(it);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                super.onFailure(call, t);
                proDialog.dismiss();
            }
        });


    }


    CarAdapter carAdapter, existAdapter;


    class CarAdapter extends BaseAdapter {

        private Cars cars;

        public CarAdapter(Cars cars) {
            this.cars = cars;
        }

        @Override
        public int getCount() {
            return cars == null ? 0 : cars.getCarlist().size();
        }

        @Override
        public Cars.CarlistBean getItem(int i) {
            return cars.getCarlist().get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            Cars.CarlistBean sourceBean = cars.getCarlist().get(i);
            if (view == null)
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_list_item, null);

            TextView text = (TextView) view.findViewById(android.R.id.text1);


            text.setText(sourceBean.getCarNo());


            return view;
        }
    }

    Dialog sourceDialog;

    private void createDialog(SourceList sourceList) {

        sourceDialog = new Dialog(this, R.style.dialog_alert);

        ListView slistView = new ListView(ctx);
        sourceDialog.setContentView(slistView);
        sourceAdapter = new SourceAdapter(sourceList);
        slistView.setAdapter(sourceAdapter);
        slistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sourceDialog.dismiss();
                source = sourceAdapter.getItem(i);
                save("source", new Gson().toJson(source));

                sourceTx.setText(source.getSourceName());
                findViewById(R.id.allbill).setEnabled(true);
                findViewById(R.id.query).setEnabled(true);
                existCar();
            }
        });

        Window win = sourceDialog.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.BOTTOM;

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

        sourceDialog.show();

    }

    SourceAdapter sourceAdapter;

    class SourceAdapter extends BaseAdapter {
        SourceList sourceList;

        public SourceAdapter(SourceList list) {
            this.sourceList = list;
        }

        @Override
        public int getCount() {
            return sourceList == null ? 0 : sourceList.getSourcesList().size();
        }

        @Override
        public SourceList.SourceBean getItem(int i) {
            return sourceList.getSourcesList().get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            SourceList.SourceBean sourceBean = sourceList.getSourcesList().get(i);
            if (view == null)
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.simple_list_item, null);

            TextView text = (TextView) view.findViewById(android.R.id.text1);


            text.setText(sourceBean.getSourceName());


            return view;
        }
    }

    private long firstTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {


                if ( secondTime - firstTime < 2000) {
                    System.exit(0);
                } else {
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = System.currentTimeMillis();
                }
                return true;
            }



        return super.onKeyDown(keyCode, event);
    }

}
