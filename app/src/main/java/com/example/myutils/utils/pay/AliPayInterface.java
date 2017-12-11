package com.example.myutils.utils.pay;

/**
 * 项目名称： LegalHigh
 * 创 建 人： 艺仔加油
 * 创建时间： 2017/8/11 10:47
 * 本类描述：
 */

public interface AliPayInterface {
    void onSuccess(String code, String orderID);//支付成功  code = 9000
    void onFailure(String code);//支付失败  code = 8000
    void onPay();//支付结果确认中
}
