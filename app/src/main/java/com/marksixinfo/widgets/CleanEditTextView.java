package com.marksixinfo.widgets;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.marksixinfo.R;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.UIUtils;

/**
 * Created by zxf on 2017/4/24 16:31
 */
public class CleanEditTextView extends EditText {

    private Drawable guanbiDrawable;
    private int padding;

    public CleanEditTextView(Context context) {
        super(context);
        init(context);
    }

    public CleanEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CleanEditTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        guanbiDrawable = context.getResources().getDrawable(R.drawable.actionbar_search_reset);
        padding = UIUtils.dip2px(context, 10);

        addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });

        setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setDrawable();
                } else {
                    isFocus = false;
                    setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
            }
        });
        setDrawable();
    }


    //设置删除图片
    private void setDrawable() {
        isFocus = true;
        if (length() > 0) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, guanbiDrawable, null);
            setCompoundDrawablePadding(padding);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    int Dx = 0;
    int Dy = 0;
    Rect rect1 = new Rect();
    boolean isFocus;

    // 处理删除事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Dx = (int) event.getRawX();
            Dy = (int) event.getRawY();
            getGlobalVisibleRect(rect1);
            rect1.left = rect1.right - 80 - guanbiDrawable.getIntrinsicWidth();
        }

        if (guanbiDrawable != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);

            rect.left = rect.right - 80 - guanbiDrawable.getIntrinsicWidth();

            if (rect.contains(eventX, eventY) && rect1.contains(Dx, Dy) && isFocus) {
                if (clickCleanListener != null && CommonUtils.StringNotNull(getText().toString().trim())) {
                    clickCleanListener.onClickClose();
                }
                setText("");
            }
        }
        return super.onTouchEvent(event);
    }

    private onClickCleanListener clickCleanListener;


    public interface onClickCleanListener {
        void onClickClose();
    }

    public void setClickCleanListener(onClickCleanListener clickCleanListener) {
        this.clickCleanListener = clickCleanListener;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    /**
     * 获取输入文本
     *
     * @return
     */
    private String getEditText() {
        return getText().toString().trim();
    }

    /**
     * 设置输入文本
     *
     * @param text
     */
    public void setEditText(CharSequence text) {
        setText(text);
        setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    /**
     * 设置输入文本
     *
     * @param text
     */
    public void setEditText(CharSequence text, boolean isVisible) {
        setText(text);
        if (isVisible) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, guanbiDrawable, null);
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    /**
     * 设置图片和textview间距
     *
     * @param padding
     */
    private void setPadding(int padding) {
        this.padding = padding;
        requestLayout();
    }
}
