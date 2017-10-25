package com.fxlc.truckadmin.activity;

import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fxlc.truckadmin.BaseActivity;
import com.fxlc.truckadmin.R;
import com.fxlc.truckadmin.bean.Truck;
import com.fxlc.truckadmin.db.MySqliteHelper;
import com.fxlc.truckadmin.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

public class TruckActivity extends BaseActivity implements View.OnClickListener {
    LinearLayout driveGroup;
    ListView soupList;
    SoupAdapter adapter;
    List<String> drives;
    List<String> soups;
    Truck truck;
    private MySqliteHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck);
        driveGroup = (LinearLayout) findViewById(R.id.drive_group);
        soupList = (ListView) findViewById(R.id.soup_list);

        adapter = new SoupAdapter();
        soupList.setAdapter(adapter);
        soupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                 truck.setSoup(soups.get(i));
                 it.putExtra("truck",truck);
                 setResult(RESULT_OK,it);
                 finish();

            }
        });

        truck = (Truck) getIntent().getSerializableExtra("truck");
        helper = new MySqliteHelper(this);

        drives = getDrives(truck.getBrand(), truck.getStyle());
        initDriveView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


        }
    }

    //    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initDriveView() {
        int margin = DisplayUtil.dp2px(ctx, 10);
        int padv = DisplayUtil.dp2px(ctx, 5);
        int padh = DisplayUtil.dp2px(ctx, 20);
        driveGroup.removeAllViews();
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(-2, -2);
        param.leftMargin = margin;
//        ColorStateList colorList = getColorStateList(R.color.tab_txt_sel);
        int[] colors = {R.color.white, R.color.text_mark};
        int[][] states = new int[2][];
        states[0] =  new int[]{android.R.attr.state_selected}  ;
        states[1] =  new int[]{}  ;
         ColorStateList colorList = new ColorStateList(states,colors);
        for (String drive : drives) {
            TextView textView = new TextView(this);
            textView.setText(drive);
//            textView.setTextColor(getResources().getColor(R.color.tab_txt_sel));
            textView.setTextColor(colorList);
            textView.setPadding(padh, padv, padh, padv);
            textView.setBackgroundResource(R.drawable.tab_sel);
            textView.setOnClickListener(ocl);
            driveGroup.addView(textView, param);
        }
        driveGroup.getChildAt(0).performClick();

    }

    View.OnClickListener ocl = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            for (int i = 0; i < driveGroup.getChildCount(); i++) {
                View v = driveGroup.getChildAt(i);
                if (v != view) {
                    v.setSelected(false);

                }
            }
            view.setSelected(true);
            TextView txt = (TextView) view;
            truck.setDrive(txt.getText().toString());
            soups = getSoups(truck.getBrand(), truck.getStyle(), truck.getDrive());

            adapter.notifyDataSetChanged();
        }
    };

    private List<String> getDrives(String brand, String style) {
        db = helper.getWritableDatabase();
        List<String> drives = new ArrayList<>();
        db = helper.getWritableDatabase();
        String sql = "select DISTINCT drive from truck where brand = ? and style = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{brand, style});
        while (cursor.moveToNext()) {

            drives.add(cursor.getString(0));
        }
        cursor.close();

        return drives;

    }

    private List<String> getSoups(String brand, String style, String drive) {
        db = helper.getWritableDatabase();
        List<String> soups = new ArrayList<>();
        db = helper.getWritableDatabase();
        String sql = "select DISTINCT soup from truck where brand = ? and style = ? and drive = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{brand, style, drive});
        while (cursor.moveToNext()) {

            soups.add(cursor.getString(0));
        }
        cursor.close();

        return soups;

    }


    class SoupAdapter extends BaseAdapter {
        public SoupAdapter() {
        }

        @Override
        public int getCount() {
            return soups == null ? 0 : soups.size();
        }

        @Override
        public Object getItem(int i) {
            return soups.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_soup, null);

            }
            TextView txt = (TextView) view.findViewById(R.id.text1);
            txt.setText(soups.get(i));
            return view;
        }
    }
}
