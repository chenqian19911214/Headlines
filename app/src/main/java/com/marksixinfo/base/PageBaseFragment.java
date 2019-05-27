package com.marksixinfo.base;

/**
 * @Auther: Administrator
 * @Date: 2019/3/22 0022 11:09
 * @Description:
 */
public abstract class PageBaseFragment extends MarkSixFragment {

    /**
     * 是否重复加载
     */
    protected boolean isReload = false;


    /**
     * 是否初始化
     */
    protected boolean isInit = true;


    /**
     * 是否可见
     */
    protected boolean isVisible;

    /**
     * 是否等待可见加载
     */
    protected boolean isWaitVisible;

    /**
     * 加载调用
     */
    public void setInitData() {
        if (isVisible) {
            isWaitVisible = false;
            if (isReload) {
                loadData();
            } else {
                if (isInit) {
                    loadData();
                    isInit = false;
                }
            }
        } else {
            isWaitVisible = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible && isWaitVisible) {
            setInitData();
        }
    }

    /**
     * 加载数据
     */
    protected abstract void loadData();


    public void setInit(boolean init) {
        isInit = init;
    }

    public void setShowCategory() {
    }

    public void setHiddenCategory() {
    }

    public void changeSelect(int oldPosition, int newPosition) {
    }
}