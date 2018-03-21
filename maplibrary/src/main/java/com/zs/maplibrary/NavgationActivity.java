package com.zs.maplibrary;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZS- Chen
 * @data 2017/11/30
 * @详情： 导航界面
 */

public class NavgationActivity extends AppCompatActivity implements AMapNaviListener {
    private AMapNaviView mAMapNaviView;
    private AMapNavi mAMapNavi;
    private List<NaviLatLng> sList;
    private List<NaviLatLng> eList;
    private int initWay;
    private String TAG = getClass().getSimpleName();
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            Log.d(TAG, "onSpeakBegin() ======== ");
        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {
            Log.d(TAG, "onBufferProgress() ======== ");
        }

        @Override
        public void onSpeakPaused() {
            Log.d(TAG, "onSpeakPaused() ======== ");
        }

        @Override
        public void onSpeakResumed() {
            Log.d(TAG, "onSpeakResumed() ======== ");
        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {
            Log.d(TAG, "onSpeakProgress() ======== ");
        }

        @Override
        public void onCompleted(SpeechError speechError) {
            Log.d(TAG, "onCompleted() ======== ");
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {
            Log.d(TAG, "onEvent() ======== ");
        }
    };
    private SpeechSynthesizer mTts;
    private NaviLatLng startLatlng;
    private NaviLatLng endLatlng;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navgation);
        getSupportActionBar().hide();
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=" + "5a1f7d7b");
        startLatlng = getIntent().getParcelableExtra("startLatlng");
        endLatlng = getIntent().getParcelableExtra("endLatlng");

        if (startLatlng == null || endLatlng == null) {
            Log.e(TAG, "传过来的坐标为null。");
            return;
        }
        initWay = getIntent().getIntExtra("way", 0);
        sList = new ArrayList<>();
        eList = new ArrayList<>();

        sList.add(startLatlng);
        eList.add(endLatlng);

        //获取 AMapNaviView 实例
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        //获取AMapNavi实例
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNaviView.setAMapNaviViewListener(new AMapNaviViewListener() {
            @Override
            public void onNaviSetting() {
            }

            /**
             * 取消导航调用
             */
            @Override
            public void onNaviCancel() {
                NavgationActivity.this.finish();
            }

            /**
             * 点击左下角X标记时候调用
             *
             * @return
             */
            @Override
            public boolean onNaviBackClick() {
                return false;
            }

            @Override
            public void onNaviMapMode(int i) {
            }

            @Override
            public void onNaviTurnClick() {
            }

            @Override
            public void onNextRoadClick() {
            }

            /**
             * 全景
             */
            @Override
            public void onScanViewButtonClick() {
            }

            /**
             * 是否锁定导航的视角
             */
            @Override
            public void onLockMap(boolean b) {
                Log.d(TAG, "onLockMap() b =" + b);
            }

            /**
             * 进入导航界面时候调用
             */
            @Override
            public void onNaviViewLoaded() {
            }
        });

