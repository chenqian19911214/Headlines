package com.marksixinfo.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.bean.ClientInfo;
import com.marksixinfo.bean.CommentsListDetail;
import com.marksixinfo.bean.DetailCommentData;
import com.marksixinfo.bean.DetailData;
import com.marksixinfo.bean.ForumCommListBean;
import com.marksixinfo.bean.LoginData;
import com.marksixinfo.bean.LotteryRealTimeData;
import com.marksixinfo.bean.MainHomeData;
import com.marksixinfo.bean.MineConcernData;
import com.marksixinfo.bean.SelectClassifyData;
import com.marksixinfo.bean.TaskCenterData;
import com.marksixinfo.bean.TaskSignInData;
import com.marksixinfo.bean.UserPostCenterData;
import com.marksixinfo.bean.UserPostCenterMember;
import com.marksixinfo.constants.NumberConstants;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.LotteryRealTimeEvent;
import com.marksixinfo.widgets.bigimageViewpage.ImagePreview;
import com.marksixinfo.widgets.bigimageViewpage.bean.ImageInfo;
import com.marksixinfo.widgets.bigimageViewpage.tool.utility.ui.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @Auther: Administrator
 * @Date: 2019/3/14 0014 20:02
 * @Description:
 */
public class CommonUtils {

//    /**
//     * 验证手机格式
//     */
//    public static boolean isMobileNO(String mobiles) {
//        /*
//         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
//         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
//         * 总结起来就是第一位必定为1，第二位必定为3或5或7或8，其他位置的可以为0-9
//         */
//        String telRegex = "[1][0-9]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
//        if (TextUtils.isEmpty(mobiles))
//            return false;
//        else
//            return mobiles.matches(telRegex);
//    }

