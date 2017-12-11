package com.example.myutils.utils.systemUtils.editText;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.widget.EditText;

/**
 * 项 目 名: MyUtils
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/12/8 17:52
 * 本类描述: 输入金钱的时候可以使用这个类EditText
 * editText.addTextChangedListener(new MoneyTextWatcher(editText));
 */

public class MoneyTextWatcher implements TextWatcher {
    private int len = 0;
    private EditText editText;

    public MoneyTextWatcher(EditText editText) {
        editText.setKeyListener(DigitsKeyListener.getInstance("0123456789."));
        this.editText = editText;

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
       if (charSequence.toString().contains(".")) {
            //判断在.之前不能为空，否则不显示
            try {
                String s1 = charSequence.toString().split("\\.")[0];
                if (TextUtils.isEmpty(s1)) {
                    editText.setText("");
                    return;
                }
            } catch (Exception ignored) {
                editText.setText("");
                return;
            }

            //判断只可以输入一个.
            int count = 0;
            for (int a = 0; a < charSequence.length(); a++) {
                if (charSequence.charAt(a) == '.') {
                    count++;
                    if (count >= 2) {
                        len = charSequence.length();
                        charSequence = charSequence.subSequence(0, len - 1);
                        editText.setText(charSequence);
                        editText.setSelection(charSequence.length());
                        break;
                    }
                }
            }

            //判断.后面只能有两位数，不能过多
            try {
                String s2 = charSequence.toString().split("\\.")[1];
                if (s2.length() > 2) {
                    editText.setText(charSequence.subSequence(0, len));
                    editText.setSelection(len);
                } else {
                    len = charSequence.length();
                }
            } catch (Exception ignored) {}
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
