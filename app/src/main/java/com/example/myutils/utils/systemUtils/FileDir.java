package com.example.myutils.utils.systemUtils;

import android.content.Context;
import android.os.Environment;

import com.example.myutils.base.BaseApplication;

import java.io.File;
import java.text.DecimalFormat;

/**
 * 项 目 名: MyUtils
 * 创 建 人: 艺仔加油
 * 创建时间: 2018/8/27 14:21
 * 本类描述:
 */
public class FileDir {
    /* /storage/emulated/0 */
    public static String STORAGE = Environment.getExternalStorageDirectory().toString();

    /**
     * 内部存储路径（相当于系统盘）
     */
    /* /data/user/0/com.scy.myappupdate/files */
    public static String getAppFiles() {
        return BaseApplication.getAppContext().getFilesDir().getAbsolutePath();
    }

    /* /data/user/0/com.scy.myappupdate/cache */
    public static String getAppCache() {
        return BaseApplication.getAppContext().getCacheDir().getAbsolutePath();
    }

    /**
     * 外部存储
     */
    /* /storage/emulated/0/Android/data/com.scy.myappupdate/files */
    public static String getSrorageFiles() {
        return BaseApplication.getAppContext().getExternalFilesDir("").getAbsolutePath();
    }

    /* /storage/emulated/0/Android/data/com.scy.myappupdate/cache */
    public static String getStorageCache() {
        return BaseApplication.getAppContext().getExternalCacheDir().getAbsolutePath();
    }

    /**
     * 图库位置
     */

    /* /storage/emulated/0/DCIM */
    /* /storage/emulated/0/DCIM/protectorPhoto    加了path之后*/
    public static String getDCIM(String path) {
        return "/storage/emulated/0/DCIM/" + path;
    }

    /**
     * 获取缓存
     */
    public static String getDisk(Context context) {
        return getDiskSize(getFolderSize(new File(STORAGE + context.getPackageName()))
                + getFolderSize(context.getCacheDir())
                + getFolderSize(context.getFilesDir()));
    }

    // 获取指定文件夹内所有文件大小的和
    private static Long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 单位转换
     */
    private static String getDiskSize(long size) {
        DecimalFormat df = new DecimalFormat("######0.00");//保留两位小数
        //        if (size > 100) {
        double b = size / 1024;
        //            if (b > 100)
        return df.format(b / 1024) + " MB";
        //            else
        //                return df.format(b) + " KB";
        //        } else
        //            return size + " B";
    }

    /**
     * 删除文件
     */
    public static void clearDisk(Context context) {
        deleteFolderSize(new File(STORAGE + context.getPackageName()));
        deleteFolderSize(context.getCacheDir());
        deleteFolderSize(context.getFilesDir());
    }

    private static void deleteFolderSize(File file) {
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    deleteFolderSize(aFileList);
                } else {
                    L.out("--->" + aFileList.delete());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
