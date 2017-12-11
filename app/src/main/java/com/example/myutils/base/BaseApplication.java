package com.example.myutils.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import com.example.myutils.MainActivity;

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
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {
                BaseApplication.activity = activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    public static Context getAppContext() {
        return context;
    }

    public static Activity getAppTopActivity() {
        return activity;
    }
}
