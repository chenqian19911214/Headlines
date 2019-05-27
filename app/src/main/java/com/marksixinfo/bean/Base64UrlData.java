package com.marksixinfo.bean;

/**
 * @Auther: Administrator
 * @Date: 2019/3/22 0022 20:51
 * @Description:
 */
public class Base64UrlData {

    private String base64;
    private String imageUrl;

    public Base64UrlData() {
    }

    public Base64UrlData(String base64, String imageUrl) {
        this.base64 = base64;
        this.imageUrl = imageUrl;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Base64UrlData that = (Base64UrlData) o;

        if (base64 != null ? !base64.equals(that.base64) : that.base64 != null) return false;
        return imageUrl != null ? imageUrl.equals(that.imageUrl) : that.imageUrl == null;
    }

    @Override
    public int hashCode() {
        int result = base64 != null ? base64.hashCode() : 0;
        result = 31 * result + (imageUrl != null ? imageUrl.hashCode() : 0);
        return result;
    }
}
