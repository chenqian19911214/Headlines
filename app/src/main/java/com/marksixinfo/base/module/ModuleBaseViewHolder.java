package com.marksixinfo.base.module;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by yishen on 2018/4/3.
 */

public abstract class ModuleBaseViewHolder extends RecyclerView.ViewHolder {
    protected IModeView IModeView;

    public ModuleBaseViewHolder(View itemView) {
        super(itemView);
    }

    public ModuleBaseViewHolder(ViewGroup parent, int viewId) {
        super(LayoutInflater.from(parent.getContext()).inflate(viewId, parent, false));
        if(itemView!=null&&itemView.getContext()!=null&&itemView.getContext() instanceof IModeView)
        {
            IModeView = (IModeView) itemView.getContext();
        }
        afterView(itemView);
    }
    /**
     * 获取对应CMS接口，操作其他ViewHolder或者整体页面
     * @return
     */
    public IModeView getIModeView() {
        return IModeView;
    }


    /**
     * 绑定VIewID
     * @param v
     */
    protected abstract void afterView(View v);

    /**
     * 刷新页面，相当于getView
     * @param item
     * @param adapterPosition
     */
    protected abstract void refreshUi(final ModuleBaseMode item, final int adapterPosition);

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            try {
                ModuleBaseViewHolder.this.handleMessage(msg);
            } catch (Exception exp) {
                Log.e(this.getClass().getName(), "ErrorMessage: ", exp);
            }
        }
    };

    public void handleMessage(Message msg) {

    }


    public String getString(int resId){
        if (IModeView.getContext()==null){
            return "";
        }
        return IModeView.getContext().getString(resId);
    }

    public int   getColor(int resId){
        if (IModeView.getContext()==null){
            return Color.WHITE;
        }
        return IModeView.getContext().getResources().getColor(resId);
    }

}
