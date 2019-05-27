package com.marksixinfo.evenbus;

/**
 * Created by lenovo on 2019/5/16.
 */

public class SendGalleryTitle {
    String titleName;

    public SendGalleryTitle(String titleName) {
        this.titleName = titleName;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }
}
