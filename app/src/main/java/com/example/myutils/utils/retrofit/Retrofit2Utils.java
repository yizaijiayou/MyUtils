package com.example.myutils.utils.retrofit;

import android.os.Environment;

import com.example.myutils.base.BaseApplication;
import com.example.myutils.utils.Tools;
import com.example.myutils.utils.systemUtils.NetUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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
  //GetJSonIbject<List<User>>  是一个GetJSonIbject<T>的接口类，用于回调到界面
  public static void getPostOb(Context context, AlertDialog alertDialog, String s1, String s2, GetJSonIbject<List<User>> getJSonIbject){
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
    private static Retrofit2Utils retrofit2Utils;

    public static Retrofit2Utils getInstance() {
        if (retrofit2Utils == null) retrofit2Utils = new Retrofit2Utils();
        return retrofit2Utils;
    }

    private Retrofit retrofit;

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
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)  //错误重连
                .cache(new Cache(new File(Environment.getExternalStorageDirectory() + "newCache"), 1024 * 1024 * 50)) //设置缓存
                .addInterceptor(cacheInterceptor)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(Reception.ip)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    private static Reception reception;

    public static Reception getReception() {
        if (reception == null) {
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
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)  //错误重连
                    .cache(new Cache(new File(Environment.getExternalStorageDirectory() + "newCache"), 1024 * 1024 * 50)) //设置缓存
                    .addInterceptor(cacheInterceptor)
                    .build();
            reception = new Retrofit.Builder()
                    .baseUrl(Reception.ip)
//                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build()
                    .create(Reception.class);
        }
        return reception;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    //********************************************************************************************************************************************************************************
    public Reception create() {
        return getRetrofit().create(Reception.class);
    }
}
