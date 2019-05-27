package com.marksixinfo.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @Auther: Administrator
 * @Date: 2019/3/14 0014 19:49
 * @Description:
 */
public class ResultData<T> {

    @SerializedName("result")
    private T result;
    @SerializedName("errorCode")
    private String errorCode;
    @SerializedName("msg")
    private String msg;
    @SerializedName("status")
    private int status;//返回状态码为2表示未登陆,客户端根据情况提示用户是否跳转至登录界面

    public T getData() {
        return result;
    }

    public void setData(T data) {
        this.result = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return msg;
    }

    public void setErrorMsg(String errorMsg) {
        this.msg = errorMsg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isOk() {
        return getStatus() == 1;
    }

    public boolean isLoginOut(){
        return getStatus() == 2;
    }
}
