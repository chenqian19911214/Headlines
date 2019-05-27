package com.marksixinfo.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.marksixinfo.adapter.ViewHolder;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.interfaces.OnItemClickListener;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 简单适配器
 *
 * @Auther: Administrator
 * @Date: 2019/4/19 0019 13:11
 * @Description:
 */
public abstract class SimpleAdapter2<T> extends RecyclerView.Adapter<ViewHolder> {

    protected LayoutInflater mInflater;
    protected ActivityIntentInterface mContext;
    protected List<T> mData;
    protected final int mItemLayoutId;
    protected int currentPosition;
    protected OnItemClickListener listener;


    public SimpleAdapter2(ActivityIntentInterface context, List<T> mData, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext.getContext());
        this.mData = mData;
        this.mItemLayoutId = itemLayoutId;
    }

    public SimpleAdapter2(ActivityIntentInterface context, List<T> mData, int itemLayoutId, OnItemClickListener listener) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext.getContext());
        this.mData = mData;
        this.mItemLayoutId = itemLayoutId;
        this.listener = listener;
    }

    public void changeDataUi(List<T> content) {
        this.mData = content;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ViewHolder.createViewHolder(parent, mItemLayoutId);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        currentPosition = position;
        convert(holder, mData.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public abstract void convert(ViewHolder holder, T item);
}
