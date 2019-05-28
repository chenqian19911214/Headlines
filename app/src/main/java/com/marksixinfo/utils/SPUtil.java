package com.marksixinfo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.reflect.TypeToken;
import com.marksixinfo.bean.MainHomeData;
import com.marksixinfo.bean.TaskSignInData;

import java.util.ArrayList;
import java.util.List;

public class SPUtil {

    public static final String SP_NAME = "com.marksix.settings";
    public static final String TOKEN = "token";
    public static final String SEARCH_HISTORY = "searchHistory";
    public static final String SEARCH_GALLERY_HISTORY = "searchGalleryHistory";
    public static final String USER_ID = "userId";
    public static final String DEVICE_ID = "deviceId";
    public static final String USER_ACCOUNT = "userAccount";
    public static final String USER_PHOTO = "userPhoto";
    public static final String NICK_NAME = "nickName";
    public static final String SOFT_INPUT_HEIGHT = "softInputHeight";
    public static final String RECOMMEND_TAG = "recommendTag";
    public static final String SP_RECOMMEND_CACHE = "SP_RECOMMEND_CACHE";//推荐key
    public static final String RECOMMEND_CACHE = "recommendCache";//推荐缓存
    public static final String SYSTEM_START_TIME = "system_start_time"; //app启动时间
    public static final String TASK_SIGN_IN = "task_sign_in"; //签到时间
    public static final String INVITATION_CODE = "invitation_code"; //邀请码
    public static final String LOTTERY_SCRATCH = "lottery_scratch"; //开奖页面是否开启刮奖
    public static final String LOTTERY_CURRENT = "lottery_current_data"; //当前开奖号码


    public static String getStringValue(Context context, String key) {
        return getSharedPreferences(context).getString(key, "");
    }

    public static boolean getBooleanValue(Context context, String key) {
        return getSharedPreferences(context).getBoolean(key, false);
    }

    public static void setBooleanValue(Context context, String key, boolean value) {
        getSharedPreferences(context).edit().putBoolean(key, value).commit();
    }

    public static void setStringValue(Context context, String key, String value) {
        getSharedPreferences(context).edit().putString(key, value).commit();
    }

    public static int getIntValue(Context context, String key) {
        return getSharedPreferences(context).getInt(key, 0);
    }

    public static void setIntValue(Context context, String key, int value) {
        getSharedPreferences(context).edit().putInt(key, value).commit();
    }

    public static long getLongValue(Context context, String key) {
        return getSharedPreferences(context).getLong(key, 0);
    }

    public static void setLongValue(Context context, String key, long value) {
        getSharedPreferences(context).edit().putLong(key, value).commit();
    }

    public static void setLoginOut(Context context) {
        setToken(context, "");
        setUserId(context, "");
        setUserPhoto(context, "");
//        setUserAccount(context,"");
        cleanRecommendAndTag(context);
    }

    public static void setToken(Context context, String token) {
        getSharedPreferences(context).edit().putString(TOKEN, token).commit();
    }

    public static String getToken(Context context) {
        return getSharedPreferences(context).getString(TOKEN, "");
    }

    public static void setDeviceId(Context context, String deviceId) {
        getSharedPreferences(context).edit().putString(DEVICE_ID, deviceId).commit();
    }

    public static String getDeviceId(Context context) {
        return getSharedPreferences(context).getString(DEVICE_ID, "");
    }

    public static void setUserAccount(Context context, String userAccount) {
        getSharedPreferences(context).edit().putString(USER_ACCOUNT, userAccount).commit();
    }

    public static String getUserAccount(Context context) {
        return getSharedPreferences(context).getString(USER_ACCOUNT, "");
    }

    public static void setUserId(Context context, String userId) {
        getSharedPreferences(context).edit().putString(USER_ID, userId).commit();
    }

    public static String getUserId(Context context) {
        return getSharedPreferences(context).getString(USER_ID, "");
    }

    public static void setUserPhoto(Context context, String userPhoto) {
        getSharedPreferences(context).edit().putString(USER_PHOTO, userPhoto).commit();
    }

    public static String getUserPhoto(Context context) {
        return getSharedPreferences(context).getString(USER_PHOTO, "");
    }

    public static void setInvitationCode(Context context, String invitationCode) {
        getSharedPreferences(context).edit().putString(INVITATION_CODE, invitationCode).commit();
    }

    public static String getInvitationCode(Context context) {
        return getSharedPreferences(context).getString(INVITATION_CODE, "");
    }

    public static void setNickName(Context context, String nickName) {
        getSharedPreferences(context).edit().putString(NICK_NAME, nickName).commit();
    }

    public static String getNickName(Context context) {
        return getSharedPreferences(context).getString(NICK_NAME, "");
    }

    public static void setRecommendTag(Context context, String recommendTag) {
        getSharedPreferences(context).edit().putString(RECOMMEND_TAG, recommendTag).commit();
    }

    public static String getRecommendTag(Context context) {
        return getSharedPreferences(context).getString(RECOMMEND_TAG, "");
    }

