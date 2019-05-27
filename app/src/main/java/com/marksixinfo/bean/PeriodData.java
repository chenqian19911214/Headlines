package com.marksixinfo.bean;

/**
 * Created by lenovo on 2019/5/13.
 */

public class PeriodData {

    /**
     * Id : 2019051
     * Period : 51
     */

    private String Id;
    private String Period;
    private String galleryId;
    private int position;

    private String newPeriod;

    public String getNewPeriod() {
        return newPeriod;
    }

    public void setNewPeriod(String newPeriod) {
        this.newPeriod = newPeriod;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(String galleryId) {
        this.galleryId = galleryId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String Period) {
        this.Period = Period;
    }
}
