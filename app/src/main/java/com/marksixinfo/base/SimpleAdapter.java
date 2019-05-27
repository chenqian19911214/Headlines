package com.marksixinfo.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.marksixinfo.adapter.ViewHolder;
import com.marksixinfo.interfaces.ActivityIntentInterface;

import java.util.List;

/**
 * 简单适配器
 *
 * @Auther: Administrator
 * @Date: 2019/4/19 0019 13:11
 * @Description:
 */
public abstract class SimpleAdapter<T> extends BaseAdapter {

    protected LayoutInflater mInflater;
    protected ActivityIntentInterface mContext;
    protected List<T> mData;
    protected final int mItemLayoutId;


    public SimpleAdapter(ActivityIntentInterface context, List<T> mData, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext.getContext());
        this.mData = mData;
        this.mItemLayoutId = itemLayoutId;
    }

    public void changeDataUi(List<T> content) {
        this.mData = content;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = ViewHolder.get(convertView, parent, mItemLayoutId);
        convert(viewHolder, getItem(position));
        return viewHolder.getConvertView();

    }

    public abstract void convert(ViewHolder holder, T item);
}
