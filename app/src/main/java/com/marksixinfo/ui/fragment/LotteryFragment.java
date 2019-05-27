package com.marksixinfo.ui.fragment;

import com.marksixinfo.R;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.constants.UrlStaticConstants;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.widgets.richweb.RichWebView;
import com.marksixinfo.widgets.richweb.RichWebViewUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;

/**
 * 开奖web
 *
 * @Auther: Administrator
 * @Date: 2019/3/15 0015 12:17
 * @Description:
 */
public class LotteryFragment extends PageBaseFragment implements SucceedCallBackListener {


    @BindView(R.id.web_view)
    RichWebView webView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private RichWebViewUtil richWebViewUtil;

    @Override
    public int getViewId() {
        return R.layout.fragment_lottery;
    }

    @Override
    protected void afterViews() {
        richWebViewUtil = new RichWebViewUtil(this);
        richWebViewUtil.setWebSetting(webView, this);
        webView.loadUrl("");
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadUrl();
            }
        });
    }


    private void loadUrl() {
        webView.loadUrl(UrlStaticConstants.LOTTERY);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        richWebViewUtil.setWebDestroy(webView);
        webView = null;
        richWebViewUtil = null;
    }

    @Override
    protected void loadData() {
        refreshLayout.autoRefresh();
        //加载完禁用下拉刷新
        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setEnableRefresh(false);
            }
        }, 2000);    }

    @Override
    public void succeedCallBack(@Nullable Object o) {
        refreshLayout.finishRefresh();
    }
}
