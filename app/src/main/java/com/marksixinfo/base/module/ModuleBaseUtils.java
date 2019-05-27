package com.marksixinfo.base.module;


import com.marksixinfo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by yishen on 2018/3/26.
 * CMS维护工具类，主要存放key，ViewHolder，mode，viewType的对应关系
 */

public class ModuleBaseUtils {

    static ArrayList<ModuleIndex> indices;
    static HashMap<String, Integer> viewType;
    static HashMap<String, Class<? extends ModuleBaseMode>> modeClass;
    static HashMap<String, Class<? extends ModuleBaseViewHolder>> holderClass;
    static HashMap<Integer, Class<? extends ModuleBaseViewHolder>> viewTypeholderClass;
    static final int start = 10086;

    static {
        if (!CommonUtils.ListNotNull(indices)) {
            viewType = new HashMap<>();
            modeClass = new HashMap<>();
            holderClass = new HashMap<>();
            viewTypeholderClass = new HashMap<>();
            indices = new ArrayList();

//            indices.add(new ModuleIndex(Navigation, ModuleBaseViewHolder.class, CMSNavigation.class));


            int i = start;
            for (ModuleIndex moduleIndex : indices) {
                viewType.put(moduleIndex.getKey(), i);
                modeClass.put(moduleIndex.getKey(), moduleIndex.getModeClass());
                holderClass.put(moduleIndex.getKey(), moduleIndex.getHolderClass());
                viewTypeholderClass.put(i, moduleIndex.getHolderClass());
                i++;
            }
        }

    }

    public static boolean isCMSHolder(int viewType) {
        return viewType >= start;
    }

    /**
     * 根据Key返回对应的Mode
     *
     * @param key
     * @return
     */
    public static Class<? extends ModuleBaseMode> getCMSModeClass(String key) {
        if (modeClass.containsKey(key)) {
            return modeClass.get(key);
        }
        return null;
    }

    /**
     * 根据Key返回对应的ViewHolder
     *
     * @param key
     * @return
     */
    public static Class<? extends ModuleBaseViewHolder> getCMSHolderClass(String key) {
        if (holderClass.containsKey(key)) {
            return holderClass.get(key);
        }
        return null;
    }

    /**
     * 根据viewType返回对应的ViewHolder
     *
     * @param viewType
     * @return
     */
    public static Class<? extends ModuleBaseViewHolder> getCMSHolderClass(int viewType) {
        if (viewTypeholderClass.containsKey(viewType)) {
            return viewTypeholderClass.get(viewType);
        }
        return null;
    }

    /**
     * 根据Key返回对应的viewType
     *
     * @param key
     * @return
     */
    public static int getCMSViewType(String key) {
        if (viewType.containsKey(key)) {
            return viewType.get(key);
        }
        return -1;
    }
}
