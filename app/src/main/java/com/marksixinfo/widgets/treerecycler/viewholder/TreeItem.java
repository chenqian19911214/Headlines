package com.marksixinfo.widgets.treerecycler.viewholder;

import android.content.Context;
import android.content.res.Resources;

import com.marksixinfo.adapter.ViewHolder;
import com.marksixinfo.interfaces.ActivityIntentInterface;

import androidx.annotation.LayoutRes;

/**
 * 组合模式
 */
public abstract class TreeItem<D> {

    protected TreeParentItem parentItem;
    /**
     * 当前item的数据
     */
    protected D data;
    /**
     * 布局资源id
     */
    protected int layoutId;
    /**
     * 每行所占比例
     */
    protected int spanSize;

    protected Context context;

    protected ActivityIntentInterface activity;

    protected onClickChangeListener mOnClickChangeListener;

    protected TreeItemManager mTreeItemManager;


    public TreeItem(D data, ActivityIntentInterface context) {
        this(data, null, context);
    }

    public TreeItem(D data, TreeParentItem parentItem, ActivityIntentInterface context) {
        this.context = context.getContext();
        activity = context;
        this.parentItem = parentItem;
        this.data = data;
        layoutId = initLayoutId();
        spanSize = initSpansize();
    }

    public int getLayoutId() {
        if (layoutId == 0) {
            throw new Resources.NotFoundException("请设置布局Id");
        }
        return layoutId;
    }

    public void setLayoutId(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }

    public D getData() {
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }

    /**
     * item在每行中的spansize
     * 默认为0,如果为0则占满一行
     * 不建议连续的两级,都设置该数值
     *
     * @return 所占值
     */
    protected int initSpansize() {
        return 0;
    }

    /**
     * 该条目的布局id
     *
     * @return 布局id
     */
    protected abstract int initLayoutId();

    /**
     * 抽象holder的绑定
     *
     * @param holder ViewHolder
     */
    public abstract void onBindViewHolder(ViewHolder holder, int position);

    /**
     * 当item被点击时
     */
    public void onClickChange(TreeItem treeItem) {
        if (mOnClickChangeListener != null) {
            mOnClickChangeListener.onClickChange(treeItem);
        }
    }

    public void setTreeItemManager(TreeItemManager treeItemManager) {
        mTreeItemManager = treeItemManager;
    }

    public void setOnClickChangeListener(onClickChangeListener onClickChangeListener) {
        mOnClickChangeListener = onClickChangeListener;
    }

    /**
     * 获取当前item的父级
     *
     * @return
     */
    public TreeParentItem getParentItem() {
        return parentItem;
    }

    public interface onClickChangeListener {
        void onClickChange(TreeItem treeItem);
    }
}