    /**
     * 验证手机号码
     *
     * @param mobiles
     * @return [0-9]{5,9}
     */
    public static boolean isMobileNO(String mobiles) {
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证输入金额
     */
    public static boolean isMoneyNO(String var) {  //^(([1-9]\d{0,9})|0)(\.\d{1,2})?$

        String telRegex = "^(([0-9]+\\.[0-9]{1,2})|([0-9]*[0-9][0-9]*\\.[0-9]{1,2})|([1-9]*[1-9][0-9]*))$";
        if (TextUtils.isEmpty(var))
            return false;
        else {
            return var.matches(telRegex);
        }
    }


    public static int dp2px(int dp, Context mContext) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                mContext.getResources().getDisplayMetrics());
    }


    /**
     * 保留2位小数
     *
     * @param val
     * @return
     */
    public static String rahToStr(double val) {
        if (!Double.isNaN(val) && val != Double.NEGATIVE_INFINITY && val != Double.POSITIVE_INFINITY) {
            BigDecimal bd = new BigDecimal(val);
            val = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            return decimalFormat.format(val);
        }
        return "";
    }

    /**
     * 保留2位小数
     *
     * @param val
     * @return
     */
    public static String rahToStr(double val, boolean isCut) {
        if (!Double.isNaN(val) && val != Double.NEGATIVE_INFINITY && val != Double.POSITIVE_INFINITY) {
            BigDecimal bd = new BigDecimal(val);
            if (isCut) {
                val = bd.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
            } else {
                val = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
            DecimalFormat decimalFormat = new DecimalFormat("0.00");

            return decimalFormat.format(val);
        }
        return "";
    }


    /**
     * 保留1位小数
     *
     * @param val
     * @return
     */
    public static String radixToStr(double val) {
        if (!Double.isNaN(val) && val != Double.NEGATIVE_INFINITY && val != Double.POSITIVE_INFINITY) {
            BigDecimal bd = new BigDecimal(val);
            val = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            DecimalFormat decimalFormat = new DecimalFormat("0.0");
            return decimalFormat.format(val);
        }
        return "";
    }


    /**
     * List to 逗号分割的字符串
     *
     * @param list
     * @return
     */
    public static String listToString(List list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i) + ",");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }

    /**
     * map to 顿号分割的下弧线连接字符串
     *
     * @param map
     * @return
     */
    public static String mapToString(Map<Long, Long> map) {
        if (MapNotNull(map)) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<Long, Long> entry : map.entrySet()) {
                Long key = entry.getKey();
                Long value = entry.getValue();
                sb.append(key + "_" + value + ",");
            }
            String s = sb.toString();
            if (StringNotNull(s)) {
                try {
                    s = s.substring(0, s.length() - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return s;
            }
        }
        return "";
    }

    /**
     * 将每三个数字加上逗号处理（通常使用金额方面的编辑）
     *
     * @return 加上逗号的数字
     */

    public static String addComma(double price) {
        return MoneyUtils.rahToStr(price);
    }

    public static void hidInputKeyBord(View v, Context context) {
        InputMethodManager imm = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }


    public final static String numberToShowString(int number) {
        int numberW = 10000;
        int numberK = 1000;
        //百万以下
        int value_W = number / numberW;
        int value_mode_W = number % numberW;

        if (value_W <= 0) {
            //少于 1 W
            return String.valueOf(value_mode_W);
        } else {
            int value_K = value_mode_W / numberK;
            if (value_K > 0) {
                return value_W + "." + value_K + "万";
            } else {
                return value_W + "万";
            }
        }
    }

    public static String getImgURL(String url) {
        return url;

    }

    public static final long INTERVAL = 500L; // 防止连续点击的时间间隔
    private static long lastClickTime = 0L; // 上一次点击的时间

    public static boolean filter() {
        long time = System.currentTimeMillis();
        if ((time - lastClickTime) > INTERVAL) {
            lastClickTime = time;
            return false;
        }
        lastClickTime = time;
        return true;
    }

    /**
     * 处理double位数显示异常问题
     */
    public static double handleDouble(double value) {
        BigDecimal bg = new BigDecimal(value);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 删除所有的空格
     */
    public static String trimAllblank(String value) {
        return value.replaceAll(" ", "");
    }

    /**
     * 删除所有的空格
     */
    public static boolean isEmpty(String value) {
        if (value == null) return false;
        return TextUtils.isEmpty(trimAllblank(value));
    }

    /**
     * 判断是不是正确的身份证号码
     */
    public static boolean isIdcard(String idcardNo) {
        return !CommonUtils.StringNotNull(IDCard.isEnble(idcardNo));
    }

    public static String transformArrayToString(List<String> array) {

        String arrayString = "";
        if (array == null || array.isEmpty()) {

            return arrayString;
        }
        for (int index = 0; index < array.size(); index++) {

            if (index != array.size() - 1) {

                arrayString = arrayString + array.get(index) + ",";
            } else {

                arrayString = arrayString + array.get(index);
            }
        }
        return arrayString;
    }

    /**
     * 跳转activity
     */
    public static final String LINK_URL_HEAD = "zallgo://";

    public static boolean isAppRunBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (tasks != null && !tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 动态计算字符长度
     */
    public static int calculateStringLength(String etstring) {
        char[] ch = etstring.toCharArray();
        int varlength = 0;
        for (int i = 0; i < ch.length; i++) {
            //加入中文标点范围
            if ((ch[i] >= 0x2E80 && ch[i] <= 0xFE4F) || (ch[i] >= 0xA13F && ch[i] <= 0xAA40) || ch[i] >= 0x80) { // 中文字符范围0x4e00 0x9fbb
                varlength = varlength + 2;
            } else {
                varlength++;
            }
        }
        return varlength;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String replaceNull(String str) {
        return (TextUtils.isEmpty(str) || "null".equals(str)) ? "" : str;
    }

    public static boolean StringNotNull(String ss) {
        return !TextUtils.isEmpty(ss);
    }

    public static boolean StringNotNull(String... ss) {
        boolean bb = true;
        if (ss != null && ss.length > 0) {
            for (String k : ss) {
                bb = bb && StringNotNull(k);
            }
        } else {
            return false;
        }
        return bb;
    }

    public static boolean ListNotNull(List list) {
        return (list != null && list.size() > 0);
    }

    public static boolean MapNotNull(Map map) {
        return (map != null && map.size() > 0);
    }

    /**
     * 邮箱校验
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email != null && !TextUtils.isEmpty(email)) {
            String str = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
            Pattern p = Pattern.compile(str);
            Matcher m = p.matcher(email);
            return m.matches();
        }
        return false;
    }


    /**
     * 测量view高度
     *
     * @param child
     * @return
     */
    public static View measureViwe(View child) {
        if (child != null) {
            int intw = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int inth = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            child.measure(intw, inth);
        }
        return child;
    }

//
//    /**
//     * 获取照片URL字符串
//     *
//     * @param selImageList
//     * @return
//     */
//    public static String getCameraUrl(ArrayList<ImageItem> selImageList) {
//        StringBuilder sb = new StringBuilder();
//        if (CommonUtils.ListNotNull(selImageList)) {
//            for (int i = 0; i < selImageList.size(); i++) {
//                ImageItem imageItem = null;
//                try {
//                    imageItem = selImageList.get(i);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (imageItem != null) {
//                    String url = imageItem.getPath();
//                    if (CommonUtils.StringNotNull(url)) {
//                        if (i == selImageList.size() - 1) {
//                            sb.append(url);
//                        } else {
//                            sb.append(url + ",");
//                        }
//                    }
//                }
//            }
//        }
//        return sb.toString();
//    }

//    /**
//     * 获取照片URL集合
//     *
//     * @param url
//     * @return
//     */
//    public static ArrayList<ImageItem> getCameraList(String url) {
//        ArrayList<ImageItem> selImageList = new ArrayList<>();
//        if (CommonUtils.StringNotNull(url)) {
//            String[] split = url.split(",");
//            if (split.length > 0) {
//                for (String s : split) {
//                    ImageItem imageItem = new ImageItem();
//                    imageItem.setPath(s);
//                    selImageList.add(imageItem);
//                }
//            }
//        }
//        return selImageList;
//    }

    public static String hideMobileNum(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return "";
        }
        return mobile.replaceAll("(\\d{3})\\d{6}(\\d{1,2})", "$1*****$2");
    }

    /**
     * 是否安装微信
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 是否安装QQ
     *
     * @param context
     * @return
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 减去空格
     *
     * @param str
     * @return
     */
    public static String removeSpace(String str) {
        if (!TextUtils.isEmpty(str)) {
            str = str.replaceAll("\\" +
                    "s*", "");
        }
        return str;
    }


    /**
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} to a fullscreen opaque
     * Activity.
     * <p/>
     * Call this whenever the background of a translucent Activity has changed
     * to become opaque. Doing so will allow the {@link android.view.Surface} of
     * the Activity behind to be released.
     * <p/>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    public static void convertActivityFromTranslucent(Activity activity) {
        try {
            Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
            method.setAccessible(true);
            method.invoke(activity);
        } catch (Throwable t) {
        }
    }

    /**
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} back from opaque to
     * translucent following a call to
     * {@link #convertActivityFromTranslucent(Activity)} .
     * <p/>
     * Calling this allows the Activity behind this one to be seen again. Once
     * all such Activities have been redrawn
     * <p/>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    public static void convertActivityToTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            convertActivityToTranslucentAfterL(activity);
        } else {
            convertActivityToTranslucentBeforeL(activity);
        }
    }

    /**
     * Calling the convertToTranslucent method on platforms before Android 5.0
     */
    public static void convertActivityToTranslucentBeforeL(Activity activity) {
        try {
            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                }
            }
            Method method = Activity.class.getDeclaredMethod("convertToTranslucent",
                    translucentConversionListenerClazz);
            method.setAccessible(true);
            method.invoke(activity, new Object[]{
                    null
            });
        } catch (Throwable t) {
        }
    }

    /**
     * Calling the convertToTranslucent method on platforms after Android 5.0
     */
    private static void convertActivityToTranslucentAfterL(Activity activity) {
        try {
            Method getActivityOptions = Activity.class.getDeclaredMethod("getActivityOptions");
            getActivityOptions.setAccessible(true);
            Object options = getActivityOptions.invoke(activity);

            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                }
            }
            Method convertToTranslucent = Activity.class.getDeclaredMethod("convertToTranslucent",
                    translucentConversionListenerClazz, ActivityOptions.class);
            convertToTranslucent.setAccessible(true);
            convertToTranslucent.invoke(activity, null, options);
        } catch (Throwable t) {
        }
    }


    /**
     * 千换算
     *
     * @param number
     * @return
     */
    public static String getThousandNumber(int number) {
        String s = "";
        if (number > 0) {
            if (number >= 1000000) {
                s = deleteZero(MoneyUtils.mDecimalFormat(number / 1000000f, "0.0")) + "m";
            } else if (number >= 1000) {
                s = deleteZero(MoneyUtils.mDecimalFormat(number / 1000f, "0.0")) + "k";
            } else {
                s = String.valueOf(number);
            }
        }
        return s;
    }

    private static String deleteZero(String s) {
        if (s.indexOf(".") > 0) {
            // 正则表达
            s = s.replaceAll("0+?$", "");// 去掉后面无用的零;
            s = s.replaceAll("[.]$", "");// 如小数点后面全是零则去
        }
        return s;
    }


    public static String fixThree(String number) {
        if (CommonUtils.StringNotNull(number)) {
            if (number.length() == 1) {
                number = "00" + number;
            } else if (number.length() == 2) {
                number = "0" + number;
            }
        }
        return number;
    }

    public static String fixImageUrl(String url) {
        if (StringNotNull(url)) {
            return new StringBuilder("[IMG]").append(url).append("[/IMG]").toString();
        }
        return url;
    }

    public static String getRunningActivityName(Context context) {
        if (context == null)
            return "";
        ActivityManager activityManager = (ActivityManager) (context.getSystemService(Context.ACTIVITY_SERVICE));
        return activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
    }


    public static void setUserLevel(ImageView imageView, int level) {
        if (level == 2) {//官方
            imageView.setImageResource(R.drawable.icon_official);
            imageView.setVisibility(View.VISIBLE);
        } else if (level == 1) {//黄V
            imageView.setImageResource(R.drawable.icon_vip);
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }


    public static void setSearchUserLevel(ImageView imageView, int level) {
        if (level == 2) {//官方
            imageView.setImageResource(R.drawable.icon_search_official);
            imageView.setVisibility(View.VISIBLE);
        } else if (level == 1) {//黄V
            imageView.setImageResource(R.drawable.icon_search_vip);
            imageView.setVisibility(View.VISIBLE);
        } else {
            imageView.setVisibility(View.INVISIBLE);
        }
    }


    public static void setInitFragmentData(List<PageBaseFragment> mDataList, int position) {
        if (CommonUtils.ListNotNull(mDataList)) {
            PageBaseFragment fragment = null;
            try {
                fragment = mDataList.get(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (fragment != null) {
                fragment.setInitData();
            }
        }
    }


    public static ValueAnimator getValueShow(View view, int height) {
        return getValueShow(view, height, 1000);
    }

    public static ValueAnimator getValueShow(View view, int height, long duration) {
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animatedValue = (int) valueAnimator.getAnimatedValue();
                view.setAlpha((animatedValue / 100f));
                view.getLayoutParams().height = (int) ((animatedValue / 100f) * height);
                view.requestLayout();
            }
        });
        return animator;
    }

    public static ValueAnimator getValueHidden(View view, int height) {
        return getValueHidden(view, height, 1000);
    }

    public static ValueAnimator getValueHidden(View view, int height, long duration) {

        ValueAnimator animator = ValueAnimator.ofInt(100, 0);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animatedValue = (int) valueAnimator.getAnimatedValue();
                view.setAlpha((animatedValue / 100f));
                view.getLayoutParams().height = (int) ((animatedValue / 100f) * height);
                view.requestLayout();
            }
        });
        return animator;
    }

    public static String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();

        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    public final static float raid = 0.1f;

    public static Bitmap toBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, (int) (canvas.getWidth() * raid), (int) (canvas.getHeight() * raid));
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap decodeResource(Context context, int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static List<ImageInfo> getImageInfo(List<String> list) {
        List<ImageInfo> imageInfoList = new ArrayList<>();
        if (CommonUtils.ListNotNull(list)) {
            for (String s : list) {
                ImageInfo imageInfo = new ImageInfo();
                imageInfo.setThumbnailUrl(s);
                imageInfo.setOriginUrl(getOriginUrl(s));
                imageInfoList.add(imageInfo);
            }
        }
        return imageInfoList;
    }

    public static String getOriginUrl(String url) {
        if (CommonUtils.StringNotNull(url)) {
            if (url.contains("?")) {
                String[] split = url.split("\\?");
                if (split.length >= 2) {
                    url = split[0] + StringConstants.RELEASE_IMAGE_ORIGINAL_FIX;
                }
            } else if (url.endsWith(".w320.jpg")) {
                url = url.replace(".w320.jpg", "");
            }
        }
        return url;
    }

    /**
     * 是否有原图,并且宽和高超过500
     *
     * @param url
     * @return
     */
    public static boolean isOriginUrl(String url) {
        if (CommonUtils.StringNotNull(url) && url.contains("?")) {
            try {
                String[] split = url.split("\\?");
                if (split.length >= 2) {
                    String s = split[1];
                    String[] strings = s.split("_");
                    if (strings.length >= 2) {
                        if (NumberUtils.stringToInt(strings[0]) >= NumberConstants.CHECK_PIC_ORIGIN_SIZE &&
                                NumberUtils.stringToInt(strings[1]) >= NumberConstants.CHECK_PIC_ORIGIN_SIZE) {
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static MainHomeData getDataByPosition(List<MainHomeData> list, int position) {
        if (CommonUtils.ListNotNull(list)) {
            MainHomeData mainHomeData = null;
            try {
                mainHomeData = list.get(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mainHomeData != null) {
                return mainHomeData;
            }
        }
        return null;
    }

    public static ForumCommListBean getForumByPosition(List<ForumCommListBean> list, int position) {
        if (CommonUtils.ListNotNull(list)) {
            ForumCommListBean forumCommListBean = null;
            try {
                forumCommListBean = list.get(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (forumCommListBean != null) {
                return forumCommListBean;
            }
        }
        return null;
    }

    public static void checkImagePreview(Context context, List<String> list, int index) {
        List<ImageInfo> imageInfo = CommonUtils.getImageInfo(list);
        if (!CommonUtils.ListNotNull(imageInfo)) {
            LogUtils.d("图片未知");
            return;
        }
        ImagePreview.getInstance().setContext(context)
                .setImageInfoList(imageInfo)
                .setIndex(index)
                // 是否启用点击图片关闭。默认启用
//                .setEnableClickClose(true)
                // 是否显示关闭页面按钮，在页面左下角。默认不显示
//                .setShowCloseButton(false)
                // 保存的文件夹名称，会在SD卡根目录进行文件夹的新建。
                .setFolderName("Download/marksix")
                // 是否显示下载按钮，在页面右下角。默认显示
                .setShowDownButton(false)
                // 是否启用上拉/下拉关闭。默认不启用
                .setEnableDragClose(true)
                // 加载策略，默认为手动模式 仅加载原图
                .setLoadStrategy(ImagePreview.LoadStrategy.AlwaysOrigin)
////                // 设置查看原图时的百分比样式：库中带有一个样式：ImagePreview.PROGRESS_THEME_CIRCLE_TEXT，使用如下：
//                .setProgressLayoutId(ImagePreview.PROGRESS_THEME_CIRCLE_TEXT, new OnOriginProgressListener() {
//                    @Override
//                    public void progress(View parentView, int progress) {
//                        LogUtils.d("progress: " + progress);
//
//                        // 需要找到进度控件并设置百分比，回调中的parentView即传入的布局的根View，可通过parentView找到控件：
//                        ProgressBar progressBar = parentView.findViewById(R.id.sh_progress_view);
//                        TextView textView = parentView.findViewById(R.id.sh_progress_text);
//                        progressBar.setProgress(progress);
//                        String progressText = progress + "111%";
//                        textView.setText(progressText);
//                    }
//
//                    @Override
//                    public void finish(View parentView) {
//                        LogUtils.d("finish: ");
//                    }
//                })
                // 缩放动画时长，单位ms
//                .setZoomTransitionDuration(400)
                //失败图
                .setErrorPlaceHolder(R.drawable.default_image)
                .start();
    }


    public static ArrayList<String> getFaceUrls(String face) {
        ArrayList<String> list = new ArrayList<>();
        if (CommonUtils.StringNotNull(face) && isContainsHttp(face) && isOriginUrl(face)) {
            list.add(face);
        }
        return list;
    }

    public static ArrayList<String> getGalleryUrls(String url) {
        ArrayList<String> list = new ArrayList<>();
        if (CommonUtils.StringNotNull(url) && isContainsHttp(url)) {
            list.add(url);
        }
        return list;
    }

    public static boolean isContainsHttp(String s) {
        if (CommonUtils.StringNotNull(s)) {
            String ss = s.toLowerCase();
            if (ss.startsWith("http") || ss.startsWith("https")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 他人详情变更
     *
     * @param data
     * @param detailData
     */
    public static void commListDataChange(MainHomeData data, UserPostCenterData detailData) {
        String userId = data.getUser_Id();
        if (detailData != null) {
            if (CommonUtils.StringNotNull(userId)) {
                UserPostCenterMember member = detailData.getMember();
                if (member != null) {
                    if (userId.equals(member.getId())) {
                        detailData.setIs_look(data.getLook_status());
                    }

                }
            }
        }
    }

    /**
     * 帖子详情变更
     *
     * @param data
     * @param detailData
     */
    public static void commListDataChange(MainHomeData data, DetailData detailData) {
        String id = data.getId();
        String userId = data.getUser_Id();
        boolean onlyFavorites = data.isOnlyFavorites();
        if (detailData != null) {
            if (onlyFavorites) {
                if (CommonUtils.StringNotNull(id) && id.equals(detailData.getId())) {
                    int favorites_num = detailData.getFavorites_num();
                    detailData.setFavorites_num(--favorites_num);
                    detailData.setFav_status(data.getFav_status());
                }
            } else {
                if (CommonUtils.StringNotNull(id) && id.equals(detailData.getId())) {
                    detailData.setReply_num(data.getReply_Num());
                    detailData.setLike_status(data.getLike_status());
                    detailData.setLike_num(data.getLike_Num());
                    detailData.setFavorites_num(data.getFavorites_Num());
                    detailData.setFav_status(data.getFav_status());
                }
                if (CommonUtils.StringNotNull(userId) && userId.equals(data.getUser_Id())) {
                    detailData.setLook_status(data.getLook_status());
                }
            }
        }
    }

    /**
     * 详情页获取Event实体
     *
     * @param detailData
     * @return
     */
    public static MainHomeData getMainHomeData(DetailData detailData) {
        MainHomeData data = new MainHomeData();
        data.setId(detailData.getId());
        data.setUser_Id(detailData.getUser_Id());
        data.setReply_Num(detailData.getReply_num());
        data.setLike_status(detailData.getLike_status());
        data.setLike_Num(detailData.getLike_num());
        data.setFavorites_Num(detailData.getFavorites_num());
        data.setFav_status(detailData.getFav_status());
        data.setLook_status(detailData.getLook_status());
        return data;
    }

    /**
     * 详情页获取Event实体
     *
     * @param detailData
     * @return
     */
    public static MainHomeData getMainHomeData(UserPostCenterData detailData) {
        MainHomeData data = new MainHomeData();
        UserPostCenterMember member = detailData.getMember();
        if (member != null) {
            String id = member.getId();
            int is_look = detailData.getIs_look();
            data.setId(null);//id设置null,只改变关注状态
            data.setUser_Id(id);
            data.setLook_status(is_look);
        }
        return data;
    }

    /**
     * 我的关注页获取Event实体
     *
     * @return
     */
    public static MainHomeData getMainHomeData(String userId, int is_look) {
        MainHomeData data = new MainHomeData();
        data.setId(null);//id设置null,只改变关注状态
        data.setUser_Id(userId);
        data.setLook_status(is_look);
        return data;
    }

    /**
     * 我的收藏页获取Event实体,取消收藏
     *
     * @return
     */
    public static MainHomeData getMainHomeData(String id) {
        MainHomeData data = new MainHomeData();
        data.setId(id);
        data.setOnlyFavorites(true);
        return data;
    }

    /**
     * 帖子列表变更
     *
     * @param data
     * @param list
     */
    public static void commListDataChange(MainHomeData data, List<MainHomeData> list) {
        String id = data.getId();
        String userId = data.getUser_Id();
        boolean onlyFavorites = data.isOnlyFavorites();
        for (MainHomeData homeData : list) {
            if (homeData != null) {
                if (onlyFavorites) {
                    if (CommonUtils.StringNotNull(id) && id.equals(homeData.getId())) {
                        int favorites_num = homeData.getFavorites_Num();
                        homeData.setFavorites_Num(--favorites_num);
                        homeData.setFav_status(data.getFav_status());
                    }
                } else {
                    if (CommonUtils.StringNotNull(id) && id.equals(homeData.getId())) {
                        homeData.setReply_Num(data.getReply_Num());
                        homeData.setLike_status(data.getLike_status());
                        homeData.setLike_Num(data.getLike_Num());
                        homeData.setFavorites_Num(data.getFavorites_Num());
                        homeData.setFav_status(data.getFav_status());
                    }
                    if (CommonUtils.StringNotNull(userId) && userId.equals(homeData.getUser_Id())) {
                        homeData.setLook_status(data.getLook_status());
                    }
                }
            }
        }
    }

    /**
     * 帖子列表变更
     *
     * @param data
     * @param list
     */
    public static boolean commListDataChange2(MainHomeData data, List<MainHomeData> list) {
        String id = data.getItem_Id();
        if (!CommonUtils.StringNotNull(id)) {
            id = data.getId();
        }
        String userId = data.getUser_Id();
        boolean onlyFavorites = data.isOnlyFavorites();
        boolean isChanged = false;
        for (MainHomeData homeData : list) {
            if (homeData != null) {
                if (onlyFavorites) {
                    if (CommonUtils.StringNotNull(id) && id.equals(homeData.getItem_Id())) {
                        int favorites_num = homeData.getFavorites_Num();
                        homeData.setFavorites_Num(--favorites_num);
                        homeData.setFav_status(data.getFav_status());
                        isChanged = true;
                    }
                } else {
                    if (CommonUtils.StringNotNull(id) && id.equals(homeData.getItem_Id())) {
                        homeData.setReply_Num(data.getReply_Num());
                        homeData.setLike_status(data.getLike_status());
                        homeData.setLike_Num(data.getLike_Num());
                        homeData.setFavorites_Num(data.getFavorites_Num());
                        homeData.setFav_status(data.getFav_status());
                        isChanged = true;
                    }
                    if (CommonUtils.StringNotNull(userId) && userId.equals(homeData.getUser_Id())) {
                        homeData.setLook_status(data.getLook_status());
                        isChanged = true;
                    }
                }
            }
        }
        return isChanged;
    }

    /**
     * 帖子列表变更(我的评论列表)
     *
     * @param data
     * @param list
     */
    public static boolean ConcernCommentListDataChange(MainHomeData data, List<MainHomeData> list) {
        String id = data.getId();
        boolean isChanged = false;
        for (MainHomeData homeData : list) {
            if (homeData != null && homeData.getReply() != null) {
                MainHomeData.ReplyBean reply = homeData.getReply();
                if (CommonUtils.StringNotNull(id) && id.equals(reply.getId())) {
                    reply.setReply_Num(data.getReply_Num());
                    reply.setLike_status(data.getLike_status());
                    reply.setLike_Num(data.getLike_Num());
                    isChanged = true;
                }
            }
        }
        return isChanged;
    }

    /**
     * 帖子列表变更(我的评论列表)
     *
     * @param detail
     * @param list
     */
    public static void ConcernCommentListDataChange(CommentsListDetail detail, List<MainHomeData> list) {
        String id = detail.getId();
        boolean onlyReply_num = detail.isOnlyReply_Num();
        for (MainHomeData homeData : list) {
            if (homeData != null && homeData.getReply() != null) {
                MainHomeData.ReplyBean reply = homeData.getReply();
                if (CommonUtils.StringNotNull(id) && id.equals(reply.getId())) {
                    if (onlyReply_num) {
                        reply.setReply_Num(detail.getReply_Num());
                    } else {
                        reply.setReply_Num(detail.getReply_Num());
                        reply.setLike_status(detail.getLike_status());
                        reply.setLike_Num(detail.getLike_Num());
                    }
                }
            }
        }
    }

    /**
     * 我的收藏/点赞列表 删除多个或删除所有,推荐页及关注页数据变化
     *
     * @param ids  变更集合 [1,2,3],正常删除  [-1],删除所有
     * @param type 1,收藏 2,评论 3,点赞 4,历史
     * @param list 当前列表数据
     */
    public static void deleteLikeAndFavorDataChange(List<String> ids, int type, List<MainHomeData> list) {
        for (MainHomeData data : list) {
            if (data != null) {
                if ("-1".equals(ids.get(0))) {//取消所有
                    deleteData(data, type, true);
                } else {//取消单个
                    for (String id : ids) {
                        if (CommonUtils.StringNotNull(id)) {
                            String mId = data.getItem_Id();
                            if (!CommonUtils.StringNotNull(mId)) {
                                mId = data.getId();
                            }
                            if (id.equals(mId)) {
                                deleteData(data, type, false);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 取消收藏/点赞
     *
     * @param data
     * @param type
     */
    private static void deleteData(MainHomeData data, int type, boolean isDeleteAll) {
        if (type == 1) {//取消收藏
            int favorites_num = data.getFavorites_Num();
            data.setFavorites_Num(isDeleteAll ? 0 : --favorites_num);
            data.setFav_status(0);
        } else if (type == 3) {//取消点赞
            int like_num = data.getLike_Num();
            data.setLike_Num(isDeleteAll ? 0 : --like_num);
            data.setLike_status(0);
        } else if (type == 2) {//评论
            int reply_num = data.getReply_Num();
            data.setReply_Num(isDeleteAll ? 0 : --reply_num);
        }
    }

    /**
     * 我的关注变更
     *
     * @param data
     * @param list
     */
    public static void mineConcernDataChange(MainHomeData data, List<MineConcernData> list) {
        String user_id = data.getUser_Id();
        for (MineConcernData bean : list) {
            if (bean != null) {
                if (CommonUtils.StringNotNull(user_id) && user_id.equals(bean.getMember_Id())) {
                    bean.setLook_status(data.getLook_status());
                }
            }
        }
    }

    /**
     * 搜索用户关注变更
     *
     * @param data
     * @param list
     */
    public static void searchConcernDataChange(MainHomeData data, List<MineConcernData> list) {
        String user_id = data.getUser_Id();
        for (MineConcernData bean : list) {
            if (bean != null) {
                if (CommonUtils.StringNotNull(user_id) && user_id.equals(bean.getId())) {
                    bean.setLook_status(data.getLook_status());
                }
            }
        }
    }


    /**
     * 我的收藏变更
     *
     * @param data
     * @param list
     */
    public static void mineCollectDataChange(MainHomeData data, List<MainHomeData> list) {
        String id = data.getId();
        Iterator<MainHomeData> iterator = list.iterator();
        while (iterator.hasNext()) {
            MainHomeData bean = iterator.next();
            if (bean != null) {
                if (CommonUtils.StringNotNull(id) && id.equals(bean.getId())) {
                    iterator.remove();
                }
            }
        }
    }


    /**
     * 回复评论变更
     *
     * @param detail
     * @param list
     */
    public static void commListDataChange(CommentsListDetail detail, Collection<DetailCommentData> list) {
        String id = detail.getId();
        for (DetailCommentData commentData : list) {
            if (commentData != null) {
                if (CommonUtils.StringNotNull(id) && id.equals(commentData.getId())) {
                    commentData.setLike_Num(detail.getLike_Num());
                    commentData.setLike_status(detail.getLike_status());
                    commentData.setReply_Num(detail.getReply_Num());
                    break;
                }
            }
        }
    }

    /**
     * 获取全部分类
     *
     * @return
     */
    public static SelectClassifyData getDefault() {
        return new SelectClassifyData("0", "全部", "", "", 0, false, false);
    }


    /**
     * 论坛列表获取实体
     *
     * @param list
     * @param position
     */
    public static DetailCommentData getForumData(List<DetailCommentData> list, int position) {
        if (CommonUtils.ListNotNull(list)) {
            DetailCommentData forumCommListBean = null;
            try {
                forumCommListBean = list.get(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (forumCommListBean != null) {
                return forumCommListBean;
            }
        }
        return null;
    }

    /**
     * result=<p>2019年香港六合三月份必开特码：02.04.<span style="color: rgb(255, 0, 0);">08</span>
     */
    public static String getForumHtml(String s) {
        if (s.contains("\"")) {
            s = s.replaceAll("\"", "\\\'");
        }
        return s;
    }

    /**
     * 设置关注
     *
     * @param look_status
     */
    public static void setLookStatus(TextView mTvConcern, int look_status) {
        // 是否关注，0:未关注;1:已关注 2:自己
        if (look_status == 1) {
            mTvConcern.setVisibility(View.VISIBLE);
            mTvConcern.setText("已关注");
            mTvConcern.setTextColor(0xff999999);
            mTvConcern.setBackgroundResource(R.drawable.shape_unconcern);
        } else if (look_status == 2) {
            mTvConcern.setVisibility(View.INVISIBLE);
        } else if (look_status == 0) {
            mTvConcern.setVisibility(View.VISIBLE);
            mTvConcern.setText("关注");
            mTvConcern.setTextColor(0xffffffff);
            mTvConcern.setBackgroundResource(R.drawable.shape_concern);
        } else if (look_status == -1) {
            mTvConcern.setVisibility(View.VISIBLE);
            mTvConcern.setTextColor(0x00000000);
        }
    }

    /**
     * 设置点赞颜色及图标
     *
     * @param mTvPraise
     * @param like_status
     */
    public static void setPraiseStatus(Context context, TextView mTvPraise, int like_status) {
        if (like_status == 1) {
            mTvPraise.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            UIUtils.setLeftDrawable(context, mTvPraise, R.drawable.praise_checked);
        } else {
            mTvPraise.setTextColor(context.getResources().getColor(R.color.black_333));
            UIUtils.setLeftDrawable(context, mTvPraise, R.drawable.praise_uncheck);
        }
    }

    /**
     * 论坛设置点赞颜色及图标
     *
     * @param mTvPraise
     * @param like_status
     */
    public static void setForumPraiseStatus(Context context, TextView mTvPraise, int like_status) {
        if (like_status == 1) {
            mTvPraise.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            UIUtils.setLeftDrawable(context, mTvPraise, R.drawable.praise_checked_forum);
        } else {
            mTvPraise.setTextColor(context.getResources().getColor(R.color.grey_999));
            UIUtils.setLeftDrawable(context, mTvPraise, R.drawable.praise_uncheck_forum);
        }
    }


    /**
     * 设置踩颜色及图标
     *
     * @param fav_status
     */
    public static void setTrampleStatus(Context context, TextView mTvCollect, int fav_status) {
        if (fav_status == 1) {
            mTvCollect.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            UIUtils.setLeftDrawable(context, mTvCollect, R.drawable.trample_ok);
        } else {
            mTvCollect.setTextColor(context.getResources().getColor(R.color.black_333));
            UIUtils.setLeftDrawable(context, mTvCollect, R.drawable.trample_on);
        }
    }

    /**
     * 设置收藏颜色及图标
     *
     * @param fav_status
     */
    public static void setCollectStatus(Context context, TextView mTvCollect, int fav_status) {
        if (fav_status == 1) {
            mTvCollect.setTextColor(context.getResources().getColor(R.color.yellow_efa));
            UIUtils.setLeftDrawable(context, mTvCollect, R.drawable.collect_checked);
        } else {
            mTvCollect.setTextColor(context.getResources().getColor(R.color.black_333));
            UIUtils.setLeftDrawable(context, mTvCollect, R.drawable.collect_uncheck);
        }
    }

    /**
     * 详情页回复实体
     *
     * @param context
     * @param id
     * @param content
     * @return
     */
    public static DetailCommentData getDetailComment(Context context, String id, String content) {
        DetailCommentData data = new DetailCommentData();
        data.setId(id);
        data.setUser_Id(SPUtil.getUserId(context));
        data.setFace(SPUtil.getUserPhoto(context));
        data.setNickname(SPUtil.getNickName(context));
        data.setContent(content);
        data.setAdd_Time("刚刚");
        return data;
    }

    /**
     * 列表移除帖子
     *
     * @param list
     * @param id
     */
    public static void getDeleteInvitation(List<MainHomeData> list, String id) {
        if (CommonUtils.ListNotNull(list)) {
            Iterator<MainHomeData> iterator = list.iterator();
            while (iterator.hasNext()) {
                MainHomeData bean = iterator.next();
                if (bean != null) {
                    if (CommonUtils.StringNotNull(id) && id.equals(bean.getId())) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    /**
     * 列表移除置顶帖子
     *
     * @param list
     */
    public static void setDeleteTop(List<MainHomeData> list) {
        if (CommonUtils.ListNotNull(list)) {
            Iterator<MainHomeData> iterator = list.iterator();
            while (iterator.hasNext()) {
                MainHomeData bean = iterator.next();
                if (bean != null) {
                    if (bean.getIs_Top() == 1) {
                        iterator.remove();
                    }
                }
            }
        }
    }


    /**
     * 列表移除上次浏览位置
     *
     * @param list
     */
    public static void deleteLastTime(List<MainHomeData> list) {
        if (CommonUtils.ListNotNull(list)) {
            Iterator<MainHomeData> iterator = list.iterator();
            while (iterator.hasNext()) {
                MainHomeData bean = iterator.next();
                if (bean != null) {
                    bean.setGoneDecoration(false);
                    if (bean.getLastLookTime() > 0) {
                        iterator.remove();
                    }
                }
            }
        }
    }


    /**
     * 头条个人签名截取
     *
     * @param str
     * @return
     */
    public static String CommHandleText(String str) {
        return handleText(str, 22);
    }

    /**
     * 昵称截取
     *
     * @param str
     * @return
     */
    public static String CommHandleText2(String str) {
        return handleText(str, 24);
    }


    /**
     * 字符长度限制，中文2个字符 ，英文1个字符
     *
     * @param str
     * @param maxLen
     * @return
     */
    public static String handleText(String str, int maxLen) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        int count = 0;
        int endIndex = 0;
        for (int i = 0; i < str.length(); i++) {
            char item = str.charAt(i);
            if (item < 128) {
                count = count + 1;
            } else {
                count = count + 2;
            }
            if (maxLen == count || (item >= 128 && maxLen + 1 == count)) {
                endIndex = i;
            }
        }
        if (count <= maxLen) {
            return str;
        } else {

            return str.substring(0, endIndex) + "...";
        }

    }


    /**
     * 通用点击背景设置
     *
     * @param textView
     * @param isClickable
     */
    public static void setCommClickable(TextView textView, boolean isClickable) {
        if (textView == null) {
            return;
        }
        GradientDrawable myGrad = (GradientDrawable) textView.getBackground();
        Resources resources = textView.getContext().getResources();
        if (resources == null) {
            return;
        }
        int color;
        if (isClickable) {
            color = resources.getColor(R.color.colorPrimary);
        } else {
            color = resources.getColor(R.color.grey_bcb);
        }
        myGrad.setColor(color);
        textView.setClickable(isClickable);
        textView.setEnabled(isClickable);
    }

    /**
     * 设置状态栏文字颜色
     *
     * @param activity
     * @param isDark
     */
    public static void setDecorTextViewColor(Activity activity, boolean isDark) {
        if (activity != null) {
            if (isDark) {
                //1、设置状态栏文字深色，同时保留之前的flag
                int originFlag = activity.getWindow().getDecorView().getSystemUiVisibility();
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                //2、清除状态栏文字深色，同时保留之前的flag
                int originFlag = activity.getWindow().getDecorView().getSystemUiVisibility();
                //使用异或清除SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                activity.getWindow().getDecorView().setSystemUiVisibility(0);
            }
        }
    }


    /**
     * 登录成功设置缓存
     *
     * @param context
     * @param response
     */
    public static void setLoginSuccessData(Context context, LoginData response) {
        if (context != null && response != null) {
            String userId = response.getId();
            SPUtil.setToken(context, response.getSafety());
            SPUtil.setUserId(context, userId);
            SPUtil.setUserPhoto(context, response.getFace());
            SPUtil.setUserAccount(context, response.getPhone());
            SPUtil.setNickName(context, response.getNickname());
            SPUtil.setInvitationCode(context, response.getUid());
            ClientInfo.initHeadParams(context);
//            PushUtils.removeAlias(context, userId);
        }
    }

    /**
     * 是否有外存卡
     *
     * @return
     */
    public static boolean isExistExternalStore() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }


    /**
     * EditText竖直方向是否可以滚动
     *
     * @param editText 需要判断的EditText
     * @return true：可以滚动   false：不可以滚动
     */
    public static boolean canVerticalScroll(EditText editText) {
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int scrollDifference = scrollRange - scrollExtent;

        if (scrollDifference == 0) {
            return false;
        }
        return (scrollY > 0) || (scrollY < scrollDifference - 1);
    }


    /**
     * 复制内容到剪贴板
     *
     * @param context
     * @param text
     */
    public static void copyText(Context context, String text) {
        if (CommonUtils.StringNotNull(text)) {
            ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("text", text);//text是内容
            cm.setPrimaryClip(clipData);
            ToastUtil.getInstance()._short(context, "复制成功");
        }
    }

    /**
     * 是否当天已经签到
     *
     * @param context
     * @return
     */
    public static boolean isCurrentDaySign(Context context) {
        TaskSignInData data = SPUtil.getTaskSignInTime(context);
        if (data != null) {
            if (SPUtil.getUserId(context).equals(data.userId)
                    && DateUtils.covertDateToString(System.currentTimeMillis())
                    .equals(DateUtils.covertDateToString(data.time))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取最后可见position
     *
     * @param lastVisiblePositions
     * @return
     */
    public static int findMax(int[] lastVisiblePositions) {
        int max = lastVisiblePositions[0];
        for (int value : lastVisiblePositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }


    /**
     * 开奖波色获取颜色
     *
     * @param index
     * @param s
     * @return
     */
    public static int[] getLotteryChartColorByString(int[] color, int index, String s) {
        if (CommonUtils.StringNotNull(s)) {
            if (s.contains("蓝")) {
                color[index] = R.color.blue_03a;
            } else if (s.contains("绿")) {
                color[index] = R.color.green_1db;
            } else {//红
                color[index] = R.color.colorPrimary;
            }
        }
        return color;
    }

    /**
     * 开奖路珠颜色
     * <p>
     * 0xff1c26ed;//蓝  蓝,大,双
     * 0xffed1c24;//红  红,小,单
     * 0xff217C00;//绿  绿
     *
     * @param string
     * @return
     */
    public static int getRoadBeadColor(String string) {
        int color = 0xff1c26ed;//蓝
        if (CommonUtils.StringNotNull(string)) {
            if (string.contains("单")) {
                color = 0xffed1c24;//红
            } else if (string.contains("小")) {
                color = 0xffed1c24;//红
            } else if (string.contains("红")) {
                color = 0xffed1c24;//红
            } else if (string.contains("双")) {
                color = 0xff1c26ed;//蓝
            } else if (string.contains("大")) {
                color = 0xff1c26ed;//蓝
            } else if (string.contains("蓝")) {
                color = 0xff1c26ed;//蓝
            } else if (string.contains("绿")) {
                color = 0xff217C00;//绿
            }
        }
        return color;
    }


    /**
     * 开奖波色获取颜色
     *
     * @param s
     * @return
     */
    public static String getLotteryColorByString(String s) {
        String color = "#e71129";
        if (CommonUtils.StringNotNull(s)) {
            if (s.contains("蓝")) {
                color = "#4150b5";
            } else if (s.contains("绿")) {
                color = "#229a20";
            }
        }
        return color;
    }


    /**
     * 获取开奖信息
     *
     * @param message
     * @return
     */
    public static LotteryRealTimeData getRealTimeDataByMessage(String message) {
        LotteryRealTimeData data = null;
        try {
            if (CommonUtils.StringNotNull(message)) {
                data = JSONUtils.fromJson(message, LotteryRealTimeData.class);
                if (data != null) {
                    List<String> lottery = data.getLottery();
                    if (CommonUtils.ListNotNull(lottery)) {
                        Iterator<String> iterator = lottery.iterator();
                        while (iterator.hasNext()) {
                            String next = iterator.next();
                            if (!CommonUtils.StringNotNull(next)) {
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    /**
     * 获取开奖信息
     *
     * @param message
     * @return
     */
    public static LotteryRealTimeEvent getRealTimeData(String message) {
        LotteryRealTimeEvent data = null;
        try {
            if (CommonUtils.StringNotNull(message)) {
                data = JSONUtils.fromJson(message, LotteryRealTimeEvent.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 设置当前开奖
     *
     * @param context
     * @param message
     */
    public static void setCurrentLottery(Context context, String message) {
        LotteryRealTimeData data = CommonUtils.getRealTimeDataByMessage(message);
        if (data != null) {
            int isOpen = data.getIsOpen();//0,晚上9点20 后台开始重置  1,准备开奖,弹框提醒  2,开球中   3,开奖结束
            if (isOpen != 3) {
                LotteryRealTimeEvent event = new LotteryRealTimeEvent(isOpen, data);
                SPUtil.setStringValue(context, SPUtil.LOTTERY_CURRENT, JSONUtils.toJson(event));
                return;
            }
        }
        SPUtil.setStringValue(context, SPUtil.LOTTERY_CURRENT, "");
    }


    /**
     * 首页推荐上次浏览位置
     *
     * @param lastTime
     * @return
     */
    public static String getLastTimeString(long lastTime) {
        String s = "刚刚看到这里  点击刷新";
        long current = System.currentTimeMillis();
        if (current > lastTime) {//更改系统时间判断
            long l = current - lastTime;
            if (l <= 600 * 1000) {//10分钟内
                s = "刚刚看到这里  点击刷新";
            } else if (l <= 3600 * 1000) {//一个小时内
                s = ((int) (l / 1000 / 60)) + "分钟前看到这里  点击刷新";
            } else if (l <= 3600 * 1000 * 24) {//一天内
                s = ((int) (l / 1000 / 60 / 60)) + "个小时前看到这里  点击刷新";
            } else {
                s = "以下是24小时前更新的内容  点击刷新";
            }
        }
        return s;
    }


    /**
     * 当天是否已经签到
     *
     * @param response
     * @return
     */
    public static boolean getTodayCheckIn(List<TaskCenterData.CheckinBean> response) {
        boolean isCheckIn = false;
        if (CommonUtils.ListNotNull(response)) {
            for (TaskCenterData.CheckinBean bean : response) {
                if (bean != null) {
                    if (bean.getToday() == 1) {
                        isCheckIn = bean.getStatus() == 1;
                    }
                }
            }
        }
        return isCheckIn;
    }


    /**
     * 获取到九点半开奖倒计时毫秒值
     *
     * @param now
     * @return
     */
    public static long getTodayNineTime(long now) {
        now *= 1000;
        String s = DateUtils.covertDateToSpanceStringToHMS(now);//2019-05-25 21:28:26
        String[] split = s.split(" ");
        long time = DateUtils.stringToLong(split[0] + " 21:30:00", DateUtils.SHORT_DATE_FORMAT_HOURS_MINUTE_SECOND);
        time -= now;
        if (time > 0) {
            return time;
        }
        return -1;
    }


}






