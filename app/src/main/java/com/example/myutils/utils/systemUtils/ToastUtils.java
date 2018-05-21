package com.example.myutils.utils.systemUtils;

import android.os.Looper;
import android.widget.Toast;

import com.example.myutils.base.BaseApplication;

/**
 * 项 目 名: MyUtils
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/2 14:42
 * 本类描述:
 */

public class ToastUtils {
    private static Toast toast;
    public static synchronized Toast getInstance() {
        if (toast == null) {
            Looper.prepare();
            toast = Toast.makeText(BaseApplication.getAppContext(), "", Toast.LENGTH_SHORT);
        }
        return toast;
    }

    public static void show(String msg) {
        toast = getInstance();
        if (toast != null) {
            toast.setText(msg);
            toast.show();
        }
    }
    public static void show(int i) {
        toast = getInstance();
        if (toast != null) {
            toast.setText(BaseApplication.getAppContext().getString(i));
            toast.show();
        }
    }
}
