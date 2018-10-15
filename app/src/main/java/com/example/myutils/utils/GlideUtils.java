package com.example.myutils.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myutils.MainActivity;
import com.example.myutils.base.BaseApplication;

import java.io.File;
import java.net.URL;

/**
 * 项 目 名: MyUtils
 * 创 建 人: 艺仔加油
 * 创建时间: 2018/3/29 16:17
 * 本类描述:
 */

/*
        RequestOptions options = new RequestOptions();
        options.fallback(R.mipmap.ic_launcher);           //当url为null或则不合法的时候会调用
        options.placeholder(R.mipmap.ic_launcher_round); //加载图片前的调用
        options.error(R.mipmap.a);                       //加载图片出错之后的调用
        options.circleCrop();                           //圆形  缩略图
        options.optionalCircleCrop();                   //圆形 原图显示    optional开头的都是原图显示
        options.centerCrop();                           //图片居中显示
        options.fitCenter();                           //适应图片显示
        options.autoClone();                           //自动档
        options.skipMemoryCache(true);                 //默认false，true为跳过内存缓存
        options.diskCacheStrategy(DiskCacheStrategy.NONE);//ALL:缓存全部尺寸（缩略图和原图）的图片；NONE:不缓存；RESOURCE:仅缓存缩略图；SOURCE:仅缓存原图；AUTOMATIC:自动
        options.override(50,50); //图片显示区域不变，改变图片像素。例如50*50，如果原图像素大于50*50，那个图片将会模糊，反之大小不成立

        Glide.with(this).load(image3).apply(options).into(imageView);//

        TransitionOptions transitionOptions = new DrawableTransitionOptions().crossFade(500); //淡入，间隔时间500毫秒；不填，默认为300毫秒

        Glide.with(this).load(image3).transition(transitionOptions).thumbnail(Glide.with(this).load(R.mipmap.ic_launcher)).into(imageView);   //thumbnail为预览显示(显示缩略图)
 */

public class GlideUtils {

    /**
     * 加载图片
     *
     * @param object    传入参数类型：Bitmap、Drawable、String、Uri、File、Integer、URL、byte[]、Object
     * @param imageView 传入ImageView对象
     */
    public void load(Object object, ImageView imageView) {
        Glide.with(BaseApplication.getAppContext()).load(object).into(imageView);
    }

    public void load(Object object, int placeholder, ImageView imageView) {
        RequestOptions options = new RequestOptions();
        options.placeholder(placeholder);
        Glide.with(BaseApplication.getAppContext()).load(object).apply(options).into(imageView);
    }

    //加载Gif图
    public void loadGif(Object object, ImageView imageView) {
        Glide.with(BaseApplication.getAppContext()).asFile().load(object).into(imageView);
    }

    /**
     * 获取Glide的所有磁盘缓存
     * 单位大小为B
     * 如果要装化成MB：
     * 例，return为x，得到MB数值为y
     * y = x / 1024 /1024
     */
    public static long getGlideDisk() {
        return getFolderSize(BaseApplication.getAppContext().getCacheDir());
    }

    /**
     * 清除Glide的所有磁盘缓存
     */
    public void clearGlideDisk() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(BaseApplication.getAppContext()).clearDiskCache();
            }
        }).start();
    }

    // 获取指定文件夹内所有文件大小的和
    private static long getFolderSize(File file) {
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
}
