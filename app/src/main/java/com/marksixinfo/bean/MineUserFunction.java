package com.marksixinfo.bean;

import android.content.Context;
import android.content.res.Resources;

import com.marksixinfo.R;
import com.marksixinfo.base.IntentUtils;
import com.marksixinfo.constants.StringConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的页面8大功能操作按钮
 *
 * @Auther: Administrator
 * @Date: 2019/4/19 0019 13:24
 * @Description:
 */
public class MineUserFunction {


    public static final String[] texts = {"提现兑换", "收入记录", "邀请好友", "好友列表",
            "我的收藏", "我的评论", "我的点赞", "浏览历史"};

    public static final int[] images = {
            R.drawable.icon_user_withdraw_deposit
            , R.drawable.icon_user_income_record
            , R.drawable.icon_user_invite_friends
            , R.drawable.icon_user_friends_list
            , R.drawable.icon_user_collect
            , R.drawable.icon_user_comment
            , R.drawable.icon_user_praise
            , R.drawable.icon_user_history};


    private String text;
    private int image;
    private String host;

    public MineUserFunction() {
    }

    public MineUserFunction(String text, int image, String host) {
        this.text = text;
        this.image = image;
        this.host = host;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


    /**
     * @param context
     * @param parentType 0,头条 1,论坛
     * @return
     */
    private static List<String> getHost(Context context, String parentType) {
        List<String> list = new ArrayList<>();
        Resources r = context.getResources();
        String s = IntentUtils.getScheme(context) + "://";
        //提现兑换
        list.add(s + r.getString(R.string.WithdrawDepositActivity));
        //收益记录
        list.add(s + r.getString(R.string.IncomeRecordActivity) + "?" + StringConstants.TYPE + "=0");
        //邀请好友
        list.add(s + r.getString(R.string.InviteFriendsActivity));
        //好友列表
        list.add(s + r.getString(R.string.FriendsListActivity));
        //我的收藏/评论/点赞/历史
        for (int i = 0; i < 4; i++) {
            list.add(s + r.getString(R.string.MineConcernAndHistoryActivity)
                    + "?" + StringConstants.TYPE + "=" + String.valueOf(i) + "&"
                    + StringConstants.PARENT_TYPE + "=" + parentType);
        }
        return list;
    }


    /**
     * 已登录列表
     *
     * @param context
     * @param parentType 0,头条 1,论坛
     * @return
     */
    public static List<MineUserFunction> getAllListData(Context context, String parentType) {
        List<MineUserFunction> list = new ArrayList<>();
        List<String> hosts = getHost(context, parentType);
        for (int i = 0; i < 8; i++) {
            list.add(new MineUserFunction(texts[i], images[i], hosts.get(i)));
        }
        return list;
    }

    /**
     * 已登录/任务中心列表
     *
     * @param context
     * @param start
     * @param end
     * @param parentType 0,头条 1,论坛
     * @return
     */
    public static List<MineUserFunction> getListData(Context context, int start, int end, String parentType) {
        List<MineUserFunction> list = new ArrayList<>();
        List<String> hosts = getHost(context, parentType);
        for (int i = start; i < end; i++) {
            try {
                list.add(new MineUserFunction(texts[i], images[i], hosts.get(i)));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return list;
    }

}
