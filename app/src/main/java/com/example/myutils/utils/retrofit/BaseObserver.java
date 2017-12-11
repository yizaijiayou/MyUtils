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
 * 本类描述:
 */

public abstract class BaseObserver<T> implements Observer<BaseBean<T>> {
    private LoadingDialog loadingDialog;
    private Context context;

    public BaseObserver(Context context, LoadingDialog loadingDialog) {
        this.context = context;
        this.loadingDialog = loadingDialog;
    }

    @Override
    public void onSubscribe(Disposable d) {
        Log.i("BaseObserver","--------------------onSubscribe---------------------");
        showLoadingDialog();
    }

    @Override
    public void onNext(BaseBean<T> tBaseBean) {
        Log.i("BaseObserver","--------------------onNext---------------------");
        if (tBaseBean.isResult()){
            onSuccess(tBaseBean.getData());
        }else{
            ToastUtils.show(tBaseBean.getMsg());
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.i("BaseObserver","--------------------onError---------------------");
        ToastUtils.show(BaseApplication.getAppContext().getString(R.string.errorConnect));
        cancelLoadingDialog();
    }

    @Override
    public void onComplete() {
        Log.i("BaseObserver","--------------------onComplete---------------------");
        cancelLoadingDialog();
    }

    public abstract void onSuccess(T t);

    protected void showLoadingDialog(){
        if (loadingDialog != null && !loadingDialog.isShowing()){
            loadingDialog.show();
        }
    }
    protected void cancelLoadingDialog(){
        if (loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }
}
