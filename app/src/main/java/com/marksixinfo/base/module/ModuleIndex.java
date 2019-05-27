package com.marksixinfo.base.module;

/**
 * 索引
 */

public class ModuleIndex {
    Class<? extends ModuleBaseViewHolder> holderClass;
    Class<? extends ModuleBaseMode> modeClass;
    String key;

    public ModuleIndex(String key, Class<? extends ModuleBaseViewHolder> holderClass, Class<? extends ModuleBaseMode> modeClass) {
        this.holderClass = holderClass;
        this.modeClass = modeClass;
        this.key = key;
    }

    public Class<? extends ModuleBaseViewHolder> getHolderClass() {
        return holderClass;
    }

    public void setHolderClass(Class<? extends ModuleBaseViewHolder> holderClass) {
        this.holderClass = holderClass;
    }

    public Class<? extends ModuleBaseMode> getModeClass() {
        return modeClass;
    }

    public void setModeClass(Class<? extends ModuleBaseMode> modeClass) {
        this.modeClass = modeClass;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
