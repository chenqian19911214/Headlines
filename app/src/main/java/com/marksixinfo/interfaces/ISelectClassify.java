package com.marksixinfo.interfaces;

import com.marksixinfo.bean.SelectClassifyData;

/**
 *  选择分类
 *
 * @Auther: Administrator
 * @Date: 2019/3/18 0018 16:09
 * @Description:
 */
public interface ISelectClassify {


    /**
     * 功能描述: 点击分类
     *
     * @param: 分类
     * @auther: Administrator
     * @date: 2019/3/18 0018 下午 4:11
     */
    void clickClassify(SelectClassifyData classify);


    /**
     * 功能描述:    删除分类
     *
     * @param: 删除位置
     * @auther: Administrator
     * @date: 2019/3/18 0018 下午 4:12
     */
    void deleteClassify(int position);

    /**
     * 功能描述: 拖动完成
     *
     * @auther: Administrator
     * @date: 2019/3/18 0018 下午 5:01
     */
    void changeOrderSuccess();


    /**
     * 功能描述: 拖动完成
     *
     * @auther: Administrator
     * @date: 2019/3/18 0018 下午 5:01
     */
    void updateClassifyName(int position);

    /**
     * 功能描述: 关闭侧滑
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2019/3/29 0029 13:22
     */
    void closeAllOpenIndex(int position);
}
