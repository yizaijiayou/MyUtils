package com.example.myutils.utils.bluetooth;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.myutils.R;
import com.example.myutils.utils.systemUtils.L;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.utils.ByteUtils;

import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

/**
 * 项目名称： MyCamera
 * 创 建 人： 艺仔加油
 * 创建时间： 2017/9/25 15:14
 * 本类描述： 蓝牙工具类(https://github.com/dingjikerbo/BluetoothKit/tree/c34509efa6fe003fd1688ab40ababa815e8b8184)
 */

public class BluetoothUtils {
    public final static UUID serviceUUID = UUID.fromString("0000FFD0-0000-1000-8000-00805F9B34FB");
    public final static UUID characterUUID = UUID.fromString("0000FFD1-0000-1000-8000-00805F9B34FB");
    private static BluetoothUtils bluetoothUtils;

    public static BluetoothUtils getInstance() {
        if (bluetoothUtils == null) bluetoothUtils = new BluetoothUtils();
        return bluetoothUtils;
    }

    /**
     * 存储相关变量
     */
    private static String name = "";

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        BluetoothUtils.name = name;
    }

    private static String mac = "";

    public static String getMac() {
        return mac;
    }

    public static void setMac(String mac) {
        BluetoothUtils.mac = mac;
    }

    /**
     * 1.全局初始化BluetoothClient
     * 注意，只要初始化一次就可以了
     */
    private static BluetoothClient mClient;

    public static void init(Context context) {
        mClient = new BluetoothClient(context);
    }

    /**
     * 2.设备扫描
     *
     * @param searchResponse
     */
    public void ScanBlueDevice(SearchResponse searchResponse) {
        if (openBluetooth()) {
            SearchRequest request = new SearchRequest.Builder()
                    .searchBluetoothLeDevice(5000, 2)   //先扫描BLE设备2次，每次5s
//                    .searchBluetoothClassicDevice(5000)//再扫描蓝牙5s
//                    .searchBluetoothLeDevice(2000)     //再扫描BLE设备2秒
                    .build();

            mClient.search(request, searchResponse);
        }
    }

    //2.1取消扫描
    public void stopSearch() {
        mClient.stopSearch();
    }

    /**
     * 3.蓝牙开关
     * 如果没有开启蓝牙，主动开启蓝牙
     */
    public boolean openBluetooth() {
        if (!mClient.isBluetoothOpened()) {
            mClient.openBluetooth();
            return false;
        } else {
            return true;
        }
    }

    /**
     * 4.蓝牙设备连接
     */
    public static BleConnectResponse mBleConnectResponse;

    public void connect(String mac, BleConnectResponse bleConnectResponse) {
        stopSearch();//连接蓝牙设备时，取消搜索
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(2)              //连接如果失败重试2次
                .setConnectTimeout(30000)        //连接超时30秒
                .setServiceDiscoverRetry(3)      //发现服务如果失败重试3次
                .setServiceDiscoverTimeout(20000)//发现服务超时20s
                .build();
        mClient.connect(mac, options, bleConnectResponse);
        mBleConnectResponse = bleConnectResponse;
    }

    /**
     * 5.连接设备成功之后要进行监听
     * 万一断开连接要重新连接
     */
    public BleConnectStatusListener bleConnectStatusListener = new BleConnectStatusListener() {
        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == STATUS_CONNECTED) {  //连接
//                if (fragmentContext != null)
//                    Toast.makeText(fragmentContext, fragmentContext.getString(R.string.bluetooth_connected_notices), Toast.LENGTH_SHORT).show();
                connectBlueDevice();
                L.out("connect111----------------------------------------STATUS_CONNECTED");
            } else if (status == STATUS_DISCONNECTED) {   //断开连接
                if (!TextUtils.isEmpty(BluetoothUtils.getMac())) {
//                    if (fragmentContext != null)
//                        Toast.makeText(fragmentContext, fragmentContext.getString(R.string.bluetooth_disconnected_notices), Toast.LENGTH_SHORT).show();
//                    DataBaseUtils.addRecord(fragmentSqLiteDatabase, DataBaseUtils.STATE_CLOSE);
                    BluetoothUtils.setMac("");
                    unConnectBlueDevice();
                    if (mBleConnectResponse != null) connect(mac, mBleConnectResponse);
                    L.out("connect111----------------------------------------STATUS_DISCONNECTED");
                }
            }
        }
    };

    private void connectBlueDevice() {
//        if (deviceName != null)
//            deviceName.setText(BluetoothUtils.getName() + "\n" + fragmentContext.getString(R.string.bluetooth_connected));
//        if (bigButtonText != null) bigButtonText.setVisibility(View.INVISIBLE);
//        if (bigButtonImage != null) bigButtonImage.setVisibility(View.VISIBLE);
//        if (wifiOutlogin != null) wifiOutlogin.setImageResource(R.mipmap.btn_guanji_sel);
//        if (lists != null) {
//            if (searchResponse != null) ScanBlueDevice(searchResponse);
//        }
    }

    private void unConnectBlueDevice() {
//        if (deviceName != null) deviceName.setText(fragmentContext.getString(R.string.app_search));
//        if (bigButtonText != null) bigButtonText.setVisibility(View.VISIBLE);
//        if (bigButtonImage != null) bigButtonImage.setVisibility(View.INVISIBLE);
//        if (wifiOutlogin != null) wifiOutlogin.setImageResource(R.mipmap.btn_guanji_nor);
//
//        if (lists != null) {
//            lists.clear();
//            if (adapter != null) adapter.setDataList(lists);
//            if (searchResponse != null) ScanBlueDevice(searchResponse);
//        }
    }

    //注册监听
    public void registerConnectStatusListener(String mac) {
        mClient.registerConnectStatusListener(mac, bleConnectStatusListener);
    }

    //注销监听
    public void unregisterConnectStatusListener(String mac) {
        mClient.unregisterConnectStatusListener(mac, bleConnectStatusListener);
    }
