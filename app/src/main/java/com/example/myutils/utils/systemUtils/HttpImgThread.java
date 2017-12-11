package com.example.myutils.utils.systemUtils;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;

/**
 * 项目名称:WatchImage.
 * 创建时间: 2017/5/28 19:31.
 * 创 建 人: 苏成艺.
 * 本类描述:根据Url下载图片用的线程
 */

public class HttpImgThread extends Thread {
    private Context mContext;
    private ImageView iv;
    private String strurl;
    private Handler mHandler;
    private int flag;

    public HttpImgThread(int flag, Context mContext, ImageView iv, String strurl, Handler mHandler) {
        this.mContext = mContext;
        this.iv = iv;
        this.strurl = strurl;
        this.mHandler = mHandler;
        this.flag = flag;
    }

    @Override
    public void run() {
        URL url = null;
        HttpURLConnection con = null;
        try {
            url = new URL(strurl);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setReadTimeout(10000);
            con.setDoInput(true);//有个这个方法之后可以使用getouputStream()或者getinputStream()
            InputStream is = con.getInputStream();
            File parent = new File(Environment.getExternalStorageDirectory() + "/ElectroCar/TrueName");
            if (!parent.exists()) {
                parent.mkdirs();
            }
            final File file = new File(parent, "TrueName" + flag + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);

            byte[] bys = new byte[2 * 1024];
            int len = -1;
            while ((len = is.read(bys)) != -1) {
                fos.write(bys, 0, len);
            }
            is.close();
            fos.close();

            mHandler.post(new Runnable() {
                @Override
                public void run() {
//                    Glide.with(mContext).load(file).into(iv);
                }
            });
        } catch (SocketException e) {
            Toast.makeText(mContext, "加载图片失败,请重试", Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
