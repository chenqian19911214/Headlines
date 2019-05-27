package com.marksixinfo.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * @Auther: Administrator
 * @Date: 2019/5/8 0008 15:14
 * @Description:
 */
public class AndroidBottomSoftBar {


    private View mViewObserved;//被监听的视图
    public static int usableHeightPrevious;//视图变化前的可用高度

    private AndroidBottomSoftBar(View viewObserving, final Activity activity) {
        mViewObserved = viewObserving;
        //给View添加全局的布局监听器
        mViewObserved.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                resetLayoutByUsableHeight(activity);
            }
        });
    }


    /**
     * 关联要监听的视图
     */
    public static void assistActivity(Activity activity) {
        new AndroidBottomSoftBar(activity.findViewById(android.R.id.content), activity);
    }

    private void resetLayoutByUsableHeight(Activity activity) {

        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;

        //比较布局变化前后的View的可用高度
        if (height != usableHeightPrevious) {
            //如果两次高度不一致
            //将当前的View的可用高度设置成View的实际高度
            mViewObserved.setPadding(0, UIUtils.getStatusBarHeight(activity), 0, getHasVirtualKey(activity));
            mViewObserved.requestLayout();//请求重新布局
            usableHeightPrevious = height;
        }
    }


    /**
     * dpi 通过反射，获取包含虚拟键的整体屏幕高度
     * height 获取屏幕尺寸，但是不包括虚拟功能高度
     *
     * @return
     */
    private static int getHasVirtualKey(Activity activity) {
        int dpi = 0;
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            dpi = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        return dpi - height;
    }
}
