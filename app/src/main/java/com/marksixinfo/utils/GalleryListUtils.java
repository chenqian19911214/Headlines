package com.marksixinfo.utils;

import com.marksixinfo.bean.GalleryListData;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 图库Utils
 *
 * @Auther: Administrator
 * @Date: 2019/5/8 0008 19:43
 * @Description:
 */
public class GalleryListUtils {


    public static GalleryListUtils getInstance() {
        return new GalleryListUtils();
    }

    /**
     * key对应的position
     */
    public Map<String, List<Integer>> keyIndex = new LinkedHashMap<>();


    /**
     * 处理接口数据
     *
     * @param list
     * @return
     */

    public List<GalleryListData.ChildBean> handlerList(List<GalleryListData> list) {
        List<GalleryListData.ChildBean> items = new ArrayList<>();
        if (CommonUtils.ListNotNull(list)) {
            keyIndex.clear();
            for (int i = 0; i < list.size(); i++) {
                GalleryListData galleryListData = list.get(i);
                if (galleryListData != null) {
                    String keys = galleryListData.getKeys();
                    ArrayList<Integer> integers = new ArrayList<>();
                    integers.add(items.size());
                    List<GalleryListData.ChildBean> child = galleryListData.getChild();
                    if (CommonUtils.ListNotNull(child)) {
                        for (int j = 0; j < child.size(); j++) {
                            GalleryListData.ChildBean childBean = child.get(j);
                            if (childBean != null) {
                                ///childBean.setKeys(keys); //qian.gai
                            }
                        }
                        items.addAll(child);
                    }
                    integers.add(items.size());
                    if (!keyIndex.containsKey(keys)) {
                        keyIndex.put(keys, integers);
                    }
                }
            }
        }
        return items;
    }
   /* public List<GalleryListData.ChildBean> handlerList(List<GalleryListData> list) {
        List<GalleryListData.ChildBean> items = new ArrayList<>();
        if (CommonUtils.ListNotNull(list)) {
            keyIndex.clear();
            for (int i = 0; i < list.size(); i++) {
                GalleryListData galleryListData = list.get(i);
                if (galleryListData != null) {
                    String keys = galleryListData.getKeys();
                    ArrayList<Integer> integers = new ArrayList<>();
                    integers.add(items.size());
                    List<GalleryListData.ChildBean> child = galleryListData.getChild();
                    if (CommonUtils.ListNotNull(child)) {
                        for (int j = 0; j < child.size(); j++) {
                            GalleryListData.ChildBean childBean = child.get(j);
                            if (childBean != null) {
                                childBean.setKeys(keys);
                            }
                        }
                        items.addAll(child);
                    }
                    integers.add(items.size());
                    if (!keyIndex.containsKey(keys)) {
                        keyIndex.put(keys, integers);
                    }
                }
            }
        }
        return items;
    }*/

    /**
     * 通过key找到相应的位置
     *
     * @param position
     * @param oldKey
     * @return
     */
    public String finKeyByPosition(int position, String oldKey) {
        if (CommonUtils.MapNotNull(keyIndex)) {
            for (Map.Entry<String, List<Integer>> entry : keyIndex.entrySet()) {
                if (entry != null) {
                    List<Integer> value = entry.getValue();
                    if (value != null) {
                        Integer integer1 = value.get(0);
                        Integer integer2 = value.get(1);
                        if (position >= integer1 && position <= integer2) {
                            return entry.getKey();
                        }
                    }
                }
            }
        }
        return oldKey;
    }

    /**
     * 获取索引集合
     *
     * @return
     */
    public List<String> getIndexBarNames() {
        List<String> list = new ArrayList<>();
        if (CommonUtils.MapNotNull(keyIndex)) {
            for (Map.Entry<String, List<Integer>> entry : keyIndex.entrySet()) {
                if (entry != null) {
                    list.add(entry.getKey());
                }
            }
        }
        return list;
    }

}
