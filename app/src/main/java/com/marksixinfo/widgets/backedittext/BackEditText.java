package com.marksixinfo.widgets.backedittext;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * @Auther: Administrator
 * @Date: 2019/3/28 0028 17:04
 * @Description:
 */
public class BackEditText extends EditText {
    public interface PressBackCallBack {
        void callBack();
    }

    private PressBackCallBack callBack;

    public void setCallBack(PressBackCallBack callBack) {
        this.callBack = callBack;
    }

    public BackEditText(Context context) {
        super(context);
    }

    public BackEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (callBack != null) {
                callBack.callBack();
            }
            return true;
        }
        return super.dispatchKeyEventPreIme(event);
    }
}
