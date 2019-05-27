package com.marksixinfo.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.widgets.ScrollWebView;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * @Auther: Administrator
 * @Date: 2019/3/19 0019 14:49
 * @Description:
 */
public class X5WebViewFragment extends MarkSixFragment {

    @Override
    public int getViewId() {
        return R.layout.fragment_x5_webview;
    }

    protected ScrollWebView mWebView;

    /**
     * 待加载的 url
     */
    private String url;

    public static final int RESULT_CODE = 200;

    /**
     * 是否是一个 html 内容的 String 优先加载
     */
    private String htmlContent = null;


    @Override
    protected void afterViews() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            url = bundle.getString(StringConstants.URL);
        }


        mWebView = findViewById(R.id.web_view);
        mWebView.setDrawingCacheEnabled(true);

        configWebView();


        if (!TextUtils.isEmpty(htmlContent)) {
            mWebView.loadDataWithBaseURL(null, htmlContent, "text/html", "utf-8", null);
        } else {
            if (isNetworkConnected(getContext())) {
                //网络正常
                mWebView.loadUrl(url);
            } else {
                mWebView.loadUrl(url);
                mWebView.setVisibility(View.VISIBLE);
            }

        }
    }


    public void loadUrl(String url){
        this.url = url;
        mWebView.loadUrl(url);
    }

    @Override
    public void onDestroyView() {
        if (mWebView != null) {
            mWebView.destroy();
        }
        super.onDestroyView();
    }

    private void configWebView() {
        setWebSetting();
        setScrollBarStyle();

        addJavascriptInterface();

        mWebView.setWebChromeClient(initWebChromeClient());
        mWebView.setWebViewClient(initWebViewClient());
        mWebView.setDownloadListener(initDownloadListener());
    }

    /**
     * 设置 {@link WebSettings}
     * 如需要修改,请继承后修改此方法
     */
    protected void setWebSetting() {
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
    protected void setScrollBarStyle() {
        setScrollBarStyle(false, false);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);// 滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
    }

    /**
     * 设置滚动条滑动模式
     *
     * @param verticalEnabled
     * @param horizontalEnabled
     */
    private void setScrollBarStyle(boolean verticalEnabled, boolean horizontalEnabled) {
        mWebView.setVerticalScrollBarEnabled(verticalEnabled);
        mWebView.setHorizontalScrollBarEnabled(horizontalEnabled);
    }
//
//    public void onEventMainThread(WebViewEvent event) {
//
//        if (event.getMove() == WebViewEvent.MOVE_TO_FRONT) {
//            if (mWebView.canGoBack()) {
//                mWebView.goBack();
//            } else {
//
//                if (webAttach != null) {
//                    webAttach.onActivityFinish(RESULT_CODE);
//                }
//            }
//        } else if (event.getMove() == WebViewEvent.RELOAD) {
//            mWebView.reload();
//        }
//
//    }

    /**
     * 设置 js 调用内容
     */
    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    protected void addJavascriptInterface() {

    }

    protected WebChromeClient initWebChromeClient() {
        return new WebChromeClient();
    }


    protected WebViewClient initWebViewClient() {
        return new WebViewClient();
    }


    protected DownloadListener initDownloadListener() {
        return new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {

            }
        };
    }

    // Webview网络状态
    protected boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;

    }

    /**
     * @param result
     * @param callbackname
     * @function 执行页面js
     */
    protected void excuteJS(String result, String callbackname) {
        final String funcstr;
        funcstr = "javascript:" + callbackname + "(" + result + ")";
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(funcstr);
            }
        });
    }


}
