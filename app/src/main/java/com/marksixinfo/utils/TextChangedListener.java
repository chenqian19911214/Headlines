package com.marksixinfo.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @Auther: Administrator
 * @Date: 2019/5/7 0007 13:48
 * @Description:
 */
public class TextChangedListener {

    public static TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                for (int i = 0; i < s.length(); i++) {
                    char c = s.charAt(i);
                    if (c >= 0x4e00 && c <= 0X9fff) {
                        s.delete(i, i + 1);
                    }
                }
            }
        }
    };

    // 限制输入框不能输入汉字
    public static void StringWatcher(final EditText editText) {
        editText.removeTextChangedListener(textWatcher);
        editText.addTextChangedListener(textWatcher);
    }
}
