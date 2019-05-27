package com.marksixinfo.base.module;


import com.marksixinfo.interfaces.ActivityIntentInterface;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by yishen on 2018/3/30.
 */

public interface IModeView extends ActivityIntentInterface {


    /**
     *  刷新页面
     *
     *  单条刷新时  传入位置,addCount和removeCount传1
     *  添加时    传入位置和添加个数,removeCount传0
     *  删除时   传入位置和删除个数,addCount传0
     *
     * @param start     刷新/添加/删除的位置
     * @param addCount  添加的个数
     * @param removeCount  删除的个数
     */
    void refreshUI(int start, int addCount, int removeCount);


    /**
     * 根据position获取对应的ViewType
     * @param adapterPosition
     * @return
     */
    int getContentViewType(int adapterPosition);

    /**
     * adapter 对整体页面的特殊回调
     * @param cmsKey 对应的CMSKey
     * @param position 对应的position
     * @param data 对应mode
     * @param type 类型区分
     */
    void doSomething(String cmsKey, int position, ModuleBaseMode data, int type);

    /**
     * 保存请求入参数据
     * @param cmsParams
     */
    void saveParams(CMSParams cmsParams);

    /**
     * 获取请求入参数据
     */
    CMSParams getParams();

    /**
     * 无数据展示
     */
    void showEmptyView();

    /**
     * 网络异常显示
     */
    void showErrorView();

    /**
     * 下拉刷新
     */
    void refreshAll();

    /**
     * 加载下一页
     */
    void loadMore(int oldSize, int addSize);

    /**
     * 渲染标题栏
     */
    void refreshTitle();

    /**
     * 渲染导航
     */
    void refreshTab();


    void setFootView();

    /**
     * 获取有倒计时的holder
     * @return
     */
    HashMap<ITimerModuleViewHolder,ITimerModuleMode> getTimerHolder();


    ArrayList<ModuleBaseMode> getDataList();


    void loadingData();
}
