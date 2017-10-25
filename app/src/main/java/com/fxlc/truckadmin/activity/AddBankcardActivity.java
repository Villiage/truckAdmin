package com.fxlc.truckadmin.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fxlc.truckadmin.BaseActivity;
import com.fxlc.truckadmin.MyApplication;
import com.fxlc.truckadmin.R;
import com.fxlc.truckadmin.api.AccountService;
import com.fxlc.truckadmin.bean.BankList;
import com.fxlc.truckadmin.net.HttpCallback;
import com.fxlc.truckadmin.net.HttpResult;
import com.fxlc.truckadmin.net.SimpleCallback;
import com.fxlc.truckadmin.util.DisplayUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

public class AddBankcardActivity extends BaseActivity implements View.OnClickListener {

    TextView nameTx, cardNoTx, opencardTx, bankTx;
    String ownerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ownerId = getIntent().getStringExtra("ownerId");
        setContentView(R.layout.activity_add_bankcard);
        findViewById(R.id.banks).setOnClickListener(this);
        findViewById(R.id.action).setOnClickListener(this);

        nameTx = (TextView) findViewById(R.id.name);
        cardNoTx = (TextView) findViewById(R.id.cardno);
        opencardTx = (TextView) findViewById(R.id.openbank);
        bankTx = (TextView) findViewById(R.id.bankname);
//        nameTx.setText(user.getName());
//        nameTx.setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        title("添加银行卡");
    }

    List<BankList.BankBean> banks;
    BankList.BankBean bank;
    Dialog bankDialog;

    private void loadBank() {

        Retrofit retrofit = MyApplication.getRetrofit();

        AccountService service = retrofit.create(AccountService.class);
        Call<HttpResult<BankList>> call = service.listBank();
        call.enqueue(new HttpCallback<BankList>() {
            @Override
            public void onSuccess(BankList bankList) {
                banks = bankList.getBankList();

                bankDialog = createListDialog(ctx, banks, bankOcl);
                bankDialog.show();
            }
        });
    }
    private void saveBank() {

        Retrofit retrofit = MyApplication.getRetrofit();
        Map<String ,String> map = new HashMap<>();
        map.put("name",nameTx.getText().toString());
        map.put("bankId",bank.getId());
        map.put("bankNo",cardNoTx.getText().toString());
        map.put("openCard",opencardTx.getText().toString());
        map.put("ownerId",ownerId);

        AccountService service = retrofit.create(AccountService.class);
        Call<HttpResult> call = service.saveBank(map);
        call.enqueue(new SimpleCallback() {
            @Override
            public void onSuccess(HttpResult result) {

                toast("添加成功");
                finish();
            }
        });
    }

    private View.OnClickListener bankOcl = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            bankDialog.dismiss();
            int p = (int) view.getTag();
            bank = banks.get(p);
            bankTx.setText(bank.getName());
            findViewById(R.id.action).setEnabled(true);

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.banks:

                if (banks == null && bankDialog == null)
                    loadBank();
                else bankDialog.show();

                break;
            case R.id.action:

                 saveBank();

                break;

        }
    }

    public static Dialog createListDialog(Context context, List<BankList.BankBean> items, View.OnClickListener ocl) {

        final Dialog d = new Dialog(context, R.style.dialog_alert);
        int pad = DisplayUtil.dp2px(context, 10);
        LinearLayout linear = new LinearLayout(context);
        linear.setOrientation(LinearLayout.VERTICAL);
        linear.setBackgroundColor(context.getResources().getColor(R.color.divider));
        TextView cancelView = new TextView(context);
        cancelView.setPadding(pad, pad, pad, pad);
        cancelView.setText("取消");
        cancelView.setId(R.id.dialog_close);
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });


        for (int i = 0; i < items.size(); i++) {

            TextView textView = new TextView(context);
            textView.setTag(i);
            textView.setOnClickListener(ocl);
            textView.setGravity(Gravity.CENTER);
            textView.setText(items.get(i).getName());
            textView.setPadding(0, DisplayUtil.dp2px(context, 10), 0, DisplayUtil.dp2px(context, 10));
            textView.setBackgroundColor(Color.WHITE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
            params.topMargin = 1;
            linear.addView(textView, params);
            textView.setOnClickListener(ocl);
        }


        d.setContentView(linear);

        Window win = d.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.BOTTOM;

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);

        return d;
    }
}
