package com.example.myutils.utils.systemUtils.editText;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.myutils.R;

/**
 * 项 目 名: MyTest
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/12/9 14:53
 * 本类描述: 密码输入框
 * 1.当输入框获取到焦点的时候，（是否显示密码）图片显示，没有焦点自动隐藏
 */

public class PassWordEditText extends EditText implements View.OnTouchListener, View.OnFocusChangeListener {


    public PassWordEditText(Context context) {
        super(context);
        init();
    }

    public PassWordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PassWordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnFocusChangeListener(this);
        setOnTouchListener(this);
    }

    /**
     * 焦点的监听
     */
    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            if (getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.watch_false), null);
            } else {
                setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.watch_true), null);
            }
        } else {
            setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        }
    }


    /**
     * 图片的点击事件
     */
    private int len = 0;
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Drawable drawableRight = getCompoundDrawables()[2];
        if (drawableRight == null || motionEvent.getAction() != MotionEvent.ACTION_UP) {
            return false;
        }
        if (motionEvent.getX() > getWidth() - getPaddingRight() - drawableRight.getIntrinsicWidth()) {
            len = getText().toString().length();
            if (getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.watch_true), null);
            } else {
                setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.watch_false), null);
                setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            setSelection(len);
        }
        return false;
    }

}
