package com.example.myutils.utils.systemUtils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.myutils.base.BaseApplication;

/**
 * Project name ElectroCar
 * Created by 哎呀呀！要努力打代码啦！··
 * on 2017/5/2 13:47.
 * 本类描述：本应用程序相关的辅助类
 */
public class AppUtils {
    public AppUtils(){
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获取应用程序的名称
     */
    public static String getAppName(){
        Context context = BaseApplication.getAppContext();
        try{
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用程序版本名称信息
     */
    public static String getVersionName(){
        Context context = BaseApplication.getAppContext();
        try{
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            return packageInfo.versionName;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用程序版本号
     */
    public static int getVersion(){
        Context context = BaseApplication.getAppContext();
        try{
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),0);
            return packageInfo.versionCode;
        }catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
        return 0;
    }
}
