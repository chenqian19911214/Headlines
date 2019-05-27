package com.marksixinfo.bean;

import java.util.List;

/**
 *当前版本名称
 */
public class EditionNameDate {


    /**
     * version : 1.0.0
     * state : 0
     * url : http://www.baidu.com
     * news : ["更新内容1","更新内容2","更新内容3","更新内容4","更新内容5"]
     */

    /*
    * 1.2
    * 1.3
    * */
    private String version;

    /**
     *  0: 不更
     *  1：选择更新
     *  2：强制更新
     * */
    private String state;
    private String url;
    private List<String> news;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getNews() {
        return news;
    }

    public void setNews(List<String> news) {
        this.news = news;
    }
}
