package com.example.myutils.utils.systemUtils;

import android.content.Context;
import android.os.Environment;

import com.example.myutils.base.BaseApplication;

/**
 * 项 目 名: MyUtils
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/12/6 13:43
 * 本类描述:
 */

public class FileDir {
    /* /storage/emulated/0 */
    public final static String STORAGE = Environment.getExternalStorageDirectory().toString();

    /**
     * 内部存储路径（相当于系统盘）
     */
    /* /data/user/0/com.scy.myappupdate/files */
    public static String getAppFiles(){
        return BaseApplication.getAppContext().getFilesDir().getAbsolutePath();
    }

    /* /data/user/0/com.scy.myappupdate/cache */
    public static String getAppCache(){
        return BaseApplication.getAppContext().getCacheDir().getAbsolutePath();
    }

    /**
     * 外部存储
     */
    /* /storage/emulated/0/Android/data/com.scy.myappupdate/files */
    public static String getStorageFiles(){
        return BaseApplication.getAppContext().getExternalFilesDir("").getAbsolutePath();
    }

    /* /storage/emulated/0/Android/data/com.scy.myappupdate/cache */
    public static String getStorageCache(){
        return BaseApplication.getAppContext().getExternalCacheDir().getAbsolutePath();
    }
}
