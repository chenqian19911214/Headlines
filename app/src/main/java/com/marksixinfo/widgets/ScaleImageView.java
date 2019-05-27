package com.marksixinfo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 自适应缩放
 *
 * @Auther: Administrator
 * @Date: 2019/5/8 0008 19:02
 * @Description:
 */
public class ScaleImageView extends ImageView {

    private int width;
    private int height;

    public ScaleImageView(Context context) {
        super(context);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScaleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setInitSize(int initWidth, int initHeight) {
        this.width = initWidth;
        this.height = initHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (width > 0 && height > 0) {
            int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
            float scale = maxWidth * 1.0f / width * 1.0f;
            float imageHeight = height * 1.0f * scale;
            setMeasuredDimension(maxWidth, (int) imageHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}

