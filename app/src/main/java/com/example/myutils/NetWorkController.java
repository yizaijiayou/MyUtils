package com.example.myutils;

import com.example.myutils.utils.retrofit.BaseObserver;
import com.example.myutils.utils.retrofit.GetJsonObject;
import com.example.myutils.utils.retrofit.Retrofit2Utils;
import com.example.myutils.utils.systemUtils.L;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * 项 目 名: MyUtils
 * 创 建 人: 艺仔加油
 * 创建时间: 2018/3/26 11:32
 * 本类描述:
 */

public class NetWorkController {

    private static Observable setObserver(Observable observer){
        return observer.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static void getRetrofit2(BaseObserver<String> baseObserver){
        setObserver(Retrofit2Utils.create().getPostCall("scy","scy"))
            .subscribe(baseObserver);
    }
}
