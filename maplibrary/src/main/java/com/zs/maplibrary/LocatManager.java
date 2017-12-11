package com.zs.maplibrary;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.Pair;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.utils.SpatialRelationUtil;
import com.amap.api.maps.utils.overlay.SmoothMoveMarker;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import java.util.List;

/**
 * @author ZS- Chen
 * @data 2017/11/28
 * @详情：高德地图相关工具类
 */

public class LocatManager {
    /**
     * 内容 ：
     * 获取对象
     * 定位数据回调接口
     * poi回调接口
     * 开启定位
     * 在地图上画一条线
     * 在地图上画一个圆
     *
     * 点平滑移动:   开始移动
     *              停止移动
     *              重新移动
     * 坐标转地址
     * 地址转坐标
     * 销毁对象
     *
     */
    private Context mContext;
    private String TAG = getClass().getSimpleName();

    public LocatManager(Context context) {
        this.mContext = context;
    }

    /**
     * 单例模式获取对象，请务必在onDestory()方法中销毁对象。
     */
    private static LocatManager locatManager;

    public static LocatManager getInstance(Context context) {
        if (locatManager == null) {
            synchronized (LocatManager.class) {
                if (locatManager == null) {
                    locatManager = new LocatManager(context);
                }
            }
        }
        return locatManager;
    }

    /**
     * 点平滑移动对象
     */
    private SmoothMoveMarker smoothMarker;
    /**
     * poi对象
     */
    private GeocodeSearch geocoderSearch;

    /**
     * 定位数据回调接口
     */
    public interface OnLocationListener {
        void onLocationSuccessListener(AMapLocation location);
        void onLocationErrorListener(AMapLocation location);
    }

    /**
     * poi回调接口
     */
    public interface OnPoiCallbackListener {
        void onLatlng2AddressSuccess(RegeocodeResult geocodeResult);

        void onAddress2LatlngSuccess(GeocodeResult regeocodeResult);
    }

