package com.example.myutils;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myutils.base.BaseFragment;
import com.example.myutils.base.PermissonListener;
import com.example.myutils.utils.systemUtils.L;

import java.util.List;

/**
 * 项 目 名: MyUtils
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/29 11:17
 * 本类描述:
 */

public class MainFragment extends BaseFragment {
    private Button button;
    @Override
    public int getContentLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    public void initView(View view) {
        button = $(view,R.id.button);
    }

    @Override
    public void initListener() {
        button.setOnClickListener(this);
    }

    @Override
    public void widgetClick(View v) {
        requestPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, null);
    }

    @Override
    public void doWork(Context context) {

    }
}
