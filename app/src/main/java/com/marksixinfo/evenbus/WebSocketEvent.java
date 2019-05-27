package com.marksixinfo.evenbus;

/**
 * WebSocket服务消息发送
 *
 * @Auther: Administrator
 * @Date: 2019/5/22 0022 19:08
 * @Description:
 */
public class WebSocketEvent {

    public String message;

    public WebSocketEvent() {
    }

    public WebSocketEvent(String message) {
        this.message = message;
    }
}