    public static void setRecommendCache(Context context, String recommendCache) {
        getSharedPreferences(context, SP_RECOMMEND_CACHE).edit().putString(RECOMMEND_CACHE, recommendCache).commit();
    }

    /**
     * 退出登录,清除推荐缓存
     *
     * @param context
     */
    public static void cleanRecommendAndTag(Context context) {
        setRecommendTag(context, "");
        setRecommendCache(context, "");
    }

    public static List<MainHomeData> getRecommendCache(Context context) {
        String s = getSharedPreferences(context, SP_RECOMMEND_CACHE).getString(RECOMMEND_CACHE, "");
        if (CommonUtils.StringNotNull(s)) {
            ArrayList<MainHomeData> mainHomeData = null;
            try {
                mainHomeData = JSONUtils.fromJson(s, new TypeToken<ArrayList<MainHomeData>>() {
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (CommonUtils.ListNotNull(mainHomeData)) {
                return mainHomeData;
            }
        }
        return null;

    }

    /**
     * 退出登录调用
     * 逻辑来自原有设置页面点击退出登录
     * SPUtil.getInstance().setStringValue(SPUtil.USER_PASS, "");
     * SPUtil.getInstance().setStringValue(SPUtil.USER_ACCOUNT, "");
     * SPUtil.getInstance().setToken("");
     * SPUtil.getInstance().setStallId("");//设置stallId为空
     * SPUtil.getInstance().setUserId("");
     * SPUtil.getInstance().setIntValue(Constants.AUTH_STATE, 5);//清除设置1 个人认证，2企业认证
     */
    public static void exitLogin(Context context) {
        //友盟推送 userId remove
//        PushUtils.removeAlias(context,SPUtil.getUserId(context));
//        ZallGoTracker.setUserId("");
//        getSharedPreferences(context).edit().remove(TOKEN).
//                //remove(USER_ACCOUNT).
//                remove(STALLID).
//                remove(USER_ID).
//                remove(MY_FRAGMENT_USER_TYPE).
//                putInt(USER_ROLE, UserTypeEnum.BUY.intValue()).   //重置成买家状态
//                putInt(Constants.AUTH_STATE, 5).
//                commit();
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    public static boolean isLogin(Context context) {
        return CommonUtils.StringNotNull(getToken(context), getUserId(context));
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return getSharedPreferences(context, SP_NAME);
    }

    public static SharedPreferences getSharedPreferences(Context context, String fileName) {
        if (context == null) {
            return null;
        }
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }

    /**
     * 删除指定key的缓存数据
     *
     * @param context
     * @param key
     */
    public static void removeKeyValue(Context context, String key) {
        getSharedPreferences(context).edit().remove(key).commit();
    }


    public static void setSearchHistory(Context context, String value) {
        setStringValue(context, SEARCH_HISTORY, value);
    }

    public static String getSearchHistory(Context context) {
        return getStringValue(context, SEARCH_HISTORY);
    }

    public static void removeSearchHistory(Context context) {
        removeKeyValue(context, SEARCH_HISTORY);
    }

    public static void setSearchGalleryHistory(Context context, String value) {
        setStringValue(context, SEARCH_GALLERY_HISTORY, value);
    }

    public static String getSearchGalleryHistory(Context context) {
        return getStringValue(context, SEARCH_GALLERY_HISTORY);
    }

    public static void removeSearchGalleryHistory(Context context) {
        removeKeyValue(context, SEARCH_GALLERY_HISTORY);
    }

    public static void setSoftInputHeight(Context context, int value) {
        setIntValue(context, SOFT_INPUT_HEIGHT, value);
    }

    public static int getSoftInputHeight(Context context) {
        return getIntValue(context, SOFT_INPUT_HEIGHT);
    }

    /**
     * 设置app启动时间
     *
     * @param context
     */
    public static void setSystemStartTime(Context context) {
        setLongValue(context, SYSTEM_START_TIME, System.currentTimeMillis());
    }

    /**
     * 获取app启动时间
     *
     * @param context
     */
    public static long getSystemStartTime(Context context) {
        return getLongValue(context, SYSTEM_START_TIME);
    }


    /**
     * 清除签到时间
     *
     * @param context
     */
    public static void cleanTaskSignInTime(Context context) {
        setStringValue(context, TASK_SIGN_IN, "");
    }

    /**
     * 设置签到时间
     *
     * @param context
     */
    public static void setTaskSignInTime(Context context, TaskSignInData data) {
        setStringValue(context, TASK_SIGN_IN, JSONUtils.toJson(data));
    }

    /**
     * 获取签到时间
     *
     * @param context
     */
    public static TaskSignInData getTaskSignInTime(Context context) {
        TaskSignInData data = null;
        try {
            String stringValue = getStringValue(context, TASK_SIGN_IN);
            if (CommonUtils.StringNotNull(stringValue)) {
                data = JSONUtils.fromJson(stringValue, TaskSignInData.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
