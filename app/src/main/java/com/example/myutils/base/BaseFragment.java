package com.example.myutils.base;


import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myutils.utils.systemUtils.ToastUtils;
import com.example.myutils.utils.Tools;
import com.example.myutils.utils.loadingDialog.LoadingDialog;
import com.example.myutils.utils.sql.SharepreferencesUtils;
import com.example.myutils.utils.sql.sqlite.ProjectSQLite;

import java.util.ArrayList;
import java.util.List;

/**
 * 项 目 名: MyUtils
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/2 15:53
 * 本类描述:
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    /**
     * 获取layout文件
     *
     * @return 返回R.layout.--
     */
    public abstract int getContentLayoutId();

    /**
     * 初始化参数
     *
     * @param savedInstanceState onCreate中所必须的参数
     */
    public abstract void init(Bundle savedInstanceState);

    /**
     * 初始化控件
     */
    public abstract void initView(View view);

    /**
     * 设置监听事件
     */
    public abstract void initListener();

    /**
     * View的点击事件
     *
     * @param v 需要单击事件的控件
     */
    public abstract void widgetClick(View v);

    @Override
    public void onClick(View v) {
        if (Tools.fastClick())
            widgetClick(v);
    }

    /**
     * 业务操作
     *
     * @param context 上下文对象
     */
    public abstract void doWork(Context context);

    /**
     * 绑定控件
     *
     * @param resId 控件Id
     * @param <T>   泛型
     * @return
     */
    protected <T extends View> T $(View view, int resId) {
        return (T) view.findViewById(resId);
    }

    protected SQLiteDatabase sqLiteDatabase;
    protected SharepreferencesUtils sharepreferences;
    protected LoadingDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //sqlite
        ProjectSQLite sqlite = new ProjectSQLite(ProjectSQLite.SQLNAME, ProjectSQLite.SQLVERSION);
        sqLiteDatabase = sqlite.getReadableDatabase();
        //sharepreferences
        sharepreferences = SharepreferencesUtils.getInstance();
        //loadingDialog
        loadingDialog = new LoadingDialog(getActivity());
        //布局
        View view = inflater.inflate(getContentLayoutId(), container, false);

        //初始方法
        init(savedInstanceState);
        initView(view);
        initListener();
        doWork(getActivity());
        return view;
    }

    protected void showLoadingDialog() {
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.show();
        }
    }

    protected void cancelLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    protected void toast(String msg) {
        ToastUtils.show(msg);
    }

    protected void toast(int i) {
        ToastUtils.show(i);
    }

    /**
     * 动态获取权限
     * requestPermission(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissonListener() {
     *
     * @Override public void onGranted() {
     * toast("获取成功");
     * L.out("获取成功");
     * }
     * @Override public void onFature(List<String> permissonList) {
     * for (int i =0;i<permissonList.size();i++) {
     * toast("获取失败--->"+permissonList.get(i));
     * L.e("获取失败--->"+permissonList.get(i));
     * }
     * }
     * });
     */
    private PermissonListener mPermissonListener;

    protected void requestPermission(String[] ss, PermissonListener permissonListener) {
        mPermissonListener = permissonListener;
        List<String> permissonList = new ArrayList<String>();
        for (int i = 0; i < ss.length; i++) {
            if (ContextCompat.checkSelfPermission(getActivity(), ss[i]) != PackageManager.PERMISSION_GRANTED) {
                permissonList.add(ss[i]);
            }
        }

        if (permissonList.size() > 0) {
            requestPermissions(permissonList.toArray(new String[permissonList.size()]), PermissonListener.PERMISSION);
        } else {
            if (mPermissonListener != null) mPermissonListener.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissonListener.PERMISSION) {
            StringBuilder stringBuilder = new StringBuilder();//被拒绝的权限
            boolean permissionB = true;  // 是否给予权限 true有  |   false没
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    permissionB = false;
                    stringBuilder.append(permissions[i]).append("，");
                }
            }

            if (permissionB) {
                if (mPermissonListener != null) mPermissonListener.onGranted();
            } else {
                if (mPermissonListener != null) mPermissonListener.onFature(stringBuilder.toString());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharepreferences.commit();
    }
}