    /**
     * 开启定位
     *
     * @param mLocationClient
     * @param mLocationOption 定位参数 可以为null
     * @param isOneLocate     是否一次定位 true：是    false : 否
     * @param interval        连续定位时时间间隔（isOneLocate = false ）
     * @param listener        定位信息的回调
     */
    public void startLocate(AMapLocationClient mLocationClient, AMapLocationClientOption mLocationOption, boolean isOneLocate, long interval, final OnLocationListener listener) {
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(mContext);
        }
        if (mLocationOption == null) {
            //设置定位参数AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。AMapLocationMode.Battery_Saving，低功耗模式  。AMapLocationMode.Device_Sensors，仅设备模式。
            mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
            //获取一次定位结果：
            mLocationOption.setOnceLocation(isOneLocate);
            if (!isOneLocate) {
                //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
                mLocationOption.setInterval(interval);
            }
            //设置是否返回地址信息（默认返回地址信息）
            mLocationOption.setNeedAddress(true);
            //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setHttpTimeOut(20000);
        }
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    listener.onLocationSuccessListener(aMapLocation);
                    Log.d("110", "定位结果：" + aMapLocation.getLatitude() + "," + aMapLocation.getLongitude());
                } else {
                    listener.onLocationErrorListener(aMapLocation);
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        });
        mLocationClient.startLocation();
    }

    public void startLocate(boolean isOneLocate, long interval, final OnLocationListener listener) {
        AMapLocationClient mLocationClient = new AMapLocationClient(mContext);;
        //设置定位参数AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。AMapLocationMode.Battery_Saving，低功耗模式  。AMapLocationMode.Device_Sensors，仅设备模式。
        mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        mLocationOption.setOnceLocation(isOneLocate);
        if (!isOneLocate) {
            //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
            mLocationOption.setInterval(interval);
        }
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置定位回调监听
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    listener.onLocationSuccessListener(aMapLocation);
                    Log.d("110", "定位结果：" + aMapLocation.getLatitude() + "," + aMapLocation.getLongitude());
                } else {
                    listener.onLocationErrorListener(aMapLocation);
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        });
        mLocationClient.startLocation();
    }

    /**
     * 在地图上画一条线
     *
     * @param aMap       AMap 对象
     * @param latLngList 坐标点集合
     * @param width      线条的大小
     * @param color      线条颜色
     */
    public void drawLine2Map(AMap aMap, List<LatLng> latLngList, int width, int color) {
        Polyline polyline = aMap.addPolyline(new PolylineOptions().addAll(latLngList).width(width).color(color));
    }
    public void drawLine2Map(AMap aMap, List<LatLng> latLngList) {
        Polyline polyline = aMap.addPolyline(new PolylineOptions().addAll(latLngList).width(5).color(R.color.blue_5CACEE));
    }

    /**
     * 在地图上画一个圆
     *
     * @param aMap         AMap 对象
     * @param latLng       圆点的坐标
     * @param radius       圆的半径
     * @param fiiColor     填充区域颜色
     * @param strokenColor 边框颜色
     * @param width        边框大小
     */
    public void drawCricle2Map(AMap aMap, LatLng latLng, double radius, int fiiColor, int strokenColor, int width) {
        Circle circle = aMap.addCircle(new CircleOptions().
                center(latLng).
                radius(radius).
                fillColor(fiiColor).
                strokeColor(strokenColor).
                strokeWidth(width));
    }
    public void drawCricle2Map(AMap aMap, LatLng latLng) {
        Circle circle = aMap.addCircle(new CircleOptions().
                center(latLng).
                radius(200).
                fillColor(R.color.blue_5CACEE).
                strokeColor(R.color.green_4EEE94).
                strokeWidth(10));
    }

    /**
     * 开始移动
     *
     * @param aMap
     * @param latLngList
     */
    public void startMove(AMap aMap, List<LatLng> latLngList) {
        if (smoothMarker == null) {
            initPointMove(aMap, latLngList);
        }
        // 开始滑动
        smoothMarker.startSmoothMove();
    }

    /**
     * 停止移动
     * <p>
     * 当在A点到B点的移动过程中调用stopMove()时，会在到达B点时才停止。
     */
    public void stopMove() {
        if (smoothMarker != null) {
            smoothMarker.stopMove();
        }
    }

    /**
     * 重新移动
     * <p>
     * 重新创建SmoothMarker对象
     *
     * @param aMap
     * @param latLngList
     */
    public void reStartMove(AMap aMap, List<LatLng> latLngList) {
        smoothMarker = null;
        startMove(aMap, latLngList);
    }

    /**
     * 初始化点平滑移动的数据。
     *
     * @param aMap
     * @param points
     */
    private void initPointMove(AMap aMap, List<LatLng> points) {
        // 获取轨迹坐标点
        LatLngBounds bounds = new LatLngBounds(points.get(0), points.get(points.size() - 2));
        aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

        smoothMarker = new SmoothMoveMarker(aMap);
        // 设置滑动的图标
        smoothMarker.setDescriptor(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));

        LatLng drivePoint = points.get(0);
        Pair<Integer, LatLng> pair = SpatialRelationUtil.calShortestDistancePoint(points, drivePoint);
        points.set(pair.first, drivePoint);
        List<LatLng> subList = points.subList(pair.first, points.size());

        // 设置滑动的轨迹左边点
        smoothMarker.setPoints(subList);
        // 设置滑动的总时间
        smoothMarker.setTotalDuration(10);
    }

    /**
     * 坐标转地址
     *
     * @param latLonPoint 坐标
     * @param range       坐标多少米范围
     * @param searchType  火系坐标系还是GPS原生坐标系  GeocodeSearch.AMAP or GeocodeSearch.GPS
     */
    public void latlng2Address(LatLonPoint latLonPoint, float range, String searchType, OnPoiCallbackListener callbackListener) {
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        initPoi(callbackListener);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, range, searchType);
        geocoderSearch.getFromLocationAsyn(query);
    }
    public void latlng2Address(LatLonPoint latLonPoint, OnPoiCallbackListener callbackListener) {
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        initPoi(callbackListener);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 50, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);
    }

    /**
     * 地址转坐标
     *
     * @param address            地址
     * @param cityCodeOrcityName 查询城市的城市编码或者中文
     */
    public void address2Latlng(String address, String cityCodeOrcityName, OnPoiCallbackListener callbackListener) {
        initPoi(callbackListener);
        GeocodeQuery query = new GeocodeQuery(address, cityCodeOrcityName);
        geocoderSearch.getFromLocationNameAsyn(query);
    }

    /**
     * 初始化
     */
    private void initPoi(final OnPoiCallbackListener listener) {
        if (geocoderSearch == null) {
            geocoderSearch = new GeocodeSearch(mContext);
        }
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                if (i == 1000) {
                    Log.d(TAG, "onRegeocodeSearched is success ! this code is " + i);
                    listener.onLatlng2AddressSuccess(regeocodeResult);
                } else {
                    Log.d(TAG, "onRegeocodeSearched is error ! this code is " + i);
                }
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                if (i == 1000) {
                    Log.d(TAG, "onGeocodeSearched is success ! this code is " + i);
                    listener.onAddress2LatlngSuccess(geocodeResult);
                } else {
                    Log.d(TAG, "onGeocodeSearched is error ! this code is " + i);
                }
            }
        });
    }

    /**
     * 请在Activity的onDestory()方法中调用此方法
     * 销毁对象，防止内存泄漏
     */
    public void onDestory() {
        locatManager = null;
    }
}
