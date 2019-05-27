package com.marksixinfo.bean;

import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2019/3/27 0027 21:39
 * @Description:
 */
public class MineCollectData {

    /**
     * total : 11
     * size : 10
     * page_num : 2
     */

    private String total;
    private int size;
    private int page_num;
    private List<MainHomeData> list;

    public List<MainHomeData> getList() {
        return list;
    }

    public void setList(List<MainHomeData> list) {
        this.list = list;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage_num() {
        return page_num;
    }

    public void setPage_num(int page_num) {
        this.page_num = page_num;
    }
}
