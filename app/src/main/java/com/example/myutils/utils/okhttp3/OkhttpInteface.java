package com.example.myutils.utils.okhttp3;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 项目名称:BasisProject.
 * 创建时间: 2017/7/9 15:35.
 * 创 建 人: 苏成艺.
 * 本类描述: Okhttp回调接口
 */

public abstract class OkhttpInteface {
    protected abstract void onSuccess(Call call, Response response, String s);
    protected abstract void onFailure(Call call, IOException e, String s);
}
