package com.marksixinfo.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * 索引bar
 *
 * @Auther: Administrator
 * @Date: 2019/5/9 0009 21:03
 * @Description:
 */
public class IndexBar extends View {

    private List<String> INDEX_NAME = new ArrayList<>();
    private OnLetterUpdateListener listener;
    private Paint mPaint;
    private float cellHeight, viewWidth;
    private int touchIndex = -1, selectedColor;
    private String currentKey;

    public IndexBar(Context context) {
        this(context, null);
    }

    public IndexBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setTextSize(UIUtils.dip2px(context, 9));
        mPaint.setAntiAlias(true);
        //获取文字被选中的颜色
        selectedColor = Color.WHITE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (INDEX_NAME.size() > 0) {
            for (int i = 0; i < INDEX_NAME.size(); i++) {
                String text = INDEX_NAME.get(i);
                //计算绘制字符的X方向起点
                int x = (int) (viewWidth / 2.0f - mPaint.measureText(text) / 2.0f);
                Rect bounds = new Rect();
                mPaint.getTextBounds(text, 0, text.length(), bounds);
                int textHeight = bounds.height();
                //计算绘制字符的Y方向起点
                int y = (int) (cellHeight / 2f + textHeight / 2f + i
                        * cellHeight);
                //绘制选中字符的背景
                if (touchIndex == i) {
                    mPaint.setColor(0xfffc5c66);
                    canvas.drawCircle(viewWidth / 2f, cellHeight * i + cellHeight / 2f, cellHeight / 2f, mPaint);
                    currentKey = text;
                }
                mPaint.setColor(touchIndex == i ? selectedColor : Color.WHITE);
                canvas.drawText(text, x, y, mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //计算当前触摸的字符索引
                index = (int) (event.getY() / cellHeight);
                if (INDEX_NAME.size() > 0) {
                    if (index >= 0 && index < INDEX_NAME.size()) {
                        if (index != touchIndex) {
                            touchIndex = index;
                            if (listener != null) {
                                currentKey = INDEX_NAME.get(index);
                                listener.onLetterUpdate(currentKey);
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //计算当前触摸的字符索引
                index = (int) (event.getY() / cellHeight);
                if (INDEX_NAME.size() > 0) {
                    if (index >= 0 && index < INDEX_NAME.size()) {
                        if (index != touchIndex) {
                            touchIndex = index;
                            if (listener != null) {
                                currentKey = INDEX_NAME.get(index);
                                listener.onLetterUpdate(currentKey);
                            }
                        }
                    }
                }
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //得到当前控件的宽度
        viewWidth = getMeasuredWidth();
        int mHeight = getMeasuredHeight();
        if (INDEX_NAME.size() > 0) {
            //获取单个字符能够拥有的高度
            cellHeight = mHeight * 1.0f / INDEX_NAME.size();
        }
    }

    public interface OnLetterUpdateListener {
        void onLetterUpdate(String letter);
    }

    public void setListener(OnLetterUpdateListener listener) {
        this.listener = listener;
    }

    /**
     * 匹配字母
     *
     * @param letter
     */
    public void matchingLetter(String letter, boolean isReload) {
        if (INDEX_NAME.size() > 0 && CommonUtils.StringNotNull(letter) && (isReload || !letter.equals(currentKey))) {
            for (int i = 0; i < INDEX_NAME.size(); i++) {
                if (letter.equals(INDEX_NAME.get(i))) {
                    touchIndex = i;
                    currentKey = letter;
                    invalidate();
                    break;
                }
            }
        }
    }

    /**
     * 设置索引
     *
     * @param indexName
     */
    public void setIndexName(List<String> indexName) {
        if (!INDEX_NAME.equals(indexName)) {
            INDEX_NAME = indexName;
            touchIndex = 0;
            invalidate();
        }
    }

    /**
     * 获取当前key的下一个key
     *
     * @return
     */
    public String getNextName(String currentName) {
        String key = currentName;
        if (INDEX_NAME.size() > 0) {
            try {
                key = INDEX_NAME.get(INDEX_NAME.indexOf(currentName) + 1);
            } catch (Exception e) {
                e.printStackTrace();
                key = "";
            }
        }
        return key;
    }
}
