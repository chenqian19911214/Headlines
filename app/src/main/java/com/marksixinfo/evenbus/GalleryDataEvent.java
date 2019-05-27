package com.marksixinfo.evenbus;

import com.marksixinfo.bean.GalleryListData;

import java.util.List;

/**
 * 获取图片宽高
 *
 * @Auther: Administrator
 * @Date: 2019/5/9 0009 16:03
 * @Description:
 */
public class GalleryDataEvent {

    public List<GalleryListData.ChildBean> datas;
    public int type;

    public GalleryDataEvent() {
    }

    public GalleryDataEvent(List<GalleryListData.ChildBean> datas, int type) {
        this.datas = datas;
        this.type = type;
    }
}
