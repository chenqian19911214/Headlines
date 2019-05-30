package com.marksixinfo.utils;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.marksixinfo.bean.ClientInfo;
import com.marksixinfo.bean.EditionNameDate;
import com.marksixinfo.constants.NetUrlGallery;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.widgets.EditionDialog;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 版本更新工具类
 * Created by lenovo on 2019/5/30.
 */

public class VersionUpdateUtils {
    private EditionDialog updateDialog;
    private VersionCallback versionCallback;
    public static VersionUpdateUtils initVersionUpdateUtils() {
        return new VersionUpdateUtils();
    }
    /**
     * 设置需要更新的监听
     * */
    public void setVersionCallback(VersionCallback versionCallback) {
        this.versionCallback = versionCallback;
    }

    /**
     * 获取当前版本
     */
    public void getInformation() {
        String url = NetUrlGallery.GET_VERSION_NAME;
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        client.sslSocketFactory();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = handler.obtainMessage();
                message.obj = "";
                message.what = 1;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream input = response.body().byteStream();
                BufferedInputStream bufinput = new BufferedInputStream(input);
                byte[] buffer = new byte[10000];
                int bytes = bufinput.read(buffer);
                final String str = new String(buffer, 0, bytes);
                if (TextUtils.isEmpty(str))
                    return;
                Message message = handler.obtainMessage();
                if (str.contains("<html>")) {
                    message.obj = "";
                    message.what = 1;
                } else {
                    message.obj = str;
                    message.what = 0;
                }
                handler.sendMessage(message);
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) { //请求成功
                String str = (String) msg.obj;
                EditionNameDate responsese = new Gson().fromJson(str, EditionNameDate.class);
                if (responsese == null)
                    return;
                String serverVersionD = responsese.getVersion();
               // String localVersionD = "1.0.2";
                String localVersionD =  ClientInfo.VERSION;
                String serverVersion = serverVersionD.replace(".", "");
                String localVersion = localVersionD.replace(".", "");

                if (TextUtils.isEmpty(serverVersion) || TextUtils.isEmpty(serverVersion))
                    return;
                if ((Integer.valueOf(serverVersion) > Integer.valueOf(localVersion)) && !responsese.getState().equals("0")) {
                    if (versionCallback != null)
                        versionCallback.CallbackData(responsese);
                }
            } else { //请求失败
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getInformation(); //5 秒后再次请求
                    }
                }, 10000);
            }
        }
    };

    /**
     * 获取当前更新弹窗的实体类
     * */
    public EditionDialog getUpdateDialog() {
        return updateDialog;
    }

    /**
     * 当前dialog 是否显示
     * @return true 显示  false 未显示
     * */
    public boolean isShowDialog(){
        if (updateDialog != null && updateDialog.isShowing()){
            return true;
        }else {
            return false;
        }
    }
    /**
     * 更新弹窗
     */
    public void showEditionDialog(Activity currentActivity, EditionNameDate editionNameDater) {
        if (currentActivity != null && !currentActivity.isFinishing()) {
            if (updateDialog == null || !updateDialog.isShowing()) {
                updateDialog = new EditionDialog((ActivityIntentInterface) currentActivity, editionNameDater);
                updateDialog.show();
            }
        }
    }

    public interface VersionCallback {
        void CallbackData(EditionNameDate editionNameDater);
    }
}
