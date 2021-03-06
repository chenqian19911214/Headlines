package com.marksixinfo.utils;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: Administrator
 * @Date: 2019/5/7 0007 16:26
 * @Description:
 */
public class EmojiFilter implements InputFilter {

    Pattern emoji = Pattern.compile(

            "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",

            Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        Matcher emojiMatcher = emoji.matcher(source);

        if (emojiMatcher.find()) {
            return "";
        }
        return null;


    }

}
