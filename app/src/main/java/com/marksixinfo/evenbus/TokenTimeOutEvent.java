package com.marksixinfo.evenbus;

/**
 *  登录失效(退出登录)
 *
 * @Auther: Administrator
 * @Date: 2019/3/23 0023 18:17
 * @Description:
 */
public class TokenTimeOutEvent {


    /**
     * 登录失效,是否跳转登录
     */
    public boolean isGoToLogin = false;

    public String errorMsg;


    public TokenTimeOutEvent(boolean isGoToLogin, String errorMsg) {
        this.isGoToLogin = isGoToLogin;
        this.errorMsg = errorMsg;
    }

    public TokenTimeOutEvent() {
    }
}
