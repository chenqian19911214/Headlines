package com.marksixinfo.base.module;

/**
 * RecyclerView 中需要定时刷新ViewHolder时所需接口
 */

public interface ITimerModuleViewHolder {
    /**
     * 需要定时刷新的操作
     * @param item
     */
    void onTimeChange(final ModuleBaseMode item);
}
