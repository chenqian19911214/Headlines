package com.marksixinfo.base;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.marksixinfo.interfaces.ActivityIntentInterface;


/**
 * @author guangleilei
 * @version 1.0 2017-05-12
 */
public abstract class BasePopWindow extends PopupWindow {

    protected View popView;
    protected Context mContext;

    public ActivityIntentInterface getCtrl() {
        return (ActivityIntentInterface) mContext;
    }

    public BasePopWindow(Context mContext) {
        super(mContext);
        this.mContext=mContext;
        initPopView();
        initData();
    }


    protected void initPopView(){
        popView = LayoutInflater.from(mContext).inflate(getLayoutResId(), null);
        setContentView(popView);

        initPop();
        initView();
    }


    public abstract void  initView();

    public abstract void  initData();

    public abstract int  getLayoutResId();



    protected void  initPop(){
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setTouchable(true);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
    }



    @Override
    public void showAsDropDown(View anchor) {
            if (Build.VERSION.SDK_INT >= 24) {
                int[] location = new int[2];
                anchor.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];
                showAtLocation(anchor, Gravity.NO_GRAVITY, x,y+anchor.getHeight());
            }else {
                super.showAsDropDown(anchor);
            }
    }

}
