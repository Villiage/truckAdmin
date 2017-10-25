package com.fxlc.truckadmin.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fxlc.truckadmin.BaseActivity;
import com.fxlc.truckadmin.R;
import com.fxlc.truckadmin.bean.Truck;
import com.fxlc.truckadmin.db.MySqliteHelper;

import java.util.ArrayList;
import java.util.List;

public class BrandActivity extends BaseActivity implements View.OnClickListener {

    ListView listView;
    ListView stylesView;
    BrandAdapter brandAdapter;
    List<String> styles;
    SecAdapter secAdapter;

    Truck truck;
    private List<String> brands = new ArrayList<>();

    private MySqliteHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        truck = (Truck) getIntent().getSerializableExtra("truck");
        helper = new MySqliteHelper(this);
        setContentView(R.layout.activity_brand);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setScrimColor(Color.TRANSPARENT);

        listView = (ListView) findViewById(R.id.list);
        initData();
        brandAdapter = new BrandAdapter(brands);
        listView.setAdapter(brandAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("cyd", "click" + i);
                String brand = brands.get(i);
                truck.setBrand(brand);
                if (!drawer.isDrawerOpen(Gravity.RIGHT)) {
                    drawer.openDrawer(Gravity.RIGHT);
                }
                styles = getStylesByBrand(brand);
                secAdapter.setDataList(styles);
            }
        });
        stylesView = (ListView) findViewById(R.id.sec_list);
        secAdapter = new SecAdapter();
        stylesView.setAdapter(secAdapter);
        stylesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent();
                truck.setStyle(styles.get(i));
                it.putExtra("truck", truck);
                it.setClass(ctx, TruckActivity.class);
                startActivityForResult(it, 106);

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        title("品牌");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 106) {
            truck = (Truck) data.getSerializableExtra("truck");
            it.putExtra("truck", truck);
            setResult(RESULT_OK, it);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }




    private void initData() {
        db = helper.getWritableDatabase();
        String sql = "select DISTINCT brand from truck";

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {

            brands.add(cursor.getString(0));
        }
        cursor.close();

    }

    private List<String> getStylesByBrand(String brand) {
        List<String> styles = new ArrayList<>();
        db = helper.getWritableDatabase();
        String sql = "select DISTINCT style from truck where brand = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{brand});
        while (cursor.moveToNext()) {

            styles.add(cursor.getString(0));
        }
        cursor.close();

        return styles;
    }

    class BrandAdapter extends BaseAdapter {
        private List<String> dataList;

        public BrandAdapter(List<String> dataList) {
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public String getItem(int i) {
            return dataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            TextView txt1, txt2, charTxt;
            if (view == null) {
//                int id = android.R.layout.simple_list_item_2;
                int id = R.layout.item_brand;
                view = LayoutInflater.from(viewGroup.getContext()).inflate(id, viewGroup, false);
            }
            String brand = dataList.get(i);
            charTxt = (TextView) view.findViewById(R.id.firstchar);
            txt1 = (TextView) view.findViewById(R.id.text1);
            txt1.setText(brand);
            charTxt.setText("A");
//            if (i > 0 && brand == dataList.get(i - 1)) {
            charTxt.setVisibility(View.GONE);
//            } else charTxt.setVisibility(View.VISIBLE);

            return view;
        }
    }

    class SecAdapter extends BaseAdapter {
        private List<String> dataList;

        public void setDataList(List<String> dataList) {
            this.dataList = dataList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return dataList == null ? 0 : dataList.size();
        }

        @Override
        public String getItem(int i) {
            return dataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            TextView txt1, txt2, charTxt;
            if (view == null) {
//                int id = android.R.layout.simple_list_item_2;
                int id = R.layout.item_brand_sec;
                view = LayoutInflater.from(viewGroup.getContext()).inflate(id, viewGroup, false);
            }
            txt1 = (TextView) view.findViewById(R.id.text1);
            txt1.setText(dataList.get(i));

            return view;
        }
    }

}
