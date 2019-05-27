package com.marksixinfo.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.marksixinfo.R;


public class ToastShow {
    private static Toast toast = null;
    private static Toast newToast = null;//toast(new出来的)

    public static void toastShow(Context context, String text) {
        if (context == null||TextUtils.isEmpty(text))
            return;
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }

    public static void toastShow(Context context, int id) {
        if (context == null)
            return;
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), id, Toast.LENGTH_SHORT);
        } else {
            toast.setText(id);
        }
        toast.show();
    }



    /**
     * 忘记交易密码操作提示自定义toast，图片居中，文字位于图片底部
     *
     * @param context
     * @param text
     * @param resource
     */
    public static void toastShow(Context context, String text, int resource) {
        if (context == null||TextUtils.isEmpty(text))
            return;
        Context applicationContext=context.getApplicationContext();
        View view = View.inflate(applicationContext, R.layout.toast_diy, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_view);
        imageView.setVisibility(View.VISIBLE);
        TextView textView = (TextView) view.findViewById(R.id.tv_view);
        if (newToast == null) {
            newToast = new Toast(applicationContext);
        }
        imageView.setImageResource(resource);
        textView.setText(text);
        newToast.setGravity(Gravity.CENTER, 0, 0);
        newToast.setView(view);
        newToast.show();
    }

    /**
     * 自定义toast，图片居中，文字位于图片底部 ,设置是否显示图片
     *
     * @param context
     * @param text
     * @param resource
     * @param isVisible 是否显示图片
     */
    public static void toastShow(Context context, String text, int resource, boolean isVisible) {
        if (context == null||TextUtils.isEmpty(text))
            return;
        Context applicationContext=context.getApplicationContext();
        View view = View.inflate(applicationContext, R.layout.toast_diy, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_view);
        TextView textView = (TextView) view.findViewById(R.id.tv_view);
        if (newToast == null) {
            newToast = new Toast(applicationContext);
        }
        imageView.setImageResource(resource);
        textView.setText(text);
        newToast.setGravity(Gravity.CENTER, 0, 0);
        newToast.setView(view);
        newToast.show();
        imageView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}
