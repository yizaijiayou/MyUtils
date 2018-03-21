package com.example.myutils.utils.okhttp3;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 项 目 名: MyTest
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/12/27 15:44
 * 本类描述: 拦截OkHttp3，并打印访问url和输出的json
 * 使    用：
 * OkHttpClient.Builder builder = new OkHttpClient.Builder();
 * builder.addInterceptor(new HttpLoggingInterceptor());
 * 输出：
 * POST http://192.168.1.250:8100/API/APP/Login/Login.ashx?action=Login&PassWord=4297F44B13955235245B2497399D7A93&AccountName=13711111666
 * {"Result":true,"Msg":"","Data":{"AccountType":"Student","IsFull":"True","Token":"48B0E301832F8CDFEABCC8DFB81A4C2C"},"ErrorCode":0}
 */

public class MyHttpLoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();

        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);

        Buffer buffer = source.buffer();
        Charset charset = Charset.forName("UTF-8");

        MediaType contentType = responseBody.contentType();
        if (contentType != null) charset = contentType.charset(Charset.forName("UTF-8"));

        Log.w("httpLog", request.method() + " " + request.url() + "\n" + buffer.clone().readString(charset));
        return response;
    }
}
