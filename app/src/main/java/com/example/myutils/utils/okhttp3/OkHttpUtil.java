package com.example.myutils.utils.okhttp3;

import android.os.Handler;
import android.os.Looper;

import com.example.myutils.base.BaseApplication;
import com.example.myutils.utils.okhttp3.cookie.PersistentCookieStore;
import com.example.myutils.utils.systemUtils.L;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 项目名称:BasisProject.
 * 创建时间: 2017/7/9 15:38.
 * 创 建 人: 苏成艺.
 * 本类描述:  网络工具类
 * 用法：
 * OkHttpUtil.getInstance().get(url, new OkhttpInteface() {
 *
 * @Override protected void onSuccess(okhttp3.Call call, Response response, String s) {
 * text.setText(s);
 * }
 * @Override protected void onFailure(okhttp3.Call call, IOException e, String s) {
 * }
 * });
 */

public class OkHttpUtil {
    private static OkHttpUtil mOkHttpUtil;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private static boolean isCookie = false;

    private OkHttpUtil() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        builder.connectTimeout(30, TimeUnit.SECONDS);

        //当需要用到Cookie的时候
        builder.cookieJar(new CookieJar() {
            private PersistentCookieStore cookieStore = new PersistentCookieStore(BaseApplication.getAppContext());

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                if (isCookie) {
                    isCookie = false;
                    if (cookies != null && cookies.size() > 0) {
                        for (Cookie item : cookies) {
                            cookieStore.add(url, item);
                        }
                    }
                }
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                return cookieStore.getCookies();
            }
        });

        mOkHttpClient = builder.build();
        mHandler = new Handler(Looper.getMainLooper());
    }

    //单例模式（官方建议如此操作）
    public static OkHttpUtil getInstance() {
        if (mOkHttpUtil == null) {
            mOkHttpUtil = new OkHttpUtil();
        }
        return mOkHttpUtil;
    }

    //在需要设置Cookie的地方（例如：登录），用法(与getinstance一样)：第一次用setCookie，以后用getInstance即可
    public static OkHttpUtil setCookie() {
        isCookie = true;
        mOkHttpUtil = new OkHttpUtil();
        return mOkHttpUtil;
    }

    /**
     * 下载需要用到的接口
     */
    public interface OnDownloadListener {
        void onDownloadSuccess(File file);

        void onDownloading(int progress);

        void onDownloadFailed();
    }

    //********************************************************************************************************************************
    public void get(String url, OkhttpInteface okhttpInteface) {
        Request request = new Request.Builder().url(url).build();
        doRequest(request, okhttpInteface);
    }

    public void post(String url, FormBody body, OkhttpInteface okhttpInteface) {
        Request request = new Request.Builder().url(url).post(body).build();
        doRequest(request, okhttpInteface);
    }

    public void upload(String url, RequestBody requestBody, OkhttpInteface okhttpInteface) {
        Request request = new Request.Builder().url(url).post(requestBody).build();
        doUpload(request, okhttpInteface);
        //上传文件的示例代码
        /* RequestBody body = new MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("action", "ImageUpload")
            .addFormDataPart("Token", Utils.getToken())
            .addFormDataPart("image", "user_icon.jpg", RequestBody.create(MediaType.parse("application/octet-stream"), file))  // file是一个File文件
            .build(); */
    }

    public void download(String url, String saveDir, final OnDownloadListener onDownloadListener) {
        //url = http://www.zjyca.com/UploadFile/UpAppVer/20171130110319_2017_11_28_v1_0_11.apk
        //saveDir = /storage/emulated/0/eTest.apk
        Request request = new Request.Builder().url(url).build();
        doDownload(request, saveDir, onDownloadListener);
    }

    //***********************************************************************************************************************************
    private void doDownload(Request request, String saveDir, OnDownloadListener onDownloadListener) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(() -> onDownloadListener.onDownloadFailed());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                FileOutputStream fos = null;
                byte[] bys = new byte[2048];
                int len = 0;

                isExistDir(saveDir);

                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(saveDir);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(bys)) != -1) {
                        fos.write(bys, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        mHandler.post(() -> onDownloadListener.onDownloading(progress));
                    }
                    fos.flush();

                    mHandler.post(() -> onDownloadListener.onDownloadSuccess(file));
                } catch (Exception e) {
                    mHandler.post(() -> onDownloadListener.onDownloadFailed());
                    e.printStackTrace();
                } finally {
                    if (is != null) is.close();
                    if (fos != null) fos.close();
                }
            }
        });
    }

    private void doUpload(Request request, final OkhttpInteface okhttpInteface) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        okhttpInteface.onFailure(call, e, "网络错误");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                callBack(call, response, s, okhttpInteface);
            }
        });
    }

    private void doRequest(Request request, final OkhttpInteface okhttpInteface) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        okhttpInteface.onFailure(call, e, "网络错误");
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                String s = response.body().string();
                callBack(call, response, s, okhttpInteface);
            }
        });
    }

    //***********************************************************************************************************************************************************
    private void callBack(final Call call, final Response response, final String s, final OkhttpInteface okhttpInteface) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    okhttpInteface.onSuccess(call, response, s);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //**************************************************************************************************************************************

    /**
     * 判断saveDir是否存在
     *
     * @param saveDir
     * @return
     */
    private String isExistDir(String saveDir) {
        File file = new File(saveDir);
        if (file.exists()) {
            file.delete();
        }
        return file.getAbsolutePath();
    }

    /**
     * 输出post访问网络请求的Url
     *
     * @param url
     * @param body
     */
    public void LogUrl(String url, FormBody body) {
        String endUrl = "";
        for (int i = 0; i < body.size(); i++) {
            endUrl += "&" + body.name(i) + "=" + body.value(i);
        }
        L.w("我是可以访问的Url", url + "?" + endUrl);
    }

    /**
     * 输出get访问网络请求的Url
     *
     * @param url
     */
    public void LogUrl(String url) {
        L.w("我是可以访问的Url", url);
    }
}
