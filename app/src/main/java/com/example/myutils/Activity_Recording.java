package com.example.myutils;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.czt.mp3recorder.MP3Recorder;
import com.example.myutils.utils.systemUtils.FileDir;

import java.io.File;
import java.io.IOException;

/**
 * 项 目 名: MyUtils
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/12/16 15:28
 * 本类描述: 录音(生成.mp3格式)，android自带的录音不是生成.mp3格式
 * 链    接：https://github.com/CarGuo/RecordWave
 */

public class Activity_Recording extends Activity {
    //录制MP3格式的录音文件
    private MP3Recorder mp3Recorder;

    private File file;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        file = new File(FileDir.INSTANCE.getSTORAGE(),"record.mp3");
    }

    public void getClick(View v){
        switch (v.getId()){
            case R.id.button1://录音
                mp3Recorder = new MP3Recorder(file);
                try {
                    mp3Recorder.start();
                } catch (IOException e) {
                    Log.e("Activity_Recording", "startRecording");
                }
                break;
            case R.id.button2://暂停录音
                if (mp3Recorder != null) {
                    mp3Recorder.setPause(false);
                    mp3Recorder.stop();
                    mp3Recorder = null;
                }
                break;
            case R.id.button3://播放录音
                try {
                    MediaPlayer mPlayer = new MediaPlayer();
                    mPlayer.setDataSource(file.getPath());
                    mPlayer.prepare();
                    mPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
