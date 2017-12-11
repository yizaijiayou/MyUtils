package com.example.myutils.utils.systemUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.myutils.base.BaseApplication;

/**
 * Project name ElectroCar
 * Created by 哎呀呀！要努力打代码啦！··
 * on 2017/5/2 13:34.
 * 本类描述： 网络工具类
 */
public class NetUtils {
    public NetUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断网络是否连接
     */
    public static boolean isConnected() {
        Context context = BaseApplication.getAppContext();
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo networkInfo = connectivity.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    ToastUtils.show("已连接网络");
                    return true;
                }
            }
        }
        ToastUtils.show("请确保网络通畅");
        return false;
    }

    /**
     * 判断连接的是否是wifi
     *
     * @param context
     * @return
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null)
            return false;
        return connectivity.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

//    /**
//     * 打开网络界面
//     * @param activity
//     * @return
//     */
//    private final static int OPEN_NETWORK_SETTINGS = 10000;
//    public static void openSetting(Activity activity){
//        Intent intent = new Intent("/");
//        ComponentName cm = new ComponentName("com.android.settings",
//                "com.android.settings.WirelessSettings");
//        intent.setComponent(cm);
//        intent.setAction("android.intent.action.VIEW");
//        activity.startActivityForResult(intent,OPEN_NETWORK_SETTINGS);
//    }
}
