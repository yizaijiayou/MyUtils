package com.example.myutils.utils.retrofit;

import android.os.Environment;
import android.os.Handler;

import com.example.myutils.base.BaseBean;
import com.example.myutils.utils.systemUtils.FileDir;
import com.example.myutils.utils.systemUtils.L;
import com.example.myutils.utils.systemUtils.NetUtils;
import com.zxy.tiny.Tiny;
import com.zxy.tiny.callback.FileBatchCallback;
import com.zxy.tiny.callback.FileCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.internal.observers.BlockingBaseObserver;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/*     在NetWorkController中的使用
在Reception的retrofit接口为
    @POST("AndroidWebTest/student")
    Observable<String> getPostCall(@QueryMap Map<String, String> options);
先写一个方法
  private static Observable<BaseBean<List<User>>> postOb(Map<String, String> maps) {
        return Retrofit2Utils.getReception()
                .getPostCall(maps)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
  }
使用这个方法
  //GetJsonIbject<List<User>>  是一个GetJSonIbject<T>的接口类，用于回调到界面
  public static void getPostOb(Context context, AlertDialog alertDialog, String s1, String s2, GetJsonIbject<List<User>> getJSonIbject){
       Map<String, String> maps = new HashMap<>();
       maps.put("scy", s1);
       maps.put("user", s2);
       postOb(maps).subscribe(new BaseObserver<List<User>>(context,alertDialog) {
            @Override
            public void onSuccess(List<User> users) {
                 getJSonIbject.getJSonIbject(users);
     //                Log.d("MainActivity",users.size()+"<-->");
            }
       });
  }
 */
public class Retrofit2Utils {
    private final static String TAG = "Retrofit2Utils";
    private final static int READ_TIMEOUT = 30;
    private final static int WRITE_TIMEOUT = 30;
    private final static int CONNECT_TIMEOUT = 30;

    private static Retrofit2Utils retrofit2Utils;

    private Retrofit retrofit;

    public static Retrofit2Utils getInstance() {
        if (retrofit2Utils == null) retrofit2Utils = new Retrofit2Utils();
        return retrofit2Utils;
    }

    private Retrofit2Utils() {
        Interceptor cacheInterceptor = chain -> {
            Request request = chain.request();
            if (!NetUtils.isConnected()) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            Response.Builder newBuilder = response.newBuilder();
            if (NetUtils.isConnected()) {
                int maxAge = 0;
                // 有网络时 设置缓存超时时间0个小时
                newBuilder.header("Cache-Control", "public, max-age=" + maxAge);
            } else {
                // 无网络时，设置超时为4周
                int maxStale = 60 * 60 * 24 * 28;
                newBuilder.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
            }
            return newBuilder.build();
        };

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)  //错误重连
                .cache(new Cache(new File(Environment.getExternalStorageDirectory() + "newCache"), 1024 * 1024 * 50)) //设置缓存
                .addInterceptor(cacheInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Reception.ip)
                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    private Retrofit getRetrofit(){
        return retrofit;
    }
    //********************************************************************************************************************************************************************************
    public static Reception create() {
        return getInstance().getRetrofit().create(Reception.class);
    }

    /**
     * 上传文件
     * API：
     *
     * @Multipart
     * @POST("/API/APP/Upload/Uploader.ashx") Observable<String> upload(@Part List<MultipartBody.Part> multipart);
     * 如果您的Retrofit2用的是GsonConverterFactory，那个这里的String要改成一个实体类,否则上传失败
     */
    public void upload(File[] files, Map<String, String> map, GetJsonIbject<String> getJsonIbject) {
        Tiny.FileCompressOptions options = new Tiny.FileCompressOptions();
        Tiny.getInstance().source(files).batchAsFile().withOptions(options).batchCompress(new FileBatchCallback() {
            @Override
            public void callback(boolean isSuccess, String[] outfile, Throwable t) {
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);

                //加参数
                Set<String> set = map.keySet();
                for (String s : set) {
                    builder.addFormDataPart(s, map.get(s));
                }

                //加图片，可以N张
                for (int i = 0; i < files.length; i++) {
                    builder.addFormDataPart("image", "photo" + i + ".jpg", RequestBody.create(MediaType.parse("application/octet-stream"), new File(outfile[i]))).build();
                }

                List<MultipartBody.Part> part = builder.build().parts();
                getRetrofit().create(Reception.class)
                        .upload(part)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BlockingBaseObserver<String>() {
                            @Override
                            public void onNext(String s) {
                                getJsonIbject.getJSonIbject(s);
                            }

                            @Override
                            public void onError(Throwable e) {
                                L.e(TAG, "---->" + e.getMessage());
                            }
                        });
            }
        });
    }

    public void upload(File file, Map<String, String> map, GetJsonIbject<String> getJsonIbject) {
        File[] files = {file};
        upload(files, map, getJsonIbject);
    }

    /**
     * 下载文件
     *
     * @param downUrl            下载路径
     * @param handler            主线程
     * @param onDownloadListener 下载回调
     *                           API
     * @Streaming //预防大文件，所以这里用Streaming
     * @GET Observable<ResponseBody> down(@Url String fileUrl);
     */
    public void down(String downUrl, Handler handler, OnDownloadListener onDownloadListener) {
        File file = new File(FileDir.STORAGE, System.currentTimeMillis()+".apk");  //下载存放的文件

        getInstance().getRetrofit().create(Reception.class)
                .down(downUrl)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())    //子线程
                .subscribe(new BlockingBaseObserver<ResponseBody>() {
                    @Override
                    public void onNext(ResponseBody responseBody) {
                        InputStream is = null;
                        FileOutputStream fos = null;
                        byte[] bys = new byte[2048];
                        int len = 0;

                        try {
                            is = responseBody.byteStream();
                            long total = responseBody.contentLength();
                            fos = new FileOutputStream(file);
                            long sum = 0;
                            while ((len = is.read(bys)) != -1) {
                                fos.write(bys, 0, len);
                                sum += len;
                                int progress = (int) (sum * 1.0f / total * 100);
                                handler.post(() -> onDownloadListener.onDownloading(progress));
                            }
                            fos.flush();
                            handler.post(() -> onDownloadListener.onDownloadSuccess(file));
                        } catch (Exception e) {
                            handler.post(() -> onDownloadListener.onDownloadFailed());
                            e.printStackTrace();
                        } finally {
                            try {
                                if (is != null) is.close();
                                if (fos != null) fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        handler.post(() -> onDownloadListener.onDownloadFailed());
                    }
                });
    }

    public interface OnDownloadListener {
        void onDownloadSuccess(File file);

        void onDownloading(int progress);

        void onDownloadFailed();
    }
}
