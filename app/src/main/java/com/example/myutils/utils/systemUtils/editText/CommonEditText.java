package com.example.myutils.utils.systemUtils.editText;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.myutils.R;

/**
 * 项 目 名: MyTest
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/12/9 18:02
 * 本类描述: 普通的删除输入框
 * 1.需要满足两点删除图片会显示（a.有焦点；b.length大于0）
 */

public class CommonEditText extends EditText implements View.OnTouchListener,View.OnFocusChangeListener,TextWatcher {
    public CommonEditText(Context context) {
        super(context);
        init();
    }

    public CommonEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CommonEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
        setOnTouchListener(this);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        showEditTextDra(charSequence.length());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            showEditTextDra(getText().toString().length());
        } else {
            setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Drawable drawableRight = getCompoundDrawables()[2];
        if (drawableRight == null || motionEvent.getAction() != MotionEvent.ACTION_UP) {
            return false;
        }
        if (motionEvent.getX() > getWidth() - getPaddingRight() - drawableRight.getIntrinsicWidth()) {
            setText("");
        }
        return false;
    }

    private void showEditTextDra(int length) {
        if (length > 0)
            setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.select_clear_edittext), null);
        else
            setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
    }
}
