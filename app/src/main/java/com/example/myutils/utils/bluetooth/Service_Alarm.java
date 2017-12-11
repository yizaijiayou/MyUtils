package com.example.myutils.utils.bluetooth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.myutils.utils.systemUtils.L;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.util.UUID;

/**
 * 项目名称： ProtectMessenger
 * 创 建 人： 艺仔加油
 * 创建时间： 2017/9/26 16:01
 * 本类描述： 目前这个服务是例子
 */

public class Service_Alarm extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        if (!TextUtils.isEmpty(BluetoothUtils.getMac()))
            BluetoothUtils.getInstance().openNotify(BluetoothUtils.getMac(), new BleNotifyResponse() {
                @Override
                public void onNotify(UUID service, UUID character, byte[] value) {
                    if (service.equals(BluetoothUtils.serviceUUID) && character.equals(BluetoothUtils.characterUUID)) {
                        String notision = ByteUtils.byteToString(value);
                        L.d("110", "notision = " + notision);
                        if (notision.equals("02")) {//报警

                        }
                    }
                }

                @Override
                public void onResponse(int code) {

                }
            });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.d("Service_Alarm", "flags-------->" + flags + "------startId----->" + startId);
        try {
            stopSelf(startId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent,flags,startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