//
//    //注册监听
//    public void registerConnectStatusListener(String mac,BleConnectStatusListener bleConnectStatusListener1) {
//        mClient.registerConnectStatusListener(mac, bleConnectStatusListener1);
//    }
//
//    //注销监听
//    public void unregisterConnectStatusListener(String mac,BleConnectStatusListener bleConnectStatusListener1) {
//        mClient.unregisterConnectStatusListener(mac, bleConnectStatusListener1);
//    }

    /**
     * 6.断开连接
     */
    public void disconnect(String mac) {
        mClient.disconnect(mac);
    }

    /**
     * 7.打开Notify通知
     */
    public void openNotify(String mac, BleNotifyResponse bleNotifyResponse) {
        mClient.notify(mac, BluetoothUtils.serviceUUID, BluetoothUtils.characterUUID, bleNotifyResponse);
    }

    /**
     * 8.关闭Notify通知
     */
    public void closeNotify(String mac) {
        if (!TextUtils.isEmpty(mac))
            mClient.unnotify(mac, BluetoothUtils.serviceUUID, BluetoothUtils.characterUUID, new BleUnnotifyResponse() {
                @Override
                public void onResponse(int code) {
                    L.out("BluetoothUtils------------------>" + code);
                }
            });
    }

    /**
     * 9.写Characteristic
     * 解释：就是像设备写入
     */
    public void write(String mac, String msg, BleWriteResponse bleWriteResponse) {
        mClient.write(mac, BluetoothUtils.serviceUUID, BluetoothUtils.characterUUID, ByteUtils.stringToBytes(msg), bleWriteResponse);
    }

    /**
     * 10.读Characteristic
     */
    public void read(String mac, String msg, BleReadResponse bleReadResponse) {
        mClient.read(mac, BluetoothUtils.serviceUUID, BluetoothUtils.characterUUID, bleReadResponse);
    }

    /**
     * 11.监听蓝牙开关的状态
     */
    private BluetoothStateListener mBluetoothStateListener;

    //开启监听
    public void OnBluetoothStateListener(BluetoothStateListener bluetoothStateListener) {
        mBluetoothStateListener = bluetoothStateListener;
        mClient.registerBluetoothStateListener(bluetoothStateListener);
    }

    //取消监听
    public void unregisterBluetoothStateListener() {
        if (mBluetoothStateListener != null)
            mClient.unregisterBluetoothStateListener(mBluetoothStateListener);
    }

    /**
     * 12.蓝牙设备的简介状态
     * // Constants.STATUS_UNKNOWN
     * // Constants.STATUS_DEVICE_CONNECTED
     * // Constants.STATUS_DEVICE_CONNECTING
     * // Constants.STATUS_DEVICE_DISCONNECTING
     * // Constants.STATUS_DEVICE_DISCONNECTED
     */
    public int getConnectState(String mac) {
        return mClient.getConnectStatus(mac);
    }

    /**
     * 13.开启蓝牙设备监听服务
     */
    public void startServices(Context context) {
        Intent service = new Intent(context, Service_Alarm.class);
        context.startService(service);
    }
}
