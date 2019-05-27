package com.marksixinfo.interfaces;

/**
 * 我的收藏/评论/点赞/历史
 *
 * @Auther: Administrator
 * @Date: 2019/4/22 0022 15:18
 * @Description:
 */
public interface IConcernAndHistory {

    /**
     * 是否是编辑状态
     */
    void isEdit(boolean is);


    /**
     * 选中item
     *
     * @param position 位置
     * @param isSelect 是否选中
     */
    void selectItem(int position, boolean isSelect);
}
