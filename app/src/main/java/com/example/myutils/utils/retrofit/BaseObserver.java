package com.example.myutils.utils.retrofit;

import android.content.Context;
import android.util.Log;

import com.example.myutils.R;
import com.example.myutils.base.BaseApplication;
import com.example.myutils.base.BaseBean;
import com.example.myutils.utils.systemUtils.ToastUtils;
import com.example.myutils.utils.loadingDialog.LoadingDialog;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 项 目 名: MyTest
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/3 15:00
 * 本类描述:当需要用到loadingDialg等的操作是请用BaseObserver，若只需要访问网络请求，可使用BaseSimpleObserver
 */

public abstract class BaseObserver<T> implements Observer<T> {
    private final String SOCKET_TIMEOUT_EXCEPTION = "网络连接超时，请检查您的网络状态，稍后重试";
    private final String CONNECT_EXCEPTION = "网络连接异常，请检查您的网络状态";
    private final String UNKNOWN_HOST_EXCEPTION = "网络异常，请检查您的网络状态";

    @Override
    public void onSubscribe(Disposable d) {
        Log.i("BaseObserver", "--------------------onSubscribe--------------------->");
    }

    @Override
    public void onNext(T t) {
        Log.i("BaseObserver", "--------------------onNext---------------------");
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        Log.i("BaseObserver", "--------------------onError--------------------->" + e.getMessage());
//        onFailure(e);
        if (e instanceof SocketTimeoutException) {
            onFailure(SOCKET_TIMEOUT_EXCEPTION);
        } else if (e instanceof ConnectException) {
            onFailure(CONNECT_EXCEPTION);
        } else if (e instanceof UnknownHostException) {
            onFailure(UNKNOWN_HOST_EXCEPTION);
        } else {
            onFailure(e.getMessage());
        }
    }

    @Override
    public void onComplete() {
        Log.i("BaseObserver", "--------------------onComplete---------------------");
    }


    public abstract void onSuccess(T t);

    public abstract void onFailure(String e);
}
