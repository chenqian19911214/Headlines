package com.marksixinfo.base.module;

/**
 * Created by yishen on 2018/4/2.
 */

public class ModulePosition {

    String Key;

    ModuleBaseMode baseMode;



    int position;
    int viewType;
    public ModulePosition(String key, ModuleBaseMode baseMode, int position) {
        this.Key = key;
        this.viewType = ModuleBaseUtils.getCMSViewType(Key);
        this.baseMode = baseMode;
        this.position = position;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public ModuleBaseMode getBaseMode() {
        return baseMode;
    }

    public void setBaseMode(ModuleBaseMode baseMode) {
        this.baseMode = baseMode;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
