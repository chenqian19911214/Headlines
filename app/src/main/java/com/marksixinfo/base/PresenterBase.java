package com.marksixinfo.base;

import com.marksixinfo.utils.LogUtils;
import com.marksixinfo.widgets.bigimageViewpage.tool.utility.ui.ToastUtil;

import androidx.fragment.app.FragmentActivity;

/**
 * @Auther: Administrator
 * @Date: 2019/3/14 0014 20:45
 * @Description:
 */
public class PresenterBase {
    private static final String TAG = "PresenterBase";

    public IBaseView getiBaseView() {
        return iBaseView;
    }

    IBaseView iBaseView;
    public LoadingDialog dialog;

    public PresenterBase(IBaseView IBaseView) {
        this.iBaseView = IBaseView;
    }

    public void showDialog(String msg, boolean canCancle, boolean isListLoading) {
        closeDialog();
        if (iBaseView != null && iBaseView.getContext() != null) {
            if (iBaseView.getContext() instanceof FragmentActivity) {
                if (((FragmentActivity) iBaseView.getContext()).isFinishing()) {
                    return;
                }
            }
            LogUtils.d(TAG, "showDialog" + dialog == null);
            if (dialog == null) {
                dialog = new LoadingDialog(iBaseView.getContext());
            }
            dialog.setCanceledOnTouchOutside(canCancle);
            dialog.show(msg, isListLoading);
        }

    }

    public void closeDialog() {
//        LogUtils.d(TAG, "closeDialog" + dialog == null);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void toast(String msg, int imgId) {
//        if (imgId < 0) {
//            ToastShow.toastShow(iBaseView.getContext(), String.valueOf(msg));
//        } else {
//            ToastShow.toastShow(iBaseView.getContext(), String.valueOf(msg), imgId);
//        }
//        ToastShow.toastShow(iBaseView.getContext(), msg);
        ToastUtil.getInstance()._short(iBaseView.getContext(), msg);
    }

    public String getString(int id) {
        return iBaseView.getContext().getString(id);
    }
}
