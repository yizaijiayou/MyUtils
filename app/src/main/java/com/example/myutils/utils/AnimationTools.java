package com.example.myutils.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

/**
 * 项 目 名: MyTest
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/12/7 17:16
 * 本类描述:
 */

public class AnimationTools {
    /**
     * 透明度
     * @param target
     * @param duration
     * @param values
     */
    public static void alpha(Object target,long duration, float... values){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target,"alpha",values);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    /**
     * 旋转
     * @param target
     * @param duration
     * @param values
     */
    public static void rotation(Object target,long duration, float... values){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target,"rotation",values);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    /**
     * 围绕 X 轴旋转
     * @param target
     * @param duration
     * @param values
     */
    public static void rotationX(Object target,long duration, float... values){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target,"rotationX",values);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    /**
     * 围绕 Y 轴旋转
     * @param target
     * @param duration
     * @param values
     */
    public static void rotationY(Object target,long duration, float... values){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target,"rotationY",values);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    /**
     * 正数向右平移，负数向左平移
     * @param target
     * @param duration
     * @param values
     */
    public static void translationX(Object target,long duration, float... values){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target,"translationX",values);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    /**
     * 正数向下平移，负数向上平移
     * @param target
     * @param duration
     * @param values
     */
    public static void translationY(Object target,long duration, float... values){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target,"translationY",values);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    /**
     * X 轴缩放
     * @param target
     * @param duration
     * @param values
     */
    public static void scaleX(Object target,long duration, float... values){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target,"scaleX",values);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    /**
     * Y 轴缩放
     * @param target
     * @param duration
     * @param values
     */
    public static void scaleY(Object target,long duration, float... values){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(target,"scaleY",values);
        objectAnimator.setDuration(duration);
        objectAnimator.start();
    }

    /**
     * XY 轴缩放
     * @param target
     * @param duration
     * @param values
     */
    public static void scaleXY(Object target,long duration, float... values){
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(target,"scaleY",values);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(target,"scaleX",values);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimatorX,objectAnimatorY);
        animatorSet.setDuration(duration);
        animatorSet.start();
    }
}
