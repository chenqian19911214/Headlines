package com.marksixinfo.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.marksixinfo.R;
import com.marksixinfo.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义view-刮刮乐.
 */
public class ScratchView extends View {

    private final static String TAG = ScratchView.class.getSimpleName();
    /**
     * 最小的橡皮擦尺寸大小
     */
    private final static float DEFAULT_ERASER_SIZE = 60f;
    /**
     * 默认蒙板的颜色
     */
    private final static int DEFAULT_MASKER_COLOR = 0xffcccccc;
    /**
     * 默认擦除比例
     */
    private final static int DEFAULT_PERCENT = 20;
    /**
     * 最大擦除比例
     */
    private final static int MAX_PERCENT = 100;

    /**
     * 遮罩 Paint
     */
    private Paint mMaskPaint;
    /**
     * 蒙层 背景
     */
    private int maskBgReseId = -1;
    /**
     * 产生遮罩效果的 Bitmap
     */
    private Bitmap mMaskBitmap;
    /**
     * 绘制遮罩的 Canvas
     */
    private Canvas mMaskCanvas;
    /**
     * 普通绘制 Bitmap 用的 Paint
     */
    private Paint mBitmapPaint;
    /**
     * 水印
     */
    private BitmapDrawable mWatermark;
    /**
     * 橡皮檫画笔
     */
    private Paint mErasePaint;
    /**
     * 擦除轨迹
     */
    private Path mErasePath;
    /**
     * 擦除效果起始点的x坐标
     */
    private float mStartX;
    /**
     * 擦除效果起始点的y坐标
     */
    private float mStartY;
    /**
     * 最小滑动距离
     */
    private int mTouchSlop;
    /**
     * 完成擦除
     */
    private boolean mIsCompleted = false;
    /**
     * 最大擦除比例
     */
    private int mMaxPercent = DEFAULT_PERCENT;
    /**
     * 当前擦除比例
     */
    private int mPercent = 0;
    /**
     * 存放蒙层像素信息的数组
     */
    private int mPixels[];

    private boolean canERase = true;//能够擦除

    private EraseStatusListener mEraseStatusListener;

    private List<AsyncTask<Integer, Integer, Boolean>> asyncTaskList = new ArrayList<>();

    public ScratchView(Context context) {
        this(context, null);
    }

