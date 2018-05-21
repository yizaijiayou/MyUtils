package com.example.myutils.utils.retrofit;

import android.content.Context;
import android.util.Log;

import com.example.myutils.R;
import com.example.myutils.base.BaseApplication;
import com.example.myutils.base.BaseBean;
import com.example.myutils.utils.systemUtils.ToastUtils;
import com.example.myutils.utils.loadingDialog.LoadingDialog;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 项 目 名: MyTest
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/3 15:00
 * 本类描述:当需要用到loadingDialg等的操作是请用BaseObserver，若只需要访问网络请求，可使用BaseSimpleObserver
 */

public abstract class BaseObserver<T> implements Observer<T> {

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
        e.printStackTrace();
        Log.i("BaseObserver", "--------------------onError--------------------->" + e.getMessage());
        onFailure(e);
    }

    @Override
    public void onComplete() {
        Log.i("BaseObserver", "--------------------onComplete---------------------");
        onFailure(null);
    }

    public abstract void onSuccess(T t);

    public abstract void onFailure(Throwable e);

}
