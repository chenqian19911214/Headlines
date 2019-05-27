package com.marksixinfo.base;

/**
 * @Auther: Administrator
 * @Date: 2019/3/14 0014 20:51
 * @Description:
 */
public class NetPresenterBase extends PresenterBase {
    final int apiSuccessImgId = 0;
    final int apiErrorImgId = 0;
    final int apiInfoImgId = 0;
    final int apiWarningImgId = 0;

    public NetPresenterBase(IBaseView IBaseView) {
        super(IBaseView);
    }

    /**
     * 成功类提示
     *
     * @param msg
     */
    public void toastSuccess(String msg) {
        toast(msg, apiSuccessImgId);
    }

    /**
     * 信息类提示
     *
     * @param msg
     */
    public void toastInfo(String msg) {
        toast(msg, apiInfoImgId);
    }

    /**
     * 预警类提示
     *
     * @param msg
     */
    public void toastWarning(String msg) {
        toast(msg, apiWarningImgId);
    }

    /**
     * 错误类提示
     *
     * @param msg
     */
    public void toastError(String msg) {
        toast(msg, apiErrorImgId);
    }
}

