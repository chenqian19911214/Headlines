package com.marksixinfo.widgets.umengshare;

import com.umeng.socialize.media.UMImage;

/**
 * @Auther: Administrator
 * @Date: 2019/3/23 0023 13:53
 * @Description:
 */
public class ShareParams {
    /**
     * 分享内容
     */
    private String content;
    /**
     * 分享标题
     */
    private String title;
    /**
     * 分享图片
     */
    private UMImage umImage;
    /**
     * 分享链接
     */
    private String url;
    /**
     * 回调
     */
    private UMengShareUtils.MyShareListener listener;


    public ShareParams() {

    }

    public ShareParams(String title, String content, UMImage umImage, String url, UMengShareUtils.MyShareListener listener) {
        this.title = title;
        this.content = content;
        this.umImage = umImage;
        this.url = url;
        this.listener = listener;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UMImage getUmImage() {
        return umImage;
    }

    public void setUmImage(UMImage umImage) {
        this.umImage = umImage;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UMengShareUtils.MyShareListener getListener() {
        return listener;
    }

    public void setListener(UMengShareUtils.MyShareListener listener) {
        this.listener = listener;
    }
}

