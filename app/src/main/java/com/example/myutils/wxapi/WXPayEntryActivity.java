package com.example.myutils.wxapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myutils.R;
import com.example.myutils.base.BaseActivity;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信支付会跳入这个界面
 */
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public int getContentLayoutId() {
        return R.layout.fragment_main;//支付后跳转的界面
    }

    @Override
    public void init(Bundle savedInstanceState) {
        api = WXAPIFactory.createWXAPI(this, getString(R.string.wetChat_APPKEY));
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    public void doWork(Context context) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        //resp.errCode   0 支付成功 | -1 支付失败
        Log.d("weiXinRefund", "onPayFinish, errCode = " + resp.errCode);
        if (resp.errCode == 0) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}