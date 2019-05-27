package com.marksixinfo.widgets;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

import com.marksixinfo.MarkSixApp;
import com.marksixinfo.bean.ClientInfo;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.UIUtils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: Administrator
 * @Date: 2019/4/8 0008 17:09
 * @Description:
 */
public class SpannableStringUtils {

    int start = 0;
    StringBuffer stringBuffer = new StringBuffer();

    public HashMap<SpannableStringStyle, Object> getStyles() {
        return styles;
    }

    HashMap<SpannableStringStyle, Object> styles = new HashMap<>();

    public void addText(int textSize, int textColor, String textStr) {
        if (CommonUtils.StringNotNull(textStr)) {
            if (textSize > 0) {
                AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(UIUtils.dip2px(ClientInfo.scale, textSize));
                styles.put(new SpannableStringStyle(start, start + textStr.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE), sizeSpan);
            }
            if (textColor != -1) {
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(textColor);
                styles.put(new SpannableStringStyle(start, start + textStr.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE), foregroundColorSpan);
            }
            stringBuffer.append(textStr);
            start = start + textStr.length();
        }
    }

    public void addText(int textSize, int textColor, String textStr, ShowAllSpan.OnAllSpanClickListener listener) {
        if (CommonUtils.StringNotNull(textStr)) {
            if (textSize > 0) {
                AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(UIUtils.dip2px(ClientInfo.scale, textSize));
                styles.put(new SpannableStringStyle(start, start + textStr.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE), sizeSpan);
            }
            if (textColor != -1) {
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(textColor);
                styles.put(new SpannableStringStyle(start, start + textStr.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE), foregroundColorSpan);
            }
            if (listener != null) {
                ShowAllSpan showAllSpan = new ShowAllSpan(MarkSixApp.getApplication().getApplicationContext(), listener);
                styles.put(new SpannableStringStyle(start, start + textStr.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE), showAllSpan);
            }
            stringBuffer.append(textStr);
            start = start + textStr.length();
        }
    }

    public void addBackgroundShape(int bgColor, int textColor, int mSize, int radius, String textStr) {
        if (CommonUtils.StringNotNull(textStr)) {
            RoundBackgroundColorSpan span = new RoundBackgroundColorSpan(bgColor, textColor
                    , UIUtils.dip2px(ClientInfo.scale, mSize), UIUtils.dip2px(ClientInfo.scale, radius));
            styles.put(new SpannableStringStyle(start, start + textStr.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE), span);
            stringBuffer.append(textStr);
            start = start + textStr.length();
        }
    }

    public SpannableString toSpannableString() {
        SpannableString result = new SpannableString(stringBuffer.toString());
        for (SpannableStringStyle style : styles.keySet()) {
            result.setSpan(styles.get(style), style.start, style.end, style.flags);
        }
        return result;
    }

    public class SpannableStringStyle {
        int start;
        int end;
        int flags;

        public SpannableStringStyle(int start, int end, int flags) {
            this.start = start;
            this.end = end;
            this.flags = flags;
        }
    }

    public static SpannableString addImgToStart(Context context, int imgId, String textContent) {
        ImageSpan span = new ImageSpan(context, imgId);
        SpannableString spanStr = new SpannableString(" " + textContent);
        spanStr.setSpan(span, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spanStr;

    }


    /**
     * 搜索关键字高亮
     *
     * */
    public static SpannableString matcherSearchTitle(int color, String text, String keyword) {
        SpannableString s = new SpannableString(text);
        keyword=escapeExprSpecialWord(keyword);
        text=escapeExprSpecialWord(text);
        if (text.contains(keyword)&&!TextUtils.isEmpty(keyword)){
            try {
                Pattern p = Pattern.compile(keyword);
                Matcher m = p.matcher(s);
                while (m.find()) {
                    int start = m.start();
                    int end = m.end();
                    s.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }catch (Exception e){
            }
        }
        return s;
    }
    public static String escapeExprSpecialWord(String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }
}
