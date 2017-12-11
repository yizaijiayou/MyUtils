package com.example.myutils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.myutils.base.BaseActivity;
import com.example.myutils.base.BaseApplication;
import com.example.myutils.base.PermissonListener;
import com.example.myutils.utils.scan.zxing.CaptureActivity;
import com.example.myutils.utils.systemUtils.FileDir;
import com.example.myutils.utils.systemUtils.L;

import java.util.List;

/**
 * 项 目 名: MyUtils
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/3 10:48
 * 本类描述:
 */

public class MainActivity extends BaseActivity {
    private ImageView icon;
    @Override
    public int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout,new MainFragment()).commit();
    }

    @Override
    public void initView() {
        icon = $(R.id.icon);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    public void doWork(Context context) {

    }


    public void getClick(View v){
        requestPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissonListener() {
            @Override
            public void onGranted() {
                Intent intent = new Intent(MainActivity.this,CaptureActivity.class);
                startActivityForResult(intent, CaptureActivity.SCANNING_CODE);
            }

            @Override
            public void onFature(List<String> permissonList) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == CaptureActivity.SCANNING_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(CaptureActivity.DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(CaptureActivity.DECODED_BITMAP_KEY);
            }
        }
    }
}
