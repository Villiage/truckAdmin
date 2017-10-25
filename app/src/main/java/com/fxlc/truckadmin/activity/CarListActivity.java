package com.fxlc.truckadmin.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fxlc.truckadmin.ListActiviity;
import com.fxlc.truckadmin.R;
import com.fxlc.truckadmin.bean.Cars;


public class CarListActivity extends ListActiviity implements View.OnClickListener {



    TruckAdapter adapter;
    Cars myCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_carlist);
        super.onCreate(savedInstanceState);
//        myCar = (Cars) getIntent().getSerializableExtra("mycar");
//        listView.setAdapter(adapter = new TruckAdapter());
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                it.setClass(ctx, TruckInfoActivity.class);
//                it.putExtra("truck", trucks.get(i));
//                startActivity(it);
//            }
//        });
        findViewById(R.id.add).setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        title("我的车辆");
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:

                it.setClass(ctx, AddCarActivity.class);
//                it.putExtra("truckerId",myCar.getTruckerId());
                startActivity(it);

                break;


        }
    }


    class TruckAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return myCar == null ? 0 : myCar.getCarlist().size();
        }

        @Override
        public Cars.CarlistBean getItem(int i) {
            return myCar.getCarlist().get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            Cars.CarlistBean car = myCar.getCarlist().get(i);
            if (view == null)
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_mycar, null);

            TextView carNo = (TextView) view.findViewById(R.id.car_no);


            carNo.setText(car.getCarNo());


            return view;
        }
    }


}
