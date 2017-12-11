package com.example.myutils.utils.systemUtils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;


import com.example.myutils.utils.systemUtils.L;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 项目名称： MyTest
 * 创 建 人： 艺仔加油
 * 创建时间： 2017/8/23 15:32
 * 本类描述： 下载doc/pdf/exel...文件，并打开
 * DocUtils docUtils = new DocUtils(this, docStr); //docStr下载链接
 * docUtils.downDoc(indentDownloadText, mHandler); //indentDownloadText是一个TextView
 */

public class DocUtils {
    private Context context;
    private File file1;
    private String Strname;

    public DocUtils(Context context, String Strname) {
        this.context = context;
        this.Strname = Strname;
        String s = Strname.substring(Strname.length() - 14); //截取最后14位 作为文件名
        file1 = new File(Environment.getExternalStorageDirectory(), getFileName(s)); //文件存储
        L.v("doc文件路径",file1.getPath());
        L.v("doc文件路径Strname",Strname);
    }

    /**
     * 该文件是否存在
     *
     * @return
     */
    public boolean beingDoc() {
        File haha = new File(file1.getAbsolutePath());
        //判断是否有此文件
        if (haha.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 下载Doc
     */
    public void downDoc() {
        new Thread() {
            public void run() {
                File haha = new File(file1.getAbsolutePath());
                //判断是否有此文件
                if (haha.exists()) {
                    //有缓存文件,拿到路径 直接打开
                    openDoc(haha);
                    return;
                }
                //本地没有此文件 则从网上下载打开
                File downloadfile = downLoad(Strname, file1.getAbsolutePath());
                if (downloadfile != null) {
                    // 下载成功,安装....
                    openDoc(downloadfile);
                } else {
                    // 提示用户下载失败.
                    Toast.makeText(context, "文件加载失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.start();
    }

    public void downDoc(final TextView textView, final Handler handler) {
        new Thread() {
            public void run() {
                File haha = new File(file1.getAbsolutePath());
                //判断是否有此文件
                if (haha.exists()) {
                    //有缓存文件,拿到路径 直接打开
                    openDoc(haha);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setClickable(true);
                            textView.setText("查看");
                        }
                    });
                    return;
                }else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("下载中");
                        }
                    });
                }
                //本地没有此文件 则从网上下载打开
                File downloadfile = downLoad(Strname, file1.getAbsolutePath());
                if (downloadfile != null) {
                    // 下载成功,安装....
                    openDoc(downloadfile);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("查看");
                            textView.setClickable(true);
                        }
                    });
                } else {
                    // 提示用户下载失败.
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText("下载");
                            textView.setClickable(true);
                            Toast.makeText(context, "文件加载失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }.start();
    }

    /**
     * 下载完成后  直接打开文件
     */
    public void openDoc(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        context.startActivity(intent);

    }

    /**
     * 传入文件 url  文件路径  和 弹出的dialog  进行 下载文档
     */
    public File downLoad(String serverpath, String savedfilepath) {
        try {
            URL url = new URL(serverpath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                File file = new File(savedfilepath);
                FileOutputStream fos = new FileOutputStream(file);
                int len = 0;
                byte[] buffer = new byte[1024];
                int total = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    total += len;
                }
                fos.flush();
                fos.close();
                is.close();
                return file;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getFileName(String serverurl) {
        return serverurl.substring(serverurl.lastIndexOf("/") + 1);
    }
}
