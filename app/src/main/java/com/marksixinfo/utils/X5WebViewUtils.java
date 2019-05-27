package com.marksixinfo.utils;

import android.annotation.SuppressLint;
import android.view.View;

import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * @Auther: Administrator
 * @Date: 2019/3/19 0019 15:03
 * @Description:
 */
public class X5WebViewUtils {





    public WebView setConfig(WebView mWebView) {
        setWebSetting(mWebView);
        setScrollBarStyle(mWebView);

        addJavascriptInterface(mWebView);

        mWebView.setWebChromeClient(initWebChromeClient(mWebView));
        mWebView.setWebViewClient(initWebViewClient(mWebView));
        mWebView.setDownloadListener(initDownloadListener(mWebView));

        return mWebView;
    }

    /**
     * 设置 {@link WebSettings}
     * 如需要修改,请继承后修改此方法
     */
    private void setWebSetting(WebView mWebView) {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

    }

    /**
     * 设置设置滚动条滑动模式
     * 如需要修改,请继承后修改此方法
     */
    private void setScrollBarStyle(WebView mWebView) {
        setScrollBarStyle(mWebView, false, false);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);// 滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
    }

    /**
     * 设置滚动条滑动模式
     *
     * @param verticalEnabled
     * @param horizontalEnabled
     */
    private void setScrollBarStyle(WebView mWebView, boolean verticalEnabled, boolean horizontalEnabled) {
        mWebView.setVerticalScrollBarEnabled(verticalEnabled);
        mWebView.setHorizontalScrollBarEnabled(horizontalEnabled);
    }

    /**
     * 设置 js 调用内容
     * @param mWebView
     */
    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void addJavascriptInterface(WebView mWebView) {

    }

    private WebChromeClient initWebChromeClient(WebView mWebView) {
        return new WebChromeClient();
    }


    private WebViewClient initWebViewClient(WebView mWebView) {
        return new WebViewClient();
    }


    private DownloadListener initDownloadListener(WebView mWebView) {
        return new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {

            }
        };
    }

}
