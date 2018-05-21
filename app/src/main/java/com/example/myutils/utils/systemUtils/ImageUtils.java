package com.example.myutils.utils.systemUtils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.myutils.R;
import com.hitomi.glideloader.GlideImageLoader;
import com.hitomi.tilibrary.style.progress.ProgressPieIndicator;
import com.hitomi.tilibrary.transfer.TransferConfig;
import com.hitomi.tilibrary.transfer.Transferee;

import java.util.ArrayList;
import java.util.List;

/**
 * 创 建 人： 艺仔加油
 * 创建时间： 2017/8/10 16:19
 * 本类描述： 图片工具类
 */

public class ImageUtils {
    private static Transferee transferee;
//    private static List<String> lists;
//    private static List<ImageView> images;

    private static Transferee getInstance(Context context) {
        if (transferee == null) {
            transferee = Transferee.getDefault(context);
        }
        return transferee;
    }

    public static void load(final Activity context, ImageView imageView, String url) {
        List<String> lists = new ArrayList<>();
        final List<ImageView> images = new ArrayList<>();
        images.add(imageView);
        if (url == null){
        }
        lists.add(url);
        Glide.with(context).load(lists.get(0)).into(imageView);
        final TransferConfig transferConfig = initConfig(context, images, lists);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transferConfig.setNowThumbnailIndex(images.indexOf(view));
                if (context.isFinishing()) {
                    L.d("aaaaaaaaaaaaaaaaaaaaaaa", "isfinish =" + context.isFinishing());
                } else {
                    L.d("aaaaaaaaaaaaaaaaaaaaaaa", "isfinish =" + context.isFinishing());
                    getInstance(context).apply(transferConfig).show();
                }
            }
        });
    }

    public static void load(final Context context, ImageView imageView, List<String> list) {
        final List<ImageView> images = new ArrayList<>();
        List<String> lists = new ArrayList<>();
        lists.addAll(list);
        images.add(imageView);
        final TransferConfig transferConfig = initConfig(context, images, lists);
        Glide.with(context).load(lists.get(0)).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transferConfig.setNowThumbnailIndex(images.indexOf(view));
                getInstance(context).apply(transferConfig).show();
            }
        });
    }

    public static void load(final Context context, final List<ImageView> imagesList, List<String> list) {
        final TransferConfig transferConfig = initConfig(context, imagesList, list);
        for (int i = 0; i < list.size(); i++) {
            Glide.with(context).load(list.get(i)).into(imagesList.get(i));
            imagesList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    transferConfig.setNowThumbnailIndex(imagesList.indexOf(view));
                    getInstance(context).apply(transferConfig).show();
                }
            });
        }
    }

    private static TransferConfig initConfig(Context context, List<ImageView> images, List<String> lists) {
        TransferConfig config = TransferConfig.build()
                .setSourceImageList(lists)
                .setOriginImageList(images)
                .setMissPlaceHolder(R.mipmap.ic_launcher_round)
                .setProgressIndicator(new ProgressPieIndicator())
                .setImageLoader(GlideImageLoader.with(context))
                .setJustLoadHitImage(true)
                .create();
        return config;
    }

    public static void destory() {
        if (transferee != null) {
            transferee.destroy();
            transferee = null;
        }
    }
}
