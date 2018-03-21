package com.example.myutils.base;

import java.util.List;

/**
 * 项 目 名: MyUtils
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/29 11:26
 * 本类描述:
 */

public interface PermissonListener {
    int PERMISSION = 10000;

    void onGranted();//获取权限成功

    void onFature(String perssion);//获取权限失败
}
