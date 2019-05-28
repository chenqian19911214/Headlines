package com.marksixinfo.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class DateUtils {
    public static String TIME_FORMAT_BIG_SPANCE = "yyyy-MM-dd HH:mm:ss";
    public static String TIME_FORMAT_NORMAL = "yyyy-MM-dd HH:mm:ss";
    public static String DATE_TIME_FORMAT = "yyyyMMddHHmmss";
    public static String SHORT_DATE_FORMAT = "yyyy.MM.dd";
    public static String SHORT_DATE_FORMAT1 = "yyyy-MM-dd";
    public static String SHORT_DATE_FORMAT_CHINESE = "yyyy年MM月dd日";
    public static String SHORT_TIME_FORMAT = "HH:mm";
    public static String SIMPLE_DATE_FORMAT = "yyyyMMdd";
    public static String FORMAT_MM_DD_HMS = "MM-dd HH:mm:ss";
    public static String TIME_FORMAT_NORMAL_NOSS = "yyyy-MM-dd HH:mm";
    public static String SHORT_DATE_FORMAT_HOURS_ = "yyyy.MM.dd HH:mm";
    public static String SHORT_DATE_FORMAT_HOURS_MINUTE_SECOND = "yyyy.MM.dd HH:mm:ss";
    public static String SHORT_DATE_FORMAT2 = "MM-dd";
    public static String DATE_FORMAT_HOURS = "HH:mm:ss";


    /**
     * if 日期是今天,显示 HH:mm
     * else 显示 "yyyy-MM-dd"
     * @param time
     * @return
     */
    public static String getIdentifyDate(long time){
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(SHORT_DATE_FORMAT1);
        String dateNowStr = sdf.format(d);
        String dateStr = covertDateToString(time,SHORT_DATE_FORMAT1);
        if(dateNowStr.equals(dateStr)){
            return covertDateToString(time,SHORT_TIME_FORMAT);
        }else{
            return covertDateToString(time,TIME_FORMAT_NORMAL_NOSS);
        }
    }

    /**
     * if 日期是今天,显示 HH:mm
     * else 显示 "yyyy-MM-dd HH:mm"
     * @param time
     * @return
     */
    public static String getIdentifyDateForHHMM(long time){
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(SHORT_DATE_FORMAT1);
        String dateNowStr = sdf.format(d);
        String dateStr = covertDateToString(time,SHORT_DATE_FORMAT1);
        if(dateNowStr.equals(dateStr)){
            return covertDateToString(time,SHORT_TIME_FORMAT);
        }else{
            return covertDateToString(time,SHORT_DATE_FORMAT1);
        }
    }

    public static Date StringToDate(String dateStr, String pattern) {
        if (dateStr != null) {
            DateFormat sdf = new SimpleDateFormat(pattern);
            Date date = null;
            try {
                date = sdf.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date;
        }
        return null;
    }

    /**
     * 时间转换为String
     *
     * @param date
     * @return
     */
    public static String covertDateToString(long date) {
        return covertDateToString(date, SHORT_DATE_FORMAT);
    }

    public static String covertDateToString1(long date) {
        return covertDateToString(date, SHORT_DATE_FORMAT1);
    }

    public static String covertDateToString2(long date) {
        return covertDateToString(date, SHORT_DATE_FORMAT2);
    }

    public static String covertDateToString3(long date) {
        return covertDateToString(date, DATE_FORMAT_HOURS);
    }


    public static String covertDateToString(long date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        return dateFormat.format(new Date(date));
    }

    /**
     * 带大空格的时间转换为String
     *
     * @param date
     * @return
     */
    public static String covertDateToSpanceString(long date) {
        return covertDateToString(date, TIME_FORMAT_NORMAL);
    }



    /**
     * yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String covertDateToSpanceString2(long date) {
        return covertDateToString(date, TIME_FORMAT_BIG_SPANCE);
    }

    /**
     * 带大空格的时间转换为String
     *
     * @param date
     * @return
     */
    public static String covertDateToSpanceStringNoSS(long date) {
        return covertDateToString(date, TIME_FORMAT_NORMAL_NOSS);
    }

    public static String covertDateToSpanceStringToDian(long date) {
        return covertDateToString(date, SHORT_DATE_FORMAT_HOURS_);
    }

    public static String covertDateToSpanceStringToHMS(long date) {
        return covertDateToString(date, SHORT_DATE_FORMAT_HOURS_MINUTE_SECOND);
    }


    /**
     * 获取前一天
     */
    public static Date getPreDay(Date date) {
        if (date != null) {

            long dateL = date.getTime() - 24 * 60 * 60 * 1000;
            return new Date(dateL);
        }
        return new Date();
    }

    /**
     * 获取指定后几天
     *
     * @param date
     * @return
     */
    public static Date getAfterDay(Date date, int afterDay) {
        if (date != null) {

            long dateL = date.getTime() + afterDay * 24 * 60 * 60 * 1000L;
            return new Date(dateL);
        }
        return new Date();
    }

    /**
     * 判断选择时间是否小于当前时间
     *
     * @param compareDate
     */
    @SuppressWarnings("deprecation")
    public static boolean dateLessToday(Date compareDate) {
        // Date currentDate=new Date();
        Calendar currentCal = Calendar.getInstance();
        Calendar compareCal = Calendar.getInstance();
        compareCal.setTime(compareDate);
        int year = currentCal.get(Calendar.YEAR)
                - compareCal.get(Calendar.YEAR);
        if (year < 0) {
            return false;
        }
        if (year > 0) {
            return true;
        }
        int diffMonth = currentCal.get(Calendar.MONTH)
                - compareCal.get(Calendar.MONTH);
        if (diffMonth < 0) {
            return false;
        }
        if (diffMonth > 0) {
            return true;
        }
        int diffDay = currentCal.get(Calendar.DAY_OF_MONTH)
                - compareCal.get(Calendar.DAY_OF_MONTH);
        return diffDay >= 0;
    }

    /**
     * @param today
     * @param endTime
     * @return false 开始时间大于结束时间 true 开始时间小于结束时间
     */
    public static boolean compareDate(Date today, Date endTime) {
        Calendar todayCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        todayCal.setTime(today);
        endCal.setTime(endTime);
        if (todayCal.get(Calendar.YEAR) - endCal.get(Calendar.YEAR) > 0) {
            return false;
        }
        if (todayCal.get(Calendar.MONTH) - endCal.get(Calendar.MONTH) > 0) {
            return false;
        }
        if (todayCal.get(Calendar.DAY_OF_MONTH)
                - endCal.get(Calendar.DAY_OF_MONTH) > 0) {
            return false;
        }
        return true;
    }

      public static boolean compareDateNew(Date today, Date endTime) {
//        Calendar todayCal = Calendar.getInstance();
//        Calendar endCal = Calendar.getInstance();
//        todayCal.setTime(today);
//        endCal.setTime(endTime);
        if (endTime.getYear() - today.getYear()> 0) {
            return false;
        }
        if (endTime.getMonth()- today.getMonth() > 0) {
            return false;
        }
        if (endTime.getDay()
                - today.getDay() > 0) {
            return false;
        }
        return false;
    }



    /**
     * 判断是否为订单查询是否跨度是否超过一个月
     */
    public static boolean timeStampMoreOneMonth(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int days = (int) ((endDate.getTime() - startDate.getTime()) / 60 / 60 / 24 / 1000) + 1;

        int maximum = getMonthLastDay(startCal.get(Calendar.YEAR),
                startCal.get(Calendar.MONTH));

        return days > maximum;
    }

    /**
     * 判断是否为订单查询是否跨度是否超过一年
     */
    public static boolean timeStampMoreOneYear(Date today, Date endDate) {

        Date temp = getOneYear(today);
        if (compareMyDate(temp ,endDate) == 1)
        {
            return true;
        }else {
            return false;
        }

    }
    public static int compareMyDate(Date d1, Date d2) {
        if (d2.getTime() >d1.getTime()) {
            return 1;
        } else if (d1.getTime() < d2.getTime()) {
            return -1;
        } else {// 相等
            return 0;
        }
    }

    //當前時間加1年
    public static Date getOneYear(Date today) {
        Calendar c = Calendar.getInstance();
        c.setTime(today);
        c.add(Calendar.YEAR, 1);
        return c.getTime();
    }

    /**
     * 得到指定月的天数
     */
    public static int getMonthLastDay(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month);
        a.set(Calendar.DATE, 1);// 把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);// 日期回滚一天，也就是最后一天
        return a.get(Calendar.DATE);
    }


    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年


    public static long getMilliSecondTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long millionSeconds = 0;
        try {
            millionSeconds = sdf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }// 毫秒
        return millionSeconds;
    }

    public static final String DATE_MODE_1 = "yyyy-MM-dd HH:mm:ss";

    public static String getFormatedDateTime(String pattern, long dateTime) {
        SimpleDateFormat sDateFormat = new SimpleDateFormat(pattern);
        return sDateFormat.format(new Date(dateTime + 0));
    }

    /**
     *  String转毫秒
     * @param s
     * @param format
     * @return
     */
    public static  long stringToLong(String s, String format){
        if (CommonUtils.StringNotNull(s,format)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                try {
                    Date parse = simpleDateFormat.parse(s);
                    return parse.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
        }
        return 0;
    }

    /**
     * 得到当前时间
     *
     * @return
     */
    public static String getCurrentDate(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date());

    }

}
