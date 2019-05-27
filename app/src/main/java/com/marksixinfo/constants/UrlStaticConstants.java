package com.marksixinfo.constants;

/**
 * 静态url
 *
 * @Auther: Administrator
 * @Date: 2019/3/24 0024 23:06
 * @Description:
 */
public interface UrlStaticConstants {

    /**
     * 文章详情webView 前缀
     */
//    String DETAIL_WEB_VIEW = "https://webview.sskk58.com/?device=android&id=";
    String DETAIL_WEB_VIEW = "http://webview." + StringConstants.Main_Host + "?device=android&id=";

    /**
     * 论坛
     */
//    String FORUM = "https://www.sskk58.com/?c=bbs&device=android";
//    String FORUM = "https://www.34399.com/?c=bbs&device=android";
//    String FORUM = "http://api.bbs.lhtt.co/?device=android";

    /**
     * 开奖
     */
//    String LOTTERY = "https://www.34399.com/?c=lottery&device=android";
    String LOTTERY = "http://www." + StringConstants.Main_Host + "?c=lottery&device=android";


    /**
     * 图库
     */
//    String IMAGES = "https://www.34399.com/?c=images&device=android";
    String IMAGES = "http://www." + StringConstants.Main_Host + "?c=images&device=android";


    /**
     * 分享下载
     */
    String SHARE_URL = "http://app.lhtt.co";


    /**
     * 邀请好友
     */
    String INVITE_FRIENDS = "http://m.lhtt.co/?c=reg&device=android&ref=";


    /**
     * 任务中心说明
     */
    String TASK_EXPLAIN = "http://m.lhtt.co/?c=rule&a=task&device=android";


    /**
     * 提现说明
     */
    String WITHDRAW_DEPOSIT = "http://m.lhtt.co/?c=rule&a=withdraw&device=android";

    /**
     * 收益说明
     */
    String EARNINGS_EXPLAIN = "http://m.lhtt.co/?c=rule&a=income&device=android";

    /**
     * 邀请好友说明
     */
    String INVITE_FRIENDS_EXPLAIN = "http://m.lhtt.co/?c=rule&a=invite&device=android";


    /**
     *  注册免责声明
     */
    String REGISTER_DISCLAIMER = "http://m.lhtt.co/?c=rule&a=term&device=android";
}
