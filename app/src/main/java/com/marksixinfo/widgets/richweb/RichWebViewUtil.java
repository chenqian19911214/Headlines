package com.marksixinfo.widgets.richweb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.marksixinfo.base.MyWebViewClient;
import com.marksixinfo.bean.ClientInfo;
import com.marksixinfo.evenbus.LoginSuccessEvent;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.interfaces.OnItemClickListener;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.SPUtil;

/**
 * @Auther: Administrator
 * @Date: 2019/3/31 0031 12:46
 * @Description:
 */
public class RichWebViewUtil {


    ActivityIntentInterface context;

    public RichWebViewUtil(ActivityIntentInterface context) {
        this.context = context;
    }

    public void setWebDestroy(WebView webView) {
        if (webView != null) {
            webView.loadUrl("");
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearCache(true);
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void setWebSetting(WebView webView, SucceedCallBackListener listener) {

        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("UTF-8");
        settings.setSupportZoom(false);
        settings.setDisplayZoomControls(false);
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setBlockNetworkImage(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setAppCacheEnabled(false);
        settings.setSaveFormData(false);
        // 自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new MyWebViewClient(context, listener));
        webView.setWebChromeClient(new WebChromeClient());
    }

    /**
     * 添加详情页点击图片查看js
     *
     * @param webView
     * @param listener
     */
    @SuppressLint("JavascriptInterface")
    public void addDetailJavaScript(WebView webView, OnItemClickListener listener) {
        webView.addJavascriptInterface(new DetailInterface(listener), "detailInterface");
    }

    /**
     * 添加论坛登录相操作
     *
     * @param webView
     * @param context
     */
    @SuppressLint("JavascriptInterface")
    public void addLoginJavaScript(WebView webView, Context context) {
        webView.addJavascriptInterface(new LoginInterface(context), "loginInterface");
    }

    class LoginInterface {

        Context context;

        public LoginInterface(Context context) {
            this.context = context;
        }

        /**
         * 论坛页面点击登录js回传  window.loginInterface.login(id,safety);
         *
         * @param userId
         * @param safety
         */
        @JavascriptInterface
        public void login(String userId, String safety) {
            SPUtil.setToken(context, safety);
            SPUtil.setUserId(context, userId);
            ClientInfo.initHeadParams(context);
            EventBusUtil.post(new LoginSuccessEvent());
        }
    }


    class DetailInterface {

        OnItemClickListener listener;

        public DetailInterface(OnItemClickListener listener) {
            this.listener = listener;
        }

        /**
         * webview点击  js回传获取点击index  window.detailInterface.clickPhoto(pos);
         *
         * @param index
         */
        @JavascriptInterface
        public void clickPhoto(int index) {
            listener.onItemClick(null, index);
        }

    }

}
