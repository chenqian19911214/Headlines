package com.marksixinfo.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2019/3/19 0019 16:03
 * @Description:
 */
public class HtmlUtil {
    // css样式，隐藏header
    private static final String HIDE_HEADER_STYLE = "<style>div.headline{display:none;}</style>";

    // css style tag, 需要格式化
    private static final String NEEDED_FORMAT_CSS_TAG = "<link rel=\"stylesheet\" type=\"text/css\" href=\"%s\"/>";

    // js script tag, 需要格式化
    private static final String NEEDED_FORMAT_JS_TAG = "<script src=\"%s\"></script>";

    public static final String MIME_TYPE = "text/html; charset=utf-8";

    public static final String ENCODING = "utf-8";

    public HtmlUtil() {
    }

    /**
     * 根据css链接生成Link标签
     *
     * @param url String
     * @return String
     */
    public static String createCssTag(String url) {
        return String.format(NEEDED_FORMAT_CSS_TAG, url);
    }

    /**
     * 根据多个css链接生成Link标签
     *
     * @param urls List<String>
     * @return String
     */
    public static String createCssTag(List<String> urls) {
        final StringBuilder sb = new StringBuilder();
        for (String url : urls) {
            sb.append(createCssTag(url));
        }
        return sb.toString();
    }

    /**
     * 根据js链接生成Script标签
     *
     * @param url String
     * @return String
     */
    public static String createJsTag(String url) {

        return String.format(NEEDED_FORMAT_JS_TAG, url);
    }

    /**
     * 根据多个js链接生成Script标签
     *
     * @param urls List<String>
     * @return String
     */
    public static String createJsTag(List<String> urls) {

        final StringBuilder sb = new StringBuilder();
        for (String url : urls) {
            sb.append(createJsTag(url));
        }
        return sb.toString();
    }

    /**
     * 根据样式标签,html字符串,js标签
     * 生成完整的HTML文档
     */
    public static String createHtmlData(String html, List<String> cssList, List<String> jsList) {
        final String css = HtmlUtil.createCssTag(cssList);
        final String js = HtmlUtil.createJsTag(jsList);
        return css.concat(HIDE_HEADER_STYLE).concat(html).concat(js);
    }

    String urlStr = "343434<br>343434343<br> <img src='https://images.34399.com//files/20190318/111114.png' <br><img src='https://images.34399.com//files/20190318/111114.png' />";

    public static List<String> getImageUrl(String body) {
        List<String> list = new ArrayList<>();
        String ss = "<img src=";
        if (CommonUtils.StringNotNull(body) && body.contains(ss)) {


            char[] chars = body.toCharArray();


            if (chars.length > 0) {

//                chars
            }


        }


        return list;
    }

    public static void main(String[] aa) {
        String urlStr = "343434<br>343434343<br> <img src='https://images.34399.com//files/20190318/111114.png' <br><img src='https://images.34399.com//files/20190318/111114.png' />";

        List<String> list = new ArrayList<>();
        String ss = "<img src=";
        if (CommonUtils.StringNotNull(urlStr) && urlStr.contains(ss)) {
//"'https://images.34399.com//files/20190318/111114.png' <br>"
            String[] split = urlStr.split(ss);

            if (split.length > 0) {
                for (int i = 0; i < split.length; i++) {
                    String s = split[i];
                    if (s.startsWith("https")) {

                        int https = s.indexOf("https");

                    } else if (s.startsWith("http")) {


                        int http = s.indexOf("http");
                    }


                }
            }

            char[] chars = urlStr.toCharArray();


            if (chars.length > 0) {
                char aChar = chars[1];
            }
        }
    }
}
