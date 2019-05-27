package com.marksixinfo.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import static com.scwang.smartrefresh.layout.util.DensityUtil.dp2px;

/**
 * 重写刷新layout定制功能
 *
 * @Auther: Administrator
 * @Date: 2019/4/11 0011 13:27
 * @Description:
 */
public class MySmartRefreshLayout extends SmartRefreshLayout {

    public MySmartRefreshLayout(Context context) {
        super(context);
    }

    public MySmartRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySmartRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Display refresh animation and trigger refresh event.
     * 显示刷新动画并且触发刷新事件
     *
     * @return true or false, Status non-compliance will fail.
     * 是否成功（状态不符合会失败）
     * <p>
     * 自动下拉距离为head,不回弹
     */
    @Override
    public boolean autoRefresh() {
        return autoRefresh(mHandler == null ? 400 : 0, mReboundDuration, 1, false);
    }

    /**
     * finish load more.
     * 完成加载/修改延时保证有300毫秒
     *
     * @return RefreshLayout
     */
    @Override
    public SmartRefreshLayout finishLoadMore() {
        return finishLoadMore(300);//保证加载动画有300毫秒的时间
    }


    /**
     * 是否已加载全部
     *
     * @return
     */
    public boolean isFooterNoMoreData() {
        return mFooterNoMoreData;
    }

    /**
     * 移动视图到指定位置
     * moveSpinner 的取名来自 谷歌官方的 {link androidx.core.widget.SwipeRefreshLayout}
     * @param spinner 位置 (px)
     * @return RefreshKernel
     */
    public void moveSpinner(int spinner){
        mKernel.moveSpinner(dp2px(spinner), true);
    }
}
