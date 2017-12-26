package com.example.myutils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;

/**
 * 项 目 名: MyUtils
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/12/16 15:51
 * 本类描述: 科大讯飞（语音在线合成）
 * 一定要在libs
 * 1.添加Msc.jar
 * 2.arm64-v8a、armeabi、armeabi-v7a、mips、mips64、x86、x86_64 中的 libmsc.so
 * 不加无法播放语音
 */

public class Activity_Speech extends AppCompatActivity {
    private final static String appid = "59dd9205";
    private SpeechSynthesizer speechSynthesizer;
    private String s = "我是语音在线合成，要播放的语音。";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SpeechUtility.createUtility(this, SpeechConstant.APPID+"="+appid);
        speechSynthesizer = SpeechSynthesizer.createSynthesizer(this,null);
    }
    public void getClick(View v){//播放
        speechSynthesizer.startSpeaking(s, null);
    }
}