    public ScratchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScratchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScratchView, defStyleAttr, 0);
        init(typedArray);
    }

    private void init(TypedArray typedArray) {
        int maskColor = typedArray.getColor(R.styleable.ScratchView_sv_maskColor, Color.TRANSPARENT);
        maskBgReseId = typedArray.getResourceId(R.styleable.ScratchView_sv_maskBgReseId, R.drawable.ic_scratch_mask);
        int watermarkResId = typedArray.getResourceId(R.styleable.ScratchView_sv_watermark, -1);
        float eraseSize = typedArray.getFloat(R.styleable.ScratchView_sv_eraseSize, DEFAULT_ERASER_SIZE);
        mMaxPercent = typedArray.getInt(R.styleable.ScratchView_sv_maxPercent, DEFAULT_PERCENT);
        typedArray.recycle();

        mMaskPaint = new Paint();
        mMaskPaint.setAntiAlias(true);//抗锯齿
        mMaskPaint.setDither(true);//防抖
        setMaskColor(maskColor);

        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setDither(true);

        setWatermark(watermarkResId);

        mErasePaint = new Paint();
        mErasePaint.setAntiAlias(true);
        mErasePaint.setDither(true);
        mErasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));//设置擦除效果
        mErasePaint.setStyle(Paint.Style.STROKE);
        mErasePaint.setStrokeCap(Paint.Cap.ROUND);//设置笔尖形状，让绘制的边缘圆滑
        setEraserSize(eraseSize);

        mErasePath = new Path();

        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        mTouchSlop = viewConfiguration.getScaledTouchSlop();

    }

    /**
     * 设置橡皮檫尺寸大小（默认大小是 60）
     *
     * @param eraserSize 橡皮檫尺寸大小
     */
    public void setEraserSize(float eraserSize) {
        mErasePaint.setStrokeWidth(eraserSize);
    }

    /**
     * 设置蒙板颜色
     *
     * @param color 十六进制颜色值，如：0xffff0000（不透明的红色）
     */
    public void setMaskColor(int color) {
        mMaskPaint.setColor(color);
    }

    /**
     * 设置最大的擦除比例
     *
     * @param max 大于0，小于等于100
     */
    public void setMaxPercent(int max) {
        if (max > 100 || max <= 0) {
            return;
        }
        this.mMaxPercent = max;
    }

    public boolean isCanERase() {
        return canERase;
    }

    public void setCanERase(boolean canERase) {
        this.canERase = canERase;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = measureSize(widthMeasureSpec);
        int measuredHeight = measureSize(heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mMaskBitmap != null) {
            canvas.drawBitmap(mMaskBitmap, 0, 0, mBitmapPaint);//绘制图层遮罩
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (canERase) {
                    startErase(event.getX(), event.getY());
                    invalidate();
                    return true;
                }
            case MotionEvent.ACTION_MOVE:
                if (canERase) {
                    erase(event.getX(), event.getY());
                    invalidate();
                    getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                }
            case MotionEvent.ACTION_UP:
                if (canERase) {
                    stopErase();
                    invalidate();
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return true;
                }
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createMasker(w, h);
    }

    /**
     * 创建蒙层
     *
     * @param width
     * @param height
     */
    private void createMasker(int width, int height) {
        Drawable drawable = getContext().getResources().getDrawable(maskBgReseId);
        Bitmap source = getBitmapFromResources(getContext(), maskBgReseId);

        LogUtils.d(TAG, "source.getWidth()=" + source.getWidth());
        LogUtils.d(TAG, "source.getHeight()=" + source.getHeight());
        LogUtils.d(TAG, "width=" + width);
        LogUtils.d(TAG, "height=" + height);
        // 定义矩阵对象
        Matrix matrix = new Matrix();
        float sx = ((float) width / source.getWidth());
        float sy = ((float) height / source.getHeight());
        // 缩放图像
        matrix.postScale(sx, sy);
        LogUtils.d(TAG, "sx=" + sx);
        LogUtils.d(TAG, "sy=" + sy);

        Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true).copy(Bitmap.Config.ARGB_8888, true);

//        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), maskBgReseId)
//                .copy(Bitmap.Config.ARGB_8888, true);

        mMaskBitmap = bitmap;
        mMaskCanvas = new Canvas(mMaskBitmap);
        try {
            drawable.setBounds(0, 0, width,height);
            drawable.draw(mMaskCanvas);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogUtils.d(TAG, "bitmap.getWidth()=" + bitmap.getWidth());
        LogUtils.d(TAG, "bitmap.getHeight()=" + bitmap.getHeight());

        if (mWatermark != null) {
            Rect rect = new Rect(0, 0, width, height);
            Rect bounds = new Rect(rect);
            mWatermark.setBounds(bounds);
            mWatermark.draw(mMaskCanvas);
        }

        mPixels = new int[width * height];
    }


    //从资源文件中获取Bitmap
    public static Bitmap getBitmapFromResources(Context context, int resId) {
        Resources res = context.getResources();
        return BitmapFactory.decodeResource(res, resId);
    }


    private int measureSize(int measureSpec) {
        int size = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            size = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                size = Math.min(size, specSize);
            }
        }
        return size;
    }

    /**
     * 开始擦除
     *
     * @param x
     * @param y
     */
    private void startErase(float x, float y) {
        mErasePath.reset();
        mErasePath.moveTo(x, y);
        this.mStartX = x;
        this.mStartY = y;
    }

    /**
     * 擦除
     *
     * @param x
     * @param y
     */
    private void erase(float x, float y) {
        int dx = (int) Math.abs(x - mStartX);
        int dy = (int) Math.abs(y - mStartY);
        if (dx >= mTouchSlop || dy >= mTouchSlop) {
            this.mStartX = x;
            this.mStartY = y;

            mErasePath.lineTo(x, y);
            mMaskCanvas.drawPath(mErasePath, mErasePaint);
            //updateErasePercent();

            mErasePath.reset();
            mErasePath.moveTo(mStartX, mStartY);
        }
    }

    private void updateErasePercent() {
        int width = getWidth();
        int height = getHeight();
        AsyncTask<Integer, Integer, Boolean> task = new AsyncTask<Integer, Integer, Boolean>() {

            @Override
            protected Boolean doInBackground(Integer... params) {
                if(isCancelled()){
                    return true;
                }
                if (mMaskBitmap != null) {
                    int width = params[0];
                    int height = params[1];
                    LogUtils.d(TAG, "mMaskBitmap.getWidth()=" + mMaskBitmap.getWidth());
                    LogUtils.d(TAG, "mMaskBitmap.getHeight()=" + mMaskBitmap.getHeight());
                    LogUtils.d(TAG, "width" + width);
                    LogUtils.d(TAG, "height=" + height);
                    mMaskBitmap.getPixels(mPixels, 0, width, 0, 0, width, height);//获取覆盖图层中所有的像素信息，stride用于表示一行的像素个数有多少

                    float erasePixelCount = 0;//擦除的像素个数
                    float totalPixelCount = width * height;//总像素个数

                    for (int pos = 0; pos < totalPixelCount; pos++) {
                        if (mPixels[pos] == 0) {//透明的像素值为0
                            erasePixelCount++;
                        }
                    }

                    int percent = 0;
                    if (erasePixelCount >= 0 && totalPixelCount > 0) {
                        percent = Math.round(erasePixelCount * 100 / totalPixelCount);
                        publishProgress(percent);
                    }
                    return percent >= mMaxPercent;
                }
                return true;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                mPercent = values[0];
                onPercentUpdate();
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                if (result && !mIsCompleted) {//标记擦除，并完成回调
                    mIsCompleted = true;
                    if (mEraseStatusListener != null) {
                        mEraseStatusListener.onCompleted(ScratchView.this);
                    }
                }
            }

            @Override
            protected void onCancelled(Boolean aBoolean) {
                super.onCancelled(aBoolean);
                LogUtils.d(aBoolean+"异步任务取消");
            }
        };
        task.execute(width, height);
        asyncTaskList.add(task);
    }

    public void cancleAllAsyncTask() {
        for (AsyncTask task : asyncTaskList) {
            task.cancel(true);
            LogUtils.d("异步任务取消");
        }
        asyncTaskList.clear();
    }

    /**
     * 停止擦除
     */
    private void stopErase() {
        this.mStartX = 0;
        this.mStartY = 0;
        mErasePath.reset();
        updateErasePercent();
    }

    private void onPercentUpdate() {
        if (mEraseStatusListener != null) {
            mEraseStatusListener.onProgress(mPercent);
        }
    }

    /**
     * 设置擦除监听器
     *
     * @param listener
     */
    public void setEraseStatusListener(EraseStatusListener listener) {
        this.mEraseStatusListener = listener;
    }

    /**
     * 设置水印图标
     *
     * @param resId 图标资源id，-1表示去除水印
     */
    public void setWatermark(int resId) {
        if (resId == -1) {
            mWatermark = null;
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
            mWatermark = new BitmapDrawable(bitmap);
            mWatermark.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        }
    }

    /**
     * 重置为初始状态
     */
    public void reset() {
        mIsCompleted = false;

        int width = getWidth();
        int height = getHeight();
        createMasker(width, height);
        invalidate();

        updateErasePercent();
    }

    /**
     * 清除整个图层
     */
    public void clear() {
        int width = getWidth();
        int height = getHeight();
        mMaskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mMaskCanvas = new Canvas(mMaskBitmap);
        Rect rect = new Rect(0, 0, width, height);
        mMaskCanvas.drawRect(rect, mErasePaint);
        invalidate();

        updateErasePercent();
    }


    /**
     * 擦除状态监听器
     */
    public  interface EraseStatusListener {

        /**
         * 擦除进度
         *
         * @param percent 进度值，大于0，小于等于100；
         */
         void onProgress(int percent);

        /**
         * 擦除完成回调函数
         *
         * @param view
         */
         void onCompleted(View view);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancleAllAsyncTask();
        mMaskBitmap = null;
        mWatermark = null;
    }
}
