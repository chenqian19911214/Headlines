package com.marksixinfo.interfaces;

/**
 * Created by Administrator on 2019/5/11.
 * 图库关键字搜索 点击事件
 */

public interface GallerySearchItemClickListener {

    /**
     * 点击某一期/点击条目
     * @param id id
     * @param period 期数
      * */
    void periodsSelect(String id, String period);
}
