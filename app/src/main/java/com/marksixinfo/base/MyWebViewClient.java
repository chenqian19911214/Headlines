package com.marksixinfo.base;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.utils.LogUtils;

/**
 * @Auther: Administrator
 * @Date: 2019/3/24 0024 22:52
 * @Description:
 */
public class MyWebViewClient extends WebViewClient {


    SucceedCallBackListener listener;
    ActivityIntentInterface context;

    public MyWebViewClient(ActivityIntentInterface context, SucceedCallBackListener listener) {
        this.listener = listener;
        this.context = context;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        //   super.onReceivedSslError(view, handler, error);
        handler.proceed();
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);

    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        view.getSettings().setBlockNetworkImage(false);
        if (listener != null && view.getProgress() == 100
                && !"about:blank".equalsIgnoreCase(url)) {
            listener.succeedCallBack(null);
        }
//                view.loadUrl("javascript:window.HtmlOut.getHeight(document.body.offsetHeight,document.body.offsetWidth);");
    }

    @Override
    public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
//        view.loadUrl(url);
        LogUtils.d("UrlLoading=" + url);
        if (null == url) {
            return false;
        }
        //拦截url跳转默认浏览器
        IntentUtils.gotoDefaultWeb(context, url);
        return true;
    }
}
