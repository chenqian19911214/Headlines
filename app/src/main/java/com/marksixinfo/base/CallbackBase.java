package com.marksixinfo.base;

import com.marksixinfo.R;
import com.marksixinfo.bean.ResultData;
import com.marksixinfo.evenbus.TokenTimeOutEvent;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.LogUtils;
import com.marksixinfo.utils.NetWorkUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.greenrobot.eventbus.EventBus;

import java.util.UUID;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Auther: Administrator
 * @Date: 2019/3/14 0014 19:50
 * @Description:
 */
public abstract class CallbackBase<T> extends Callback<ResultData<T>> {


    String cancelTag = "";
    private static final String TAG = "CallbackBase";
    IBaseView ui;
    private long lastLoadTime;

    public CallbackBase(IBaseView ui) {
        this.ui = ui;
        setTag();
        this.cancelTag = UUID.randomUUID().toString();
    }

    /**
     * 是否需要请求中弹框
     */
    boolean isNeedDialog = true;


    /**
     * 加载框显示文案
     */
    String dialogMsg = "";
    /**
     * 是否需要报错提示toast
     */
    boolean isNeedToast = true;
    /**
     * 是否使用列表loading
     */
    boolean isListLoading = false;


    /**
     * 登录失效,是否跳转登录
     */
    boolean isGoToLogin = true;


    /**
     * Thread Pool Thread
     *
     * @param response
     * @param id
     */
    @Override
    public abstract ResultData<T> parseNetworkResponse(Response response, int id) throws Exception;

    public void onError(String msg, String code) {
        if (!isUIUseable()) {
            return;
        }
        if (isNeedToast()) {
            ui.toastError(msg);
        }
        LogUtils.e(TAG, msg + "____" + code);
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        if (NetWorkUtils.isNetworkConnected(ui.getContext())) {
            if (e != null && (e.toString().contains("closed") || e.toString().contains("Canceled"))) {
                onError("连接服务失败", "404");
            } else if (call != null && call.isCanceled()) {
                LogUtils.e("用户取消请求");
            } else if (e != null && (e.toString().contains("502"))) {
                onError("服务未启动", "502");
            } else if (e != null && (e.toString().contains("404"))) {
                onError("访问服务器地址出错", "404");
            } else if (e != null && (e.toString().toUpperCase().contains("FAILED TO CONNECT TO"))) {
                onError("连接服务失败", "404");
            } else if (e != null && (e.toString().toUpperCase().contains("TIMEOUT"))) {
                onError("连接服务失败", "404");
            } else {
                onError(getString(R.string.toast_common_system_net_error), e != null ? e.toString() : "");
            }
        } else {
            onConnectFail();
        }
    }

    /**
     * 没有网络
     */
    public void onConnectFail() {
        onError(getString(R.string.toast_common_system_net_error), "");
    }

    @Override
    public void onResponse(ResultData<T> response, int id) {
        try {
            animationTime();
            if (response != null) {
                if (response.isOk()) {
                    onSuccess(response.getData(), id);
                } else if (response.isLoginOut()) {
                    //登录失效,未登录
                    EventBus.getDefault().post(new TokenTimeOutEvent(isGoToLogin, response.getErrorMsg()));
                } else if (CommonUtils.StringNotNull(response.getErrorMsg())) {
                    LogUtils.d(response.getErrorMsg());
                    onError(response.getErrorMsg(), response.getErrorCode());
                } else {
                    onError(null, null, -1);
                }
            } else {
                onError(null, null, -1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(e);
            onConnectFail();
        }
    }

    private void animationTime() {
        if (isListLoading()) {
            if (System.currentTimeMillis() - lastLoadTime < 300) {
                try {
                    LogUtils.d("延时开始" + System.currentTimeMillis());
                    Thread.sleep(300);//保证300毫秒的动画
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            LogUtils.d("延时结束" + System.currentTimeMillis());
        }
    }

    @Override
    public void onBefore(Request request, int id) {
        super.onBefore(request, id);
        if (!isUIUseable()) {
            return;
        }
        ui.onBefore();
        if (isNeedDialog()) {
            if (isListLoading()) {
                lastLoadTime = System.currentTimeMillis();
                LogUtils.d("开始时间" + lastLoadTime);
            }
            ui.showDialog(getDialogMsg(), false, isListLoading());
        }
    }

    @Override
    public void onAfter(int id) {
        super.onAfter(id);
        if (!isUIUseable()) {
            return;
        }
        if (isNeedDialog()) {
            ui.closeDialog();
        }
    }

    public boolean isUIUseable() {
        return ui != null && ui.getContext() != null;
    }


    public abstract void onSuccess(T response, int id);

    String getString(int id) {
        if (isUIUseable() && ui.getContext() != null) {
            return ui.getContext().getString(id);
        }
        return "";
    }

    public boolean isNeedDialog() {
        return isNeedDialog;
    }

    public CallbackBase<T> setNeedDialog(boolean needDialog) {
        isNeedDialog = needDialog;
        return this;
    }

    public String getDialogMsg() {
        return dialogMsg;
    }

    public CallbackBase<T> setDialogMsg(String dialogMsg) {
        this.dialogMsg = dialogMsg;
        return this;
    }

    public boolean isNeedToast() {
        return isNeedToast;
    }

    public CallbackBase<T> setNeedToast(boolean needToast) {
        isNeedToast = needToast;
        return this;
    }

    public boolean isListLoading() {
        return isListLoading;
    }

    public CallbackBase<T> setListLoading(boolean listLoading) {
        isListLoading = listLoading;
        return this;
    }

    /**
     * 取消tag
     *
     * @return
     */
    public String getCancelTag() {
        return cancelTag;
    }

    /**
     * 是否允许空入参
     *
     * @return
     */
    public boolean isAllowNullParams() {
        return false;
    }

    /**
     * 取消请求
     */
    public void cancel() {
//        LogUtil.e(TAG,getCancelTag());
        BaseNetUtil.cancel(getCancelTag());
    }

    public boolean isGoToLogin() {
        return isGoToLogin;
    }

    public CallbackBase<T> setGoToLogin(boolean goToLogin) {
        isGoToLogin = goToLogin;
        return this;
    }

    private void setTag() {
//        if (ui != null) {
//            ui instanceof MarkSixActivity
//        }

    }
}