package com.example.myutils.utils.pay;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;


import com.alipay.sdk.app.PayTask;
import com.example.myutils.R;
import com.example.myutils.utils.Tools;
import com.example.myutils.utils.systemUtils.L;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名称： ElectroCar
 * 创 建 人： 艺仔加油
 * 创建时间： 2017/8/11 9:50
 * 本类描述： 支付宝不需要key，微信需要key
 * 1.微信支付  ------> 当微信支付完了之后，再wxapi包下的WXPayEntryActivity下做操作
 * 2.支付宝支付 ----->在回调下写操作
 */

public class PayUtils {
    /**
     * 1.微信支付
     * context 上下文对象
     * data  传入的类型：{"appid":"wx80129625d3b18578","noncestr":"4CdQFtesJMTz7rVV","outtradeno":"1201708110924061479","package":"Sign=WXPay","partnerid":"1484252692","prepayid":"wx201708110923309bdbb427d50026276506","sign":"46FC0A5B28AF8F21DF5802293C16E201","timestamp":"1502443446"}
     */
    public static void wetChatPay(Context context, Object data) {
        if (Tools.isWeixinAvilible(context)) {//判断是否有安装微信
            IWXAPI api = WXAPIFactory.createWXAPI(context, context.getString(R.string.wetChat_APPKEY));
            try {
                JSONObject weChatInfo = new JSONObject(String.valueOf(data));
                PayReq req = new PayReq();
                req.appId = weChatInfo.getString("appid");
                req.nonceStr = weChatInfo.getString("noncestr");
                req.packageValue = weChatInfo.getString("package");
                req.partnerId = weChatInfo.getString("partnerid");
                req.prepayId = weChatInfo.getString("prepayid");
                req.sign = weChatInfo.getString("sign");
                req.timeStamp = weChatInfo.getString("timestamp");
//                            Getouttradeno.outtradeno = weChatInfo.getString("outtradeno");
                api.registerApp(context.getString(R.string.wetChat_APPKEY));
                api.sendReq(req);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 2.支付宝支付
     * payId  传入形式：app_id=2017051107199228&biz_content=%7B%22out_trade_no%22%3A%221201708111022468582%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22subject%22%3A%22%E6%B5%81%E9%87%8F%E8%AE%A2%E5%8D%95%22%2C%22total_amount%22%3A0.02%7D&charset=utf-8&method=alipay.trade.app.pay¬ify_url=http%3A%2F%2F203.88.193.234%3A8807%2FNotify%2Fnotify_url.aspx&sign_type=RSA2×tamp=2017-08-11%2010%3A22%3A46&version=1.0&sign=ZjVN61PKQ57z%2F9XC01%2FuGWr1yIL8dITyXHiW84RMoXiH%2FNeUrdM00XiRJx5BdW%2B8WdLpkGyvjiVrMwWtSayDHHtt97oGaqI9cW2SjHyXZNS3Pyjx9SsY4V39o9A60qw0QLATqqGfEzF2MYOvAdim5nauSC2hsIBRYOwLkpmCp1v5To0f%2BGn0FAtivc0mop6K24PkbNde0lZUlOFPXftNImrZi3YKzZm0c7VAfbAGlBqZkECoQXEpgQgrBr9XqFegQ9LWB%2FtlX0HYn5ZzNiUmNU5XL1DcEc6JnCoE%2FY2L5QhTyqS5%2FCCkZsQRh503eZT%2FHtaYTQQXh0wHxZBnIgMZxQ%3D%3D
     * 就一个String类型
     */
    public static void aliPay(final Activity activity, final String payId, final AliPayInterface aliPayInterface) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(activity);
                String result = alipay.pay(payId, true);
                final String payInfo = result;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PayResult result = new PayResult(payInfo);
                        String resultStatus = result.getResultStatus();
                        if (TextUtils.equals(resultStatus, "9000")) {
                            Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show();
                            aliPayInterface.onSuccess("9000", "");
                        } else {
                            if (TextUtils.equals(resultStatus, "8000")) {
                                Toast.makeText(activity, "支付结果确认中", Toast.LENGTH_SHORT).show();
                                aliPayInterface.onFailure("8000");
                            } else {
                                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                Toast.makeText(activity, "支付失败", Toast.LENGTH_SHORT).show();
                                aliPayInterface.onPay();
                            }
                        }
                    }
                });
            }
        }).start();
    }

    //整一条json传过来
    //{"Result":true,"Msg":"wxf33c86d7173532b6\u00261428077102\u0026sOoWVIupZDl1nKqeJtl6I2WfRiSZhiUu\u0026成功","Data":{"PushMsg":"{\"appid\":\"wxf33c86d7173532b6\",\"noncestr\":\"cgEDnNcRBnYTTzQM\",\"outtradeno\":\"53201709011143203931\",\"package\":\"Sign=WXPay\",\"partnerid\":\"1428077102\",\"prepayid\":\"wx20170902132918463752d95f0679451819\",\"sign\":\"00A137445FE7AC57EBFB47CC939A788C\",\"timestamp\":\"1504358720\"}","OrderId":"1533"}}
    public static void wetChatPayJson(Context context, Object json) {
        if (Tools.isWeixinAvilible(context)) {//判断是否有安装微信
            IWXAPI api = WXAPIFactory.createWXAPI(context, context.getString(R.string.wetChat_APPKEY));
            String weChatJson = (String) json;
            try {
                JSONObject jsonObject = new JSONObject(weChatJson);
                if (jsonObject.getBoolean("Result")) {
                    JSONObject weChatOrderInfo = jsonObject.getJSONObject("Data");
                    String orderId = weChatOrderInfo.getString("OrderId");
                    String pushMsg = weChatOrderInfo.getString("PushMsg");
                    JSONObject weChatInfo = new JSONObject(pushMsg);
                    PayReq req = new PayReq();
                    req.appId = weChatInfo.getString("appid");
                    req.nonceStr = weChatInfo.getString("noncestr");
                    req.packageValue = weChatInfo.getString("package");
                    req.partnerId = weChatInfo.getString("partnerid");
                    req.prepayId = weChatInfo.getString("prepayid");
                    req.sign = weChatInfo.getString("sign");
                    req.timeStamp = weChatInfo.getString("timestamp");
//                  Getouttradeno.outtradeno = weChatInfo.getString("outtradeno");
                    api.registerApp(context.getString(R.string.wetChat_APPKEY));
                    api.sendReq(req);
                } else {
                    Toast.makeText(context, "请求失败", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
        }
    }

    //传入一条Json
    //{"Result":true,"Msg":"wxf33c86d7173532b6\u00261428077102\u0026sOoWVIupZDl1nKqeJtl6I2WfRiSZhiUu\u0026成功","Data":{"PushMsg":"{\"appid\":\"wxf33c86d7173532b6\",\"noncestr\":\"cgEDnNcRBnYTTzQM\",\"outtradeno\":\"53201709011143203931\",\"package\":\"Sign=WXPay\",\"partnerid\":\"1428077102\",\"prepayid\":\"wx20170902132918463752d95f0679451819\",\"sign\":\"00A137445FE7AC57EBFB47CC939A788C\",\"timestamp\":\"1504358720\"}","OrderId":"1533"}}
    public static void aliPayJson(final Activity activity, final Object json, final AliPayInterface aliPayInterface) {
        try {
            JSONObject jsonObject = new JSONObject(String.valueOf(json));
            if (jsonObject.getBoolean("Result")) {
                final JSONObject orderInfo = jsonObject.getJSONObject("Data");
                final String pushMsg = orderInfo.getString("PushMsg");
                final String orderId = orderInfo.getString("OrderId");
                L.d("aliPayJson", "orderId=" + orderId + ":::pushMsg" + pushMsg);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(activity);
                        String result = alipay.pay(pushMsg, true);
                        final String payInfo = result;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                PayResult result = new PayResult(payInfo);
                                String resultStatus = result.getResultStatus();
                                if (TextUtils.equals(resultStatus, "9000")) {
                                        Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show();
                                    aliPayInterface.onSuccess("9000", orderId);
                                } else {
                                    if (TextUtils.equals(resultStatus, "8000")) {
                                        Toast.makeText(activity, "支付结果确认中", Toast.LENGTH_SHORT).show();
                                        aliPayInterface.onFailure("8000");
                                    } else {
                                        // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                        Toast.makeText(activity, "支付失败", Toast.LENGTH_SHORT).show();
                                        aliPayInterface.onPay();
                                    }
                                }
                            }
                        });
                    }
                }).start();
            } else {
                Toast.makeText(activity, "请求失败", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