        mAMapNavi.addAMapNaviListener(this);
        mTts = SpeechSynthesizer.createSynthesizer(this, null);
        if (mTts != null) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "60");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "100");
        } else {
            Log.e(TAG, "语音合成对象为null ！！！");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNaviView.onDestroy();
        mAMapNavi.destroy();
        if (mTts != null) {
            mTts.stopSpeaking();
        }
    }

    @Override
    public void onInitNaviFailure() {
        Log.d(TAG, "onInitNaviFailure()");
    }

    /**
     * 方法:
     * int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute);
     * 参数:
     *
     * @congestion 躲避拥堵
     * @avoidhightspeed 不走高速
     * @cost 避免收费
     * @hightspeed 高速优先
     * @multipleroute 多路径
     * <p>
     * 说明:
     * 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
     * 注意:
     * 不走高速与高速优先不能同时为true
     * 高速优先与避免收费不能同时为true
     */
    @Override
    public void onInitNaviSuccess() {
        switch (initWay) {
            case 1:
                driveRote();
                break;
            case 2:
                walk();
                break;
            case 3:
                ride();
                break;
            default:
                break;
        }
    }

    private void ride() {
        if (startLatlng != null && endLatlng != null)
            mAMapNavi.calculateRideRoute(startLatlng, endLatlng);
    }

    private void walk() {
        if (startLatlng != null && endLatlng != null)
            mAMapNavi.calculateWalkRoute(startLatlng, endLatlng);
    }

    private void driveRote() {
        int strategy = 0;
        try {
            strategy = mAMapNavi.strategyConvert(true, false, false, false, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.calculateDriveRoute(sList, eList, null, strategy);
    }

    @Override
    public void onStartNavi(int i) {
//        Log.d(TAG, "onStartNavi() i = " + i);
    }

    @Override
    public void onTrafficStatusUpdate() {
//        Log.d(TAG, "onTrafficStatusUpdate()");
    }

    /**
     * 驾车导航的时候会调用
     * <p>
     * 当前位置变化 getCoord() ：坐标
     * getSpeed() :速度
     *
     * @param aMapNaviLocation
     */
    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
        Log.d(TAG, "onLocationChange()" + aMapNaviLocation.getSpeed() + "::" + aMapNaviLocation.getAltitude() + "::"
                + aMapNaviLocation.getAccuracy() + "::" + aMapNaviLocation.getBearing() + "::" + aMapNaviLocation.getCoord() + "::"
                + aMapNaviLocation.getTime());
    }

    /**
     * 导航提示信息 ： i= 1  ， s = 准备出发,全程二十七点五公里,大约需要三十六分钟
     * 讯飞
     * TYPE_CLOUD : 在线
     * TYPE_LOCAL ：离线
     *
     * @param i
     * @param s
     */
    @Override
    public void onGetNavigationText(final int i, final String s) {
        Log.d(TAG, "onGetNavigationText()  i= " + i + "|| s = " + s);
        //设置本地合成发音人voicer为空，默认通过语记界面指定发音人。
        if (mTts != null) {
            mTts.startSpeaking(s, mSynListener);
        } else {
            Log.d(TAG, "TTS = null ");
        }
    }

    @Override
    public void onGetNavigationText(String s) {
//        Log.d(TAG, "onGetNavigationText() ");
    }

    /**
     * 模拟导航结束
     */
    @Override
    public void onEndEmulatorNavi() {
//        Log.d(TAG, "onEndEmulatorNavi() ");
    }

    @Override
    public void onArriveDestination() {
//        Log.d(TAG, "onArriveDestination() ");
    }

    @Override
    public void onCalculateRouteFailure(int i) {
//        Log.d(TAG, "onCalculateRouteFailure() ");
    }

    @Override
    public void onReCalculateRouteForYaw() {
//        Log.d(TAG, "onReCalculateRouteForYaw() ");
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
//        Log.d(TAG, "onReCalculateRouteForTrafficJam() ");
    }

    @Override
    public void onArrivedWayPoint(int i) {
//        Log.d(TAG, "onArrivedWayPoint() ");
    }

    @Override
    public void onGpsOpenStatus(boolean b) {
//        Log.d(TAG, "onGpsOpenStatus() ");
    }

    /**
     * 实时位置信息 getCurrentRoadName ：当前道路名字 。
     * getNextRoadName ： 下个道路名字
     *
     * @param naviInfo
     */
    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
//        Log.d(TAG, "onNaviInfoUpdate() " + naviInfo.getCurrentRoadName() + "::" + naviInfo.getNextRoadName());
    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {
//        Log.d(TAG, "onNaviInfoUpdated() " + aMapNaviInfo.getPathRemainTime() + "::" + aMapNaviInfo.getPathRemainDistance());
    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {
//        Log.d(TAG, "updateCameraInfo() ");
    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {
//        Log.d(TAG, "onServiceAreaUpdate() ");
    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
//        Log.d(TAG, "showCross() ");
    }

    @Override
    public void hideCross() {
//        Log.d(TAG, "hideCross() ");
    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {
//        Log.d(TAG, "showLaneInfo() ");
    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {
//        Log.d(TAG, "hideLaneInfo() ");
    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        mAMapNavi.setEmulatorNaviSpeed(45);
        mAMapNavi.startNavi(NaviType.EMULATOR);
//        mAMapNavi.startNavi(NaviType.GPS);
    }

    @Override
    public void notifyParallelRoad(int i) {
//        Log.d(TAG, "notifyParallelRoad() ");
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
//        Log.d(TAG, "OnUpdateTrafficFacility() ");
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
//        Log.d(TAG, "OnUpdateTrafficFacility() ");
    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
//        Log.d(TAG, "OnUpdateTrafficFacility() ");
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
//        Log.d(TAG, "updateAimlessModeStatistics() ");
    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
//        Log.d(TAG, "updateAimlessModeCongestionInfo() ");
    }

    @Override
    public void onPlayRing(int i) {
//        Log.d(TAG, "onPlayRing() ");
    }
}
