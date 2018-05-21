package com.example.myutils.utils.systemUtils

import android.content.Context
import android.os.Environment

import com.example.myutils.base.BaseApplication

import java.io.File
import java.text.DecimalFormat

/**
 * 项 目 名: MyUtils
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/12/6 13:43
 * 本类描述:
 */

object FileDir {
    /* /storage/emulated/0 */
    val STORAGE = Environment.getExternalStorageDirectory().toString()

    /**
     * 内部存储路径（相当于系统盘）
     */
    /* /data/user/0/com.scy.myappupdate/files */
    val appFiles: String
        get() = BaseApplication.getAppContext().filesDir.absolutePath

    /* /data/user/0/com.scy.myappupdate/cache */
    val appCache: String
        get() = BaseApplication.getAppContext().cacheDir.absolutePath

    /**
     * 外部存储
     */
    /* /storage/emulated/0/Android/data/com.scy.myappupdate/files */
    val storageFiles: String
        get() = BaseApplication.getAppContext().getExternalFilesDir("")!!.absolutePath

    /* /storage/emulated/0/Android/data/com.scy.myappupdate/cache */
    val storageCache: String
        get() = BaseApplication.getAppContext().externalCacheDir!!.absolutePath

    /**
     * 图库位置
     */

    /* /storage/emulated/0/DCIM */
    /* /storage/emulated/0/DCIM/protectorPhoto    加了path之后*/
    fun getDCIM(path: String): String {
        return "/storage/emulated/0/DCIM/$path"
    }

    /**
     * 获取缓存
     */
    fun getDisk(context: Context): String {
        return getDiskSize(getFolderSize(File(STORAGE + context.packageName))
                + getFolderSize(BaseApplication.getAppContext().cacheDir)
                + getFolderSize(BaseApplication.getAppContext().filesDir))
    }

    // 获取指定文件夹内所有文件大小的和
    private fun getFolderSize(file: File): Long {
        var size: Long = 0
        try {
            val fileList = file.listFiles()
            for (aFileList in fileList) {
                if (aFileList.isDirectory) {
                    size = size + getFolderSize(aFileList)
                } else {
                    size = size + aFileList.length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return size
    }

    /**
     * 单位转换
     */
    private fun getDiskSize(size: Long): String {
        val df = DecimalFormat("######0.00")//保留两位小数
        //        if (size > 100) {
        val b = (size / 1024).toDouble()
        //            if (b > 100)
        return df.format(b / 1024) + " MB"
        //            else
        //                return df.format(b) + " KB";
        //        } else
        //            return size + " B";
    }

    /**
     * 删除文件
     */
    fun clearDisk(context: Context) {
        deleteFolderSize(File(STORAGE + context.packageName))
        deleteFolderSize(BaseApplication.getAppContext().cacheDir)
        deleteFolderSize(BaseApplication.getAppContext().filesDir)
    }

    private fun deleteFolderSize(file: File) {
        try {
            val fileList = file.listFiles()
            for (aFileList in fileList) {
                if (aFileList.isDirectory) {
                    deleteFolderSize(aFileList)
                } else {
                    L.out("--->" + aFileList.delete())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
