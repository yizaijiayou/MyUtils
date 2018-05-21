package com.example.myutils.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import com.example.myutils.MainActivity;
import com.zxy.tiny.Tiny;

/**
 * Created by Administrator on 2017/11/1.
 */

public class BaseApplication extends Application {
    private static Context context;
    private static Activity activity;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        getActivity();
    }

    private void getActivity() {
        //监听Activity状态
    }

    public static Context getAppContext() {
        return context;
    }

}
