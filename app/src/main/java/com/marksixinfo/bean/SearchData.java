package com.marksixinfo.bean;

import java.util.List;

/**
 *  头条搜索头部用户
 *
 * @Auther: Administrator
 * @Date: 2019/5/24 0024 19:04
 * @Description:
 */
public class SearchData {

    private List<MainHomeData> list;
    private List<MineConcernData> member;

    public List<MainHomeData> getList() {
        return list;
    }

    public void setList(List<MainHomeData> list) {
        this.list = list;
    }

    public List<MineConcernData> getMember() {
        return member;
    }

    public void setMember(List<MineConcernData> member) {
        this.member = member;
    }
}
