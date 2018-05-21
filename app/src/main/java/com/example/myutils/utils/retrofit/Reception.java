package com.example.myutils.utils.retrofit;


import com.example.myutils.base.BaseBean;
import com.example.myutils.base.ProtectMan;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 项目名称： MyTest
 * 创 建 人： 艺仔加油
 * 创建时间： 2017/10/25 10:27
 * 本类描述：
 * （1）Retrofit2的三类注解：HTTP请求方法、标记类、参数类
 * 1.HTTP请求方法：GET、POST、PUT、DELETE、PATCH、HEAD、OPTIONS、HTTP(用于替换以上7个，以及其他扩展方法)
 * 2.标   记   类：FormUrlEncoded、Multipart、Streaming
 * 3.参   数   类：Headers、Header、Body、Field、FieldMap、Part、PartMap、Path、Query、QueryMap、Url
 * （2）用法：
 * 1.
 * method 表示请求的方法，区分大小写
 * path表示路径  {}为占位符
 * hasBody表示是否有请求体
 *
 * @HTTP(method = "GET", path = "blog/{id}", hasBody = false)
 * Call<ResponseBody> getBlog(@Path("id") int id);
 * <p>
 * Call<ResponseBody> foo(@Query("id") List<Integer> id);
 */

public interface Reception {
    String ip = "http://192.168.1.145:8080/";

    @POST("AndroidWebTest/student")
    Observable<String> getPostCall(@QueryMap Map<String, String> options);

    @POST("AndroidWebTest/student")
    Observable<String> getPostCall(@Query("scy") String flag, @Query("user") String temp);

    /**
     * 上传文件
     */
    @Multipart
    @POST("/API/APP/Upload/Uploader.ashx")
    Observable<String> upload(@Part List<MultipartBody.Part> multipart);

    /**
     * 下载文件
     */
    @Streaming
    @GET
    Observable<ResponseBody> down(@Url String fileUrl);

}
