package com.marksixinfo.base;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.marksixinfo.R;
import com.marksixinfo.bean.ResultData;
import com.marksixinfo.evenbus.TokenTimeOutEvent;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.LogUtils;
import com.marksixinfo.utils.NetWorkUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import ikidou.reflect.TypeBuilder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * post数值callback
 *
 * @Auther: Administrator
 * @Date: 2019/4/3 0003 20:03
 * @Description:
 */
public abstract class ArrayCallback<T> implements Callback {

    IBaseView ui;
    private final Handler handler;
    private static final String TAG = "ArrayCallback";

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


    public ArrayCallback(IBaseView ui) {
        this.ui = ui;
        handler = new Handler(Looper.getMainLooper());
    }


    @Override
    public void onFailure(Call call, IOException e) {
        if (ui != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onError(call, e, 0);
                }
            });
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        try {
            if (response != null && response.body() != null) {
                JsonObject s = new JsonParser().parse(response.body().string()).getAsJsonObject();
                ResultData<T> resultData = BaseNetUtil.parseFromJson(s.toString(),
                        new TypeToken<ResultData<T>>() {
                        }.getType());
                if (s.has("result")) {
                    JsonElement result = s.remove("result");
                    if (result != null && resultData != null) {
                        resultData.setData(BaseNetUtil.parseFromJson(result.toString(),
                                TypeBuilder.newInstance(String.class).build()));
                    }
                }
                if (ui != null && ui.getContext() != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showResponse((ResultData<String>) resultData);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(e);
            onConnectFail();
        } finally {
            onAfter();
        }
    }

    public abstract void onSuccess(ResultData<String> response);


    private void showResponse(ResultData<String> response) {
        if (response.isOk()) {
            onSuccess(response);
        } else if (response.isLoginOut()) {
            //登录失效,未登录
            MobclickAgent.onProfileSignOff();
            EventBus.getDefault().post(new TokenTimeOutEvent(isGoToLogin, response.getErrorMsg()));
        } else if (CommonUtils.StringNotNull(response.getErrorMsg())) {
            onError(response.getErrorMsg(), response.getErrorCode());
        } else {
            onError(null, null, -1);
        }
        onAfter();
    }

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
                onError(ui.getContext().getResources().getString(R.string.toast_common_system_net_error), "");
            }
        } else {
            onConnectFail();
        }
    }

    public void onBefore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!isUIUseable()) {
                    return;
                }
                ui.onBefore();
                if (isNeedDialog()) {
                    ui.showDialog(getDialogMsg(), false, isListLoading());
                }
            }
        });
    }

    public void onAfter() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!isUIUseable()) {
                    return;
                }
                if (isNeedDialog()) {
                    ui.closeDialog();
                }
            }
        });
    }


    String getString(int id) {
        if (isUIUseable() && ui.getContext() != null) {
            return ui.getContext().getString(id);
        }
        return "";
    }

    public boolean isUIUseable() {
        return ui != null && ui.getContext() != null;
    }

    public boolean isNeedDialog() {
        return isNeedDialog;
    }

    public ArrayCallback<T> setNeedDialog(boolean needDialog) {
        isNeedDialog = needDialog;
        return this;
    }

    public boolean isNeedToast() {
        return isNeedToast;
    }

    public ArrayCallback<T> setNeedToast(boolean needToast) {
        isNeedToast = needToast;
        return this;
    }

    public String getDialogMsg() {
        return dialogMsg;
    }

    public ArrayCallback<T> setDialogMsg(String dialogMsg) {
        this.dialogMsg = dialogMsg;
        return this;
    }


    public boolean isListLoading() {
        return isListLoading;
    }

    public ArrayCallback<T> setListLoading(boolean listLoading) {
        isListLoading = listLoading;
        return this;
    }

    public boolean isGoToLogin() {
        return isGoToLogin;
    }

    public ArrayCallback<T> setGoToLogin(boolean goToLogin) {
        isGoToLogin = goToLogin;
        return this;
    }


    public void onError(String msg, String code) {
        if (!isUIUseable()) {
            return;
        }
        if (isNeedToast()) {
            ui.toastError(msg);
        }
        LogUtils.e(TAG, msg + "____" + code);
    }

    public void onConnectFail() {
        if (ui != null) {
            onError(ui.getContext().getResources().getString(R.string.toast_common_system_net_error), "");
        }
    }
}
