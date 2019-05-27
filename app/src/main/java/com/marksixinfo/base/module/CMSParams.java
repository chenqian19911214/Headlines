package com.marksixinfo.base.module;

import android.os.Parcel;
import android.os.Parcelable;

import com.marksixinfo.base.BaseNetUtil;
import com.marksixinfo.constants.StringConstants;

import java.util.HashMap;

/**
 * Created by yishen on 2018/4/9.
 * CMS接口请求实
 */

public class CMSParams implements Parcelable {

    public static final String saveKey="CMSParams";
    int pageSize = 10;
    int pageIndex = 0;
    long branchId;  //类目id 一级类目id
    long moduleId;//组件id 用于单条请求接口
    int positionId;//holder位置
    String token;
    String path = "";

    public CMSParams(String token) {
        this.token=token;
    }
    public CMSParams() {
    }

    public void clear(){
        pageIndex =0;
        path = "";
        positionId = 0;
        branchId = 0;
        moduleId = 0;
    }


    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public long getBranchId() {
        return branchId;
    }

    public CMSParams setBranchId(long branchId) {
        this.branchId = branchId;
        return this;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPath() {
        return path;
    }

    public CMSParams setPath(String path) {
        this.path = path;
        return this;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public long getModuleId() {
        return moduleId;
    }

    public void setModuleId(long moduleId) {
        this.moduleId = moduleId;
    }

    public HashMap<String,String> getCMSParams()
    {
        HashMap<String,String> p=new HashMap<>();
//        BaseNetUtil.putStringParams(p, NetUrlHeadline.UC_TO, token);
//        BaseNetUtil.putStringParams(p, NetUrlHeadline.ZS_PATH, path);
//        BaseNetUtil.putStringParams(p, NetUrlHeadline.ZS_PAGE, String.valueOf(pageIndex));
//        BaseNetUtil.putStringParams(p, NetUrlHeadline.ZS_PAGESIZE, String.valueOf(pageSize));
//        BaseNetUtil.putStringParams(p, NetUrlHeadline.SY_PA, String.valueOf(pageIndex));
        if(branchId>0) {
//            BaseNetUtil.putStringParams(p, NetUrlHeadline.GS_ID, String.valueOf(branchId));
        }
        if(moduleId>0) {
//            BaseNetUtil.putStringParams(p, NetUrlHeadline.GS_ID, String.valueOf(moduleId));
        }
        return p;
    }

    public HashMap<String,String> getHappyBuyCMSParams() {
        HashMap<String,String> p=new HashMap<>();
//        BaseNetUtil.putStringParams(p, NetUrlHeadline.UC_TO, token);
//        BaseNetUtil.putStringParams(p, NetUrlHeadline.PATH, path);
        BaseNetUtil.putStringParams(p, StringConstants.PAGE, String.valueOf(pageIndex));
//        BaseNetUtil.putStringParams(p, NetUrlHeadline.PAGE_SIZE, String.valueOf(pageSize));
        if (branchId > 0) {
            BaseNetUtil.putStringParams(p, StringConstants.ID, String.valueOf(branchId));
        }
        if (moduleId > 0) {
            BaseNetUtil.putStringParams(p, StringConstants.ID, String.valueOf(moduleId));
        }
        return p;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(pageSize);
        parcel.writeInt(pageIndex);
        parcel.writeLong(branchId);
        parcel.writeLong(moduleId);
        parcel.writeInt(positionId);
        parcel.writeString(token);
        parcel.writeString(path);
    }

    protected CMSParams(Parcel in) {
        pageSize = in.readInt();
        pageIndex = in.readInt();
        branchId = in.readLong();
        moduleId = in.readLong();
        positionId = in.readInt();
        token = in.readString();
        path = in.readString();
    }

    public static final Creator<CMSParams> CREATOR = new Creator<CMSParams>() {
        @Override
        public CMSParams createFromParcel(Parcel in) {
            return new CMSParams(in);
        }

        @Override
        public CMSParams[] newArray(int size) {
            return new CMSParams[size];
        }
    };
}
