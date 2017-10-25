package com.fxlc.truckadmin.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fxlc.truckadmin.BaseActivity;
import com.fxlc.truckadmin.MyApplication;
import com.fxlc.truckadmin.R;
import com.fxlc.truckadmin.api.AccountService;
import com.fxlc.truckadmin.api.BillService;
import com.fxlc.truckadmin.bean.BankCards;
import com.fxlc.truckadmin.bean.FeeInfo;
import com.fxlc.truckadmin.net.HttpCallback;
import com.fxlc.truckadmin.net.HttpResult;
import com.fxlc.truckadmin.net.SimpleCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

public class FeeInfoActivity extends BaseActivity implements View.OnClickListener {


    String orderId;
    int statu;
    TextView freightTx, freightSumTx, goodsTypeTx, priceTx, allowLossTx, lossTx, lossSumTx, msgFeeTx, insuranceTx, endSumTx;
    TextView loadWeightTx, unloadWeightTx, carnoTx, billnoTx, bossMobileTx, followMobileTx;
    TextView intsumTx;
    TextView action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fee_info);
        orderId = getIntent().getStringExtra("orderId");


        carnoTx = (TextView) findViewById(R.id.carno);
        billnoTx = (TextView) findViewById(R.id.billno);
        followMobileTx = (TextView) findViewById(R.id.follow_mobile);
        bossMobileTx = (TextView) findViewById(R.id.boss_mobile);

        findViewById(R.id.call1).setOnClickListener(this);
        findViewById(R.id.call2).setOnClickListener(this);
        freightTx = (TextView) findViewById(R.id.freight);
        freightSumTx = (TextView) findViewById(R.id.totalWorth);
        goodsTypeTx = (TextView) findViewById(R.id.goods_type);
        priceTx = (TextView) findViewById(R.id.goods_price);

        loadWeightTx = (TextView) findViewById(R.id.load_weight);
        unloadWeightTx = (TextView) findViewById(R.id.unload_weight);

        allowLossTx = (TextView) findViewById(R.id.allow_loss);
        lossTx = (TextView) findViewById(R.id.loss);
        lossSumTx = (TextView) findViewById(R.id.loss_sum);
        msgFeeTx = (TextView) findViewById(R.id.msg_fee);
        insuranceTx = (TextView) findViewById(R.id.insurance);
        endSumTx = (TextView) findViewById(R.id.endsum);
        intsumTx = (TextView) findViewById(R.id.intsum);

        action = (TextView) findViewById(R.id.action);

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        title("费用详情");
    }

    private void setValue(FeeInfo feeInfo) {


        carnoTx.setText(feeInfo.getCarNo() + " " + feeInfo.getHandcarNo());
        billnoTx.setText(feeInfo.getOrderNo());
        bossMobileTx.setText(feeInfo.getBossMobile());
        followMobileTx.setText(feeInfo.getFollowMobile());

        freightTx.setText(feeInfo.getFreight() + " 元/吨");
        freightSumTx.setText("￥" + feeInfo.getFreightSum());

        loadWeightTx.setText(feeInfo.getLoadWeight() + "吨");
        unloadWeightTx.setText(feeInfo.getUnloadWeight() + "吨");

        goodsTypeTx.setText(feeInfo.getGoodsType());
        priceTx.setText(feeInfo.getPerPrice() + "元/吨");

        allowLossTx.setText(("允许损耗*吨 超出按货物单价赔付").replace("*", feeInfo.getWear()));

        lossTx.setText(feeInfo.getRealLoss() + "吨");

        lossSumTx.setText("-￥" + feeInfo.getLossSum());

        msgFeeTx.setText(feeInfo.getMsgFee() + "元");
        insuranceTx.setText(feeInfo.getInsurance() + "元");
        endSumTx.setText("￥" + feeInfo.getEndSum());
        intsumTx.setText("￥" + feeInfo.getIntSum());
        if (feeInfo.getFeesType() == 0) {
            findViewById(R.id.intpart).setVisibility(View.GONE);
        }
        if (feeInfo.getOrderStatus().equals("3")) {
            action.setOnClickListener(this);
            action.setEnabled(true);
        }else {
            action.setText("已结算");
            action.setEnabled(false);

        }

    }

    FeeInfo mfeeIno;

    private void loadData() {
        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();

        BillService service = retrofit.create(BillService.class);
        Call<HttpResult<FeeInfo>> call = service.feeInfo(orderId);

        call.enqueue(new HttpCallback<FeeInfo>() {
            @Override
            public void onSuccess(FeeInfo feeInfo) {
                proDialog.dismiss();
                mfeeIno = feeInfo;
                setValue(feeInfo);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.call1:
                if (mfeeIno != null && !TextUtils.isEmpty(mfeeIno.getBossMobile())) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mfeeIno.getBossMobile()));
                    startActivity(intent);
                }


                break;
            case R.id.call2:
                if (mfeeIno != null && !TextUtils.isEmpty(mfeeIno.getFollowMobile())) {
                    Intent intents = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mfeeIno.getFollowMobile()));
                    startActivity(intents);
                }

                break;
            case R.id.action:
                getMyCards();
                break;
            case R.id.add:
                d.dismiss();
                it.setClass(ctx, AddBankcardActivity.class);
                it.putExtra("ownerId",mfeeIno.getOwnerId());
                startActivity(it);

        }
    }

    private void goCount(BankCards.BankCard card) {
        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();
        BillService service = retrofit.create(BillService.class);

        Call<HttpResult> call = service.goCount(mfeeIno.getOrderId(), card.getId());

        call.enqueue(new SimpleCallback() {
            @Override
            public void onSuccess(HttpResult result) {
                proDialog.dismiss();
                toast("结算成功");
                action.setText("已结算");
                action.setEnabled(false);
            }
        });


    }

    private List<BankCards.BankCard> bankList;


    public void getMyCards() {

        proDialog.show();
        Retrofit retrofit = MyApplication.getRetrofit();
        AccountService service = retrofit.create(AccountService.class);
        final Call<HttpResult<BankCards>> call = service.listBankCard(mfeeIno.getOwnerId());
        call.enqueue(new HttpCallback<BankCards>() {
            @Override
            public void onSuccess(BankCards bankCards) {
                proDialog.dismiss();
                bankList = bankCards.getBankcard();

                createDialog();

            }
        });
    }

    Dialog d;

    private void createDialog() {

        d = new Dialog(ctx, R.style.dialog_alert);


        d.setContentView(R.layout.dialog_option);
        d.findViewById(R.id.cancel).setOnClickListener(this);
        d.findViewById(R.id.add).setOnClickListener(this);
        ListView listview = d.findViewById(R.id.list);
        listview.setAdapter(new MListAdapter());
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                d.dismiss();
                BankCards.BankCard card = (BankCards.BankCard) adapterView.getItemAtPosition(i);
                goCount(card);

            }
        });
        Window win = d.getWindow();
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.gravity = Gravity.BOTTOM;

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        d.show();

    }

    class MListAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return bankList == null ? 0 : bankList.size();
        }

        @Override
        public BankCards.BankCard getItem(int i) {
            return bankList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView text;
            if (view == null) {

                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_dialog_cardsel, null);
            }
            text = (TextView) view;
            String s = getItem(i).getBankNo().substring(getItem(i).getBankNo().length() - 4);
            String name = getItem(i).getName();
            text.setText( name + " " + getItem(i).getBankType() + "(" + s + ") " );
            return view;
        }
    }
}
