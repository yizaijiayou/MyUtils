package com.example.myutils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.myutils.base.BaseActivity;
import com.example.myutils.base.BaseApplication;
import com.example.myutils.base.PermissonListener;
import com.example.myutils.utils.scan.zxing.CaptureActivity;
import com.example.myutils.utils.systemUtils.FileDir;
import com.example.myutils.utils.systemUtils.L;

import java.io.IOException;
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
        //播放铃声
        SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        int soundId = soundPool.load(BaseApplication.getAppContext(), R.raw.lingsheng, 1);
        int stream = soundPool.play(soundId, 0.8f, 0.8f, 1, 0, 1.0f);
        soundPool.setOnLoadCompleteListener((soundPool1, sampleId, status) -> soundPool1.play(sampleId, 0.8f, 0.8f, 1, 0, 1.0f));

//        MediaPlayer mMediaPlayer = new MediaPlayer();
//
//        try {
//            mMediaPlayer.setDataSource();
//            mMediaPlayer.prepare();
//            mMediaPlayer.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        requestPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissonListener() {
//            @Override
//            public void onGranted() {
//                Intent intent = new Intent(MainActivity.this,CaptureActivity.class);
//                startActivityForResult(intent, CaptureActivity.SCANNING_CODE);
//            }
//
//            @Override
//            public void onFature(String s) {
//
//            }
//        });
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
