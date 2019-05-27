package com.marksixinfo.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.MarkSixWhiteActivity;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.LogUtils;
import com.marksixinfo.utils.NetWorkUtils;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.richweb.RichWebView;

import androidx.fragment.app.FragmentActivity;
import butterknife.BindView;


/**
 * 功能描述:   通用webView
 *
 * @auther: Administrator
 * @date: 2019/4/16 0016 17:01
 */
public class WebViewActivity extends MarkSixWhiteActivity implements View.OnClickListener {


    TextView title;
    @BindView(R.id.web_view)
    RichWebView webView;
    @BindView(R.id.loading)
    LoadingLayout loading;
    //    @BindView(R.id.mProgressBar)
//    ProgressBar mProgressBar;
    private String url = "";
    private String mTitle = "";
    SucceedCallBackListener listener;

    @Override
    public int getViewId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void afterViews() {

        title = markSixTitleWhite.getTvTitle();

        url = getStringParam(StringConstants.URL);
        mTitle = getStringParam(StringConstants.TITLE);
        loading.setRetryListener(this);
        title.setText(CommonUtils.StringNotNull(mTitle) ? mTitle : "");

        loadUrl();
    }


    private void loadUrl() {
        if (CommonUtils.StringNotNull(url)) {
            setWebSetting();
            webView.loadUrl(url);
        } else {
            loading.showEmpty();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void setWebSetting() {

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
        webView.setWebViewClient(initWebViewClient());
        webView.setWebChromeClient(initWebChromeClient());
    }


    protected WebChromeClient initWebChromeClient() {
        return new WebChromeClient() {
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle("对话框").setMessage(message).setPositiveButton("确定", null);

                // 不需要绑定按键事件
                // 屏蔽keycode等于84之类的按键
                builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        Log.v("onJsAlert", "keyCode==" + keyCode + "event=" + event);
                        return true;
                    }
                });
                // 禁止响应按back键的事件
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                if (view.getContext() instanceof FragmentActivity) {
                    if (!(((FragmentActivity) view.getContext()).isFinishing())) {
                        dialog.show();
                    }
                }
                result.confirm();// 因为没有绑定事1件，需要强行confirm,否则页面会变黑显示不了内容。
                return true;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
//                if (newProgress == 100) {
//                    mProgressBar.setVisibility(View.GONE);
//                } else {
//                    mProgressBar.setVisibility(View.VISIBLE);
//                    mProgressBar.setProgress(newProgress);
//                }
                super.onProgressChanged(view, newProgress);
            }


            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (NetWorkUtils.isNetworkConnected(getContext())) {
                    //超时找不到网页
                    if (title != null && title.equals("找不到网页")) {
                        loading.showEmpty();
                    }
                } else {
                    //网络错误title设置空
                    WebViewActivity.this.title.setText("");
                    loading.showError();
                }

            }
        };
    }

    protected WebViewClient initWebViewClient() {
        return new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                LogUtils.d( "url=" + url);
//                if (null == url)
//                    return false;
//                if (url.startsWith("tel:") || url.startsWith("sms:") || url.startsWith("smsto:") || url.startsWith("mms:") || url.startsWith("mmsto:")) {
//                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
//                    startActivity(intent);
//                    return true;
//                } else if (url.startsWith("mailto:")) {
//                    //Handle mail Urls
//                    startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse(url)));
//                    return true;
//                }
//
//
//                LogUtils.d("HtmlUrl  shouldOverrideUrlLoading", url + "");
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                LogUtils.d("HtmlUrl  onPageStarted" + url);
//                HashMap params = getUrlParam();
//                if (params != null) {
//                    params.put(NumberConstants.URL, url);
//                }
//                Intent in = getIntent();
//                if (in != null) {
//                    in.putExtra(getString(R.string.thisUrl), IntentUtils.addParams(new StringBuffer(getThisHost()), params).toString());
//                }
//                if (isFrist)//第一次进入由父类埋页面进入事件，后面每次帮H5埋对应的页面入和出
//                {
//                    isFrist = false;
//                } else {
//                    ZallGoTracker.onPageLeave(getThisHost());
//                    ZallGoTracker.onPageEnter(getThisHost(), "", getThisHostUrl(), getLastHostUrl());
//                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

//                mProgressBar.setVisibility(View.GONE);
                LogUtils.d("HtmlUrl onPageFinished" + url + "getThisHost=" + getThisHost());
                // html加载完成之后，添加监听js函数
//                if (url.contains("xiangfubao")) {
//                    addImageClickListner();
//                }
                String title = view.getTitle();//回退后改变title
                WebViewActivity.this.title.setText(title);
                view.getSettings().setBlockNetworkImage(false);
                if (view.getProgress() == 100) {
                    loading.showContent();
                    if (listener != null && !"about:blank".equalsIgnoreCase(url)) {
                        listener.succeedCallBack(null);
                    }
                }
            }


            @Override
            public void onReceivedError(WebView view, int errorCode, String description, final String failingUrl) {
                if (getActivity() == null) {
                    return;
                }
                loading.showError();
//                if (webAttach != null) {
//                    webAttach.onReceivedError(view, errorCode, description, failingUrl);
//                }
//
//                // 网络错误
//                mWebView.setVisibility(View.GONE);
//                mNoResult.setVisibility(View.VISIBLE);
//
//                mNoResult.init(getString(R.string.show_error), R.drawable.detail_request_error, getString(R.string.reloading), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //刷新
//                        if (mWebView != null) {
//                            mWebView.loadUrl(failingUrl);
//                        }
//                    }
//                });

            }
        };
    }

    @Override
    public void onClick(View v) {
        loadUrl();
    }
}
