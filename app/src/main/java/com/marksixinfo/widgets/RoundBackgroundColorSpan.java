package com.marksixinfo.widgets;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

import com.marksixinfo.bean.ClientInfo;
import com.marksixinfo.utils.UIUtils;

import androidx.annotation.NonNull;

/**
 * @Auther: Administrator
 * @Date: 2019/4/22 0022 22:53
 * @Description:
 */
public class RoundBackgroundColorSpan extends ReplacementSpan {
    private int mRadius;
    private int bgColor;
    private int textColor;
    private int mSize;

    public RoundBackgroundColorSpan(int bgColor,
                                    int textColor,
                                    int mSize,
                                    int radius) {
        super();
        this.bgColor = bgColor;
        this.textColor = textColor;
        this.mRadius = radius;
        this.mSize = mSize;
    }

    /**
     * @param start 第一个字符的下标
     * @param end   最后一个字符的下标
     * @return span的宽度
     */
    @Override
    public int getSize(@NonNull Paint paint,
                       CharSequence text,
                       int start,
                       int end,
                       Paint.FontMetricsInt fm) {
        return (int) (paint.measureText(text, start, end));
    }

    /**
     * @param y baseline
     */
    @Override
    public void draw(@NonNull Canvas canvas,
                     CharSequence text,
                     int start,
                     int end,
                     float x,
                     int top,
                     int y, int bottom,
                     @NonNull Paint paint) {
        int defaultColor = paint.getColor();//保存文字颜色
        float defaultStrokeWidth = paint.getStrokeWidth();

        //绘制圆角矩形
        paint.setColor(bgColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(UIUtils.dip2px(ClientInfo.scale, 1));
        paint.setTextSize(mSize);
        paint.setAntiAlias(true);
        RectF rectF = new RectF(x, y + paint.ascent() - UIUtils.dip2px(ClientInfo.scale, 2f),
                x + paint.measureText(text, start, end)
                        + UIUtils.dip2px(ClientInfo.scale, 4f), y + paint.descent());
        //设置文字背景矩形，x为span其实左上角相对整个TextView的x值，y为span左上角相对整个View的y值。
        // paint.ascent()获得文字上边缘，paint.descent()获得文字下边缘
        //x+2.5f解决线条粗细不一致问题
        canvas.drawRoundRect(rectF, mRadius, mRadius, paint);

        //绘制文字
        paint.setColor(textColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(defaultStrokeWidth);
        canvas.drawText(text, start, end, x + UIUtils.dip2px(ClientInfo.scale, 2f)
                , y - UIUtils.dip2px(ClientInfo.scale, 1f), paint);//此处mRadius为文字右移距离

        paint.setColor(defaultColor);//恢复画笔的文字颜色
    }
}
